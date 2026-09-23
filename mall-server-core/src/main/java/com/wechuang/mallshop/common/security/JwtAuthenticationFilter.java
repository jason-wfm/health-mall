package com.wechuang.mallshop.common.security;

import cn.hutool.core.util.StrUtil;
import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.account.service.LoginService;
import com.wechuang.mallshop.admin.model.entity.MenuBase;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.config.ConfigProperties;
import com.wechuang.mallshop.common.consts.AuthConstant;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.consts.ConstantJwt;
import com.wechuang.mallshop.common.consts.ConstantMsg;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * 处理携带token的请求过滤器
 *
 * @author Xinze
 * @since 2019-03-30 20:48:05
 */
@Component
@Order(2) // 设置过滤器顺序，数值越小优先级越高
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private ConfigProperties configProperties;
    @Resource
    private LoginService userService;

    /**
     * 站点公开配置，首屏即调且常带脏 Authorization；本过滤器完全不处理 JWT，避免解析与鉴权副作用。
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path != null && "/front/sys/config/info".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String access_token = JwtUtil.getAccessToken(request);
        if (StrUtil.isNotBlank(access_token)) {
            try {
                // 解析token
                Claims claims = JwtUtil.parseToken(access_token, configProperties.getTokenKey());
                JwtSubject jwtSubject = JwtUtil.getJwtSubject(claims);

                //可以不再次读库
                UserBase user = userService.getByUserId(jwtSubject.getUserId());

                if (user == null) {
                    throw new UsernameNotFoundException("User not found");
                }

                List<MenuBase> authorities = new ArrayList<>();

                //处理 clientId
                String servletPath = request.getServletPath();
                if (servletPath != null && servletPath.startsWith("/manage")) {
                    if (user.getUserEnable() != null && !user.getUserEnable()) {
                        throw new BusinessException(ResultCode.FORBIDDEN);
                    }

                    user.setClientId(AuthConstant.ADMIN_CLIENT_ID);

                    authorities = user.getAuthorities().stream()
                            .filter(m -> StrUtil.isNotBlank(m.getAuthority())).collect(Collectors.toList());

                    //处理操作权限判断
                    if (ConstantConfig.URL_BASE.equals("https://demo.modulithshop.cn")) {
                        if (!user.getUserId().equals(10001)) {
                            if (request.getMethod().equals("POST")) {
                                if (CheckUtil.isNotEmpty(user.getStoreId())) {

                                } else {
                                    CommonUtil.responseError(response, 250, __("演示系统，不可更改！"), 0, "");
                                    return;
                                }
                            }
                        }
                    }
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // 在JWT认证成功后，将userId设置到MDC中
                MdcTraceFilter.setUserIdToMdc(String.valueOf(user.getUserId()));
                
                // token将要过期签发新token, 防止突然退出登录
                long expiration = (claims.getExpiration().getTime() - new Date().getTime()) / 1000 / 60;
                if (expiration < configProperties.getTokenRefreshTime()) {
                    String token = JwtUtil.buildToken(jwtSubject, configProperties.getTokenExpireTime(),
                            configProperties.getTokenKey());
                    response.addHeader(ConstantJwt.TOKEN_HEADER_NAME, token);
                    //loginRecordService.saveAsync(user.getUsername(), LoginRecord.TYPE_REFRESH, null, user.getTenantId(), request);
                }
            } catch (ExpiredJwtException e) {
                CommonUtil.responseError(response, 250, ConstantJwt.TOKEN_EXPIRED_MSG, ConstantJwt.TOKEN_EXPIRED_CODE,
                        e.getMessage());
                return;
            } catch (Exception e) {
                CommonUtil.responseError(response, 250, ConstantMsg.BAD_CREDENTIALS_MSG, ConstantMsg.BAD_CREDENTIALS_CODE,
                        e.toString());
                return;
            }
        }
        chain.doFilter(request, response);
    }

}