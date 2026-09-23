package com.wechuang.mallshop.common.security;

import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置
 *
 * @author Xinze
 * @since 2019-03-23 18:04:52
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Resource
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Resource
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // [healthmall-ext] C端门店定位过滤器（Task 7）：紧跟 JWT 过滤器之后注册，@Order 已区分先后
    @Resource
    private StoreAccessFilter storeAccessFilter;

    @Resource
    private MdcTraceFilter mdcTraceFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/front/sys/upload/**", "/api/captcha", "/").permitAll()
                        .requestMatchers(
                                "/api/login",
                                "/front/account/login/**",
                                "/front/sys/config/**",
                                "/front/sys/captcha/**",
                                "/front/sys/district/**",
                                "/front/sys/**",
                                "/front/pt/**",
                                "/front/**",
                                "/druid/**",
                                "/doc.html",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/swagger-ui/**",
                                "/pc/**",
                                "/h5/**",
                                "/admin/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                }) // 启用 CORS
                .logout(logout -> logout.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                // 添加MDC追踪过滤器，确保在JWT认证过滤器之前执行
                .addFilterBefore(mdcTraceFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // [healthmall-ext] 添加C端门店定位过滤器（Task 7）：/front 请求先解析 Host/appid 定位门店，
                // strict 模式下 query store_id 越界直接拒绝；与 JWT 过滤器同注册位置，先后由注册顺序与 @Order 保证
                .addFilterBefore(storeAccessFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}