package com.wechuang.mallshop.common.security;

import com.wechuang.mallshop.common.consts.ConstantMsg;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.consts.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * [healthmall-ext] manage 端身份 fail-fast 拦截器（Task 6 评审 Critical 修复）
 *
 * 背景：原 fail-fast 落在 ContextUtil.getSiteId()/getStoreId() 的 /manage 分支，而这两个工具
 * 会在 JwtAuthenticationFilter 设置 SecurityContext 之前的预认证 SQL 窗口内（userService.getByUserId
 * 触发租户拦截器回调）被执行，空身份抛出的 BusinessException 被 JWT 过滤器 catch(Exception) 吞成
 * 250 BAD_CREDENTIALS，导致所有带 token 的 manage 请求全部失败。
 *
 * 修复：ContextUtil 回退为可降级（空身份返回 0），鉴权 fail-fast 职责上移到本 Web 层拦截器。
 * HandlerInterceptor 在全部过滤器（含 Spring Security 链）之后执行，天然避开预认证窗口；
 * 正常情况下 /manage/** 已由安全链 authenticated() 拦下无 token 请求，本拦截器兜底覆盖
 * 安全链放行但 SecurityContext 无身份（或身份缺 roleId）的异常路径，防数据范围工具静默降级成全表。
 */
@Component
public class ManageIdentityFailFastInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 仅拦截 manage 端（注册时已限定 /manage/**，此处再作 URI 防御性校验）
        String requestURI = request.getRequestURI();
        if (requestURI == null || !requestURI.startsWith("/manage")) {
            return true;
        }

        ContextUser loginUser = ContextUtil.getLoginUser();
        if (loginUser == null || loginUser.getRoleId() == null) {
            // 与 ContextUtil 原空身份判定一致（loginUser==null 或 roleId==null）；
            // 输出结构参照 GlobalExceptionHandler/JwtAuthenticationEntryPoint：CommonRes 体（status=250 + 业务 code），
            // HTTP 状态码置 401 以区分"未认证"（本项目既有错误响应体 status 惯例为 250）
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            CommonUtil.responseError(response, Constants.RESULT_ERROR_STATUS, ConstantMsg.UNAUTHENTICATED_MSG,
                    ConstantMsg.UNAUTHENTICATED_CODE, "manage identity missing");
            return false;
        }

        return true;
    }
}
