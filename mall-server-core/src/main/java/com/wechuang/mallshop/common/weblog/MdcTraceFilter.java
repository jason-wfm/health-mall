package com.wechuang.mallshop.common.weblog;

import cn.hutool.core.util.StrUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MDC链路追踪过滤器
 * 在请求开始时设置traceId、globalId、spanId等信息到MDC中
 * userId将在JWT认证后通过其他方式设置
 * 
 * 提供静态方法用于非HTTP请求场景（如定时任务）手动设置MDC信息
 * 提供静态方法用于异步任务场景传递MDC上下文
 */
@Component
@Order(1) // 设置过滤器顺序，数值越小优先级越高
public class MdcTraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // 生成或获取追踪ID
            String traceId = generateOrGetTraceId(request);
            String globalId = generateOrGetGlobalId(request);
            String spanId = generateOrGetSpanId(request);
            
            // 设置MDC值（不包括userId，因为此时还没有认证信息）
            MDC.put("traceId", traceId);
            MDC.put("globalId", globalId);
            MDC.put("spanId", spanId);
            // userId将在JWT认证后设置
            
            // 继续执行过滤器链
            filterChain.doFilter(request, response);
        } finally {
            // 请求结束时清除MDC值，避免内存泄漏
            MDC.clear();
        }
    }
    
    /**
     * 生成或获取TraceId
     * @param request HttpServletRequest
     * @return traceId
     */
    private String generateOrGetTraceId(HttpServletRequest request) {
        // 优先从请求头获取，如果没有则生成新的
        String traceId = request.getHeader("X-Trace-Id");
        if (StrUtil.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        return traceId;
    }
    
    /**
     * 生成或获取GlobalId
     * @param request HttpServletRequest
     * @return globalId
     */
    private String generateOrGetGlobalId(HttpServletRequest request) {
        // 优先从请求头获取，如果没有则生成新的
        String globalId = request.getHeader("X-Global-Id");
        if (StrUtil.isBlank(globalId)) {
            globalId = UUID.randomUUID().toString().replace("-", "");
        }
        return globalId;
    }
    
    /**
     * 生成或获取SpanId
     * @param request HttpServletRequest
     * @return spanId
     */
    private String generateOrGetSpanId(HttpServletRequest request) {
        // 优先从请求头获取，如果没有则生成新的
        String spanId = request.getHeader("X-Span-Id");
        if (StrUtil.isBlank(spanId)) {
            spanId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }
        return spanId;
    }
    
    /**
     * 公共方法，用于在其他地方设置userId到MDC中
     * @param userId 用户ID
     */
    public static void setUserIdToMdc(String userId) {
        if (userId != null) {
            MDC.put("userId", userId);
        }
    }
    
    /**
     * 从安全上下文中获取用户ID并设置到MDC中
     * 这个方法可以在JWT认证成功后调用
     */
    public static void setUserIdToMdcFromContext() {
        try {
            ContextUser user = ContextUtil.getLoginUser();
            if (user != null && user.getUserId() != null) {
                MDC.put("userId", String.valueOf(user.getUserId()));
            } else {
                MDC.put("userId", "");
            }
        } catch (Exception e) {
            // 忽略异常，设置空字符串
            MDC.put("userId", "");
        }
    }
    
    /**
     * 为非HTTP请求（如定时任务）初始化MDC追踪信息
     * @param taskName 任务名称
     */
    public static void initMdcForTask(String taskName) {
        // 生成追踪ID
        String traceId = UUID.randomUUID().toString().replace("-", "");
        String globalId = UUID.randomUUID().toString().replace("-", "");
        String spanId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        
        // 设置MDC值
        MDC.put("traceId", traceId);
        MDC.put("globalId", globalId);
        MDC.put("spanId", spanId);
        MDC.put("userId", ""); // 定时任务没有用户ID概念，设置为空
        
        // 可以将任务名称作为额外信息记录
        if (taskName != null) {
            MDC.put("taskName", taskName);
        }
    }
    
    /**
     * 为非HTTP请求（如定时任务）初始化MDC追踪信息（简化版本）
     */
    public static void initMdcForTask() {
        initMdcForTask(null);
    }
    
    /**
     * 清除MDC信息
     * 在非HTTP请求结束时调用
     */
    public static void clearMdc() {
        MDC.clear();
    }
    
    /**
     * 获取当前线程的MDC上下文
     * 用于异步任务中传递MDC上下文
     * @return MDC上下文映射
     */
    public static Map<String, String> getMdcContext() {
        Map<String, String> context = new HashMap<>();
        // 获取当前MDC中的关键字段
        String traceId = MDC.get("traceId");
        String globalId = MDC.get("globalId");
        String spanId = MDC.get("spanId");
        String userId = MDC.get("userId");
        String taskName = MDC.get("taskName");
        
        if (traceId != null) context.put("traceId", traceId);
        if (globalId != null) context.put("globalId", globalId);
        if (spanId != null) context.put("spanId", spanId);
        if (userId != null) context.put("userId", userId);
        if (taskName != null) context.put("taskName", taskName);
        
        return context;
    }
    
    /**
     * 设置MDC上下文
     * 用于异步任务中恢复MDC上下文
     * @param context MDC上下文映射
     */
    public static void setMdcContext(Map<String, String> context) {
        if (context != null) {
            context.forEach(MDC::put);
        }
    }
    
    /**
     * 清除当前MDC上下文并设置新的上下文
     * @param context 新的MDC上下文映射
     */
    public static void clearAndSetMdcContext(Map<String, String> context) {
        MDC.clear();
        setMdcContext(context);
    }
}