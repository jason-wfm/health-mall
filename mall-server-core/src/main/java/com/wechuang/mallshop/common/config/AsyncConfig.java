package com.wechuang.mallshop.common.config;

import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.TaskDecorator;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置类
 * 用于传递MDC、RequestContext、LocaleContext和SecurityContext到异步任务线程
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 拷贝当前线程的 SecurityContext 快照（复用独立 context 对象，避免与请求线程的 ThreadLocal 共享可变实例）
     * 异步线程内 ContextUtil/DataScopeContext/租户拦截器依赖此上下文解析商家身份
     */
    private static SecurityContext captureSecurityContext() {
        SecurityContext source = SecurityContextHolder.getContext();
        Authentication authentication = source.getAuthentication();
        SecurityContext snapshot = SecurityContextHolder.createEmptyContext();
        snapshot.setAuthentication(authentication);
        return snapshot;
    }

    /**
     * 复合上下文传递装饰器
     * 同时传递：MDC上下文、RequestContext、LocaleContext、SecurityContext
     */
    private static class CompositeContextTaskDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            // 捕获当前线程的所有上下文
            Map<String, String> mdcContext = MdcTraceFilter.getMdcContext();
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            Locale locale = LocaleContextHolder.getLocale();
            SecurityContext securityContext = captureSecurityContext();

            return () -> {
                try {
                    // 恢复MDC上下文
                    MdcTraceFilter.clearAndSetMdcContext(mdcContext);

                    // 恢复RequestContext
                    if (requestAttributes != null) {
                        RequestContextHolder.setRequestAttributes(requestAttributes, true);
                    }

                    // 恢复LocaleContext
                    LocaleContextHolder.setLocale(locale);

                    // 恢复SecurityContext
                    SecurityContextHolder.setContext(securityContext);

                    // 执行实际的任务
                    runnable.run();
                } finally {
                    // 任务执行完成后清除所有上下文
                    MDC.clear();
                    RequestContextHolder.resetRequestAttributes();
                    LocaleContextHolder.resetLocaleContext();
                    SecurityContextHolder.clearContext();
                }
            };
        }
    }

    /**
     * 带线程信息打印的增强版TaskDecorator（用于调试）
     */
    private static class EnhancedCompositeContextTaskDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            // 捕获当前线程的所有上下文
            Map<String, String> mdcContext = MdcTraceFilter.getMdcContext();
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            Locale locale = LocaleContextHolder.getLocale();
            SecurityContext securityContext = captureSecurityContext();

            // 记录捕获上下文时的线程信息（用于调试）
            String originalThreadName = Thread.currentThread().getName();
            Locale defaultLocale = Locale.getDefault();
            boolean hasCustomLocale = locale != null && !locale.equals(defaultLocale);

            return () -> {
                String asyncThreadName = Thread.currentThread().getName();
                try {
                    // 恢复MDC上下文
                    MdcTraceFilter.clearAndSetMdcContext(mdcContext);

                    // 恢复RequestContext
                    if (requestAttributes != null) {
                        RequestContextHolder.setRequestAttributes(requestAttributes, true);
                    }

                    // 恢复LocaleContext（只有当有自定义locale时才需要记录日志）
                    if (hasCustomLocale) {
                        LocaleContextHolder.setLocale(locale);
                        // 记录日志：Locale已从原始线程传递到异步线程
                        MDC.put("original_thread", originalThreadName);
                        MDC.put("async_thread", asyncThreadName);
                        MDC.put("locale", locale.toString());
                    } else {
                        LocaleContextHolder.setLocale(locale);
                    }

                    // 恢复SecurityContext
                    SecurityContextHolder.setContext(securityContext);

                    // 执行实际的任务
                    runnable.run();
                } finally {
                    // 任务执行完成后清除所有上下文
                    MDC.clear();
                    RequestContextHolder.resetRequestAttributes();
                    LocaleContextHolder.resetLocaleContext();
                    SecurityContextHolder.clearContext();
                }
            };
        }
    }

    @Override
    @Bean("taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                super.beforeExecute(t, r);
                // 线程执行前的钩子方法
                // 可以在这里添加线程执行前的监控或日志
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                // 线程执行后的钩子方法
                // 确保清理所有ThreadLocal，防止内存泄漏
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
                LocaleContextHolder.resetLocaleContext();
                SecurityContextHolder.clearContext();
            }
        };

        // 配置线程池参数
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        // 设置自定义的TaskDecorator来传递所有上下文
        executor.setTaskDecorator(new CompositeContextTaskDecorator());

        executor.initialize();
        return executor;
    }

    /**
     * 专门的商品详情查询线程池
     * 使用增强版的TaskDecorator，包含调试信息
     */
    @Bean("productDetailExecutor")
    public Executor productDetailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 配置线程池参数（针对商品详情查询优化）
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("ProductDetail-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        // 使用增强版的TaskDecorator（带调试日志）
        executor.setTaskDecorator(new EnhancedCompositeContextTaskDecorator());

        executor.initialize();
        return executor;
    }

    /**
     * IO密集型任务线程池（如文件操作、网络请求）
     */
    @Bean("ioIntensiveExecutor")
    public Executor ioIntensiveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // IO密集型任务可以使用更多线程
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("IO-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        // 使用复合上下文传递装饰器
        executor.setTaskDecorator(new CompositeContextTaskDecorator());

        executor.initialize();
        return executor;
    }

    /**
     * CPU密集型任务线程池（如计算、数据处理）
     */
    @Bean("cpuIntensiveExecutor")
    public Executor cpuIntensiveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // CPU密集型任务线程数不宜过多，建议为CPU核心数
        int cpuCores = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(cpuCores);
        executor.setMaxPoolSize(cpuCores * 2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("CPU-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        // 使用复合上下文传递装饰器
        executor.setTaskDecorator(new CompositeContextTaskDecorator());

        executor.initialize();
        return executor;
    }
}