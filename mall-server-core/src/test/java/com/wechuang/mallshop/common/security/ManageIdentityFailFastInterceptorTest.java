package com.wechuang.mallshop.common.security;

import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.common.consts.ConstantMsg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [healthmall-ext] manage 端身份 fail-fast 拦截器测试（Task 6 评审 Critical 修复）
 * ContextUtil 空身份降级返回 0 后，manage 无身份的鉴权兜底由本拦截器承担
 */
class ManageIdentityFailFastInterceptorTest {

    private final ManageIdentityFailFastInterceptor interceptor = new ManageIdentityFailFastInterceptor();

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    private void loginAs(Integer roleId) {
        UserBase user = new UserBase();
        user.setRoleId(roleId);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    void manageWithoutIdentityRejectedWith401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/manage/trade/order/base");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        // HTTP 401 + CommonRes 体（status=250 惯例 + 业务 code 401），结构对齐 JwtAuthenticationEntryPoint
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains(ConstantMsg.UNAUTHENTICATED_MSG));
        assertTrue(response.getContentAsString().contains("\"code\":" + ConstantMsg.UNAUTHENTICATED_CODE));
    }

    @Test
    void manageWithRoleIdNullRejected() throws Exception {
        // 身份存在但 roleId 缺失：与 ContextUtil 原空身份判定一致，按无身份拒绝
        loginAs(null);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/manage/trade/order/base");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(401, response.getStatus());
    }

    @Test
    void manageWithIdentityPasses() throws Exception {
        loginAs(9); // 平台管理员

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/manage/trade/order/base");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(200, response.getStatus());
    }

    @Test
    void nonManagePathWithoutIdentityPasses() throws Exception {
        // 非 /manage 前缀（注册未覆盖或防御性校验）：C 端匿名放行，不受本拦截器影响
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/front/pt/product/base");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(200, response.getStatus());
    }
}
