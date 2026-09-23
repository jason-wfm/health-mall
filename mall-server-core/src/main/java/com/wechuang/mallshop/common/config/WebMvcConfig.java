package com.wechuang.mallshop.common.config;

import com.wechuang.mallshop.common.config.resolver.UnderlineToCamelArgumentResolver;
import com.wechuang.mallshop.common.consts.ConstantJwt;
import com.wechuang.mallshop.common.security.ManageIdentityFailFastInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvc配置, 拦截器、资源映射等都在此配置
 *
 * @author Xinze
 * @since 2019-06-12 10:11:16
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ManageIdentityFailFastInterceptor manageIdentityFailFastInterceptor;

    /**
     * 支持跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .exposedHeaders(ConstantJwt.TOKEN_HEADER_NAME)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * [healthmall-ext] manage 端身份 fail-fast 拦截器（Task 6 评审 Critical 修复）：
     * ContextUtil 数据范围工具回退为可降级后，manage 无身份的鉴权兜底由本拦截器承担；
     * HandlerInterceptor 在安全链之后执行，不会误伤 JWT 预认证 SQL 窗口。仅拦 /manage/**
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(manageIdentityFailFastInterceptor)
                .addPathPatterns("/manage/**");
    }

    /**
     * 添加参数解析器
     *
     * @param argumentResolvers 参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UnderlineToCamelArgumentResolver());
    }

}
