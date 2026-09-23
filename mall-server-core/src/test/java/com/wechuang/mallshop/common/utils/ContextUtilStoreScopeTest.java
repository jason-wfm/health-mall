package com.wechuang.mallshop.common.utils;

import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.common.consts.ConstantRole;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * [healthmall-ext] ContextUtil 身份×端别过滤矩阵测试（spec §4.2）
 * manage：商家(2)维持本店；门店(3)收窄为本店（越权修复）；平台/租户(9/8)放行；
 * 空身份降级返回 0（fail-fast 已上移 Web 层 ManageIdentityFailFastInterceptor，本工具须可安全回调）
 * front：解析属性（STORE_ACCESS_STORE_ID）优先，query 参数仅兼容期兜底
 */
class ContextUtilStoreScopeTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
        injectServletRequest(null);
    }

    private MockHttpServletRequest mockRequest(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        injectServletRequest(request);
        return request;
    }

    private void loginAs(int roleId, Integer storeId, Integer siteId) {
        UserBase user = new UserBase();
        user.setRoleId(roleId);
        user.setStoreId(storeId);
        user.setSiteId(siteId);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    /** 测试环境无 Spring 注入，反射替换 RequestUtil 的静态 request 以驱动参数兜底分支 */
    private static void injectServletRequest(HttpServletRequest request) {
        try {
            Field field = RequestUtil.class.getDeclaredField("staticServletRequest");
            field.setAccessible(true);
            field.set(null, request);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // ---------- getStoreId：manage 侧 ----------

    @Test
    void manageSellerKeepsOwnStoreId() {
        // 回归锚点：商家(2)维持本店过滤（P1 双保险，P2 多店上线时移除）
        mockRequest("/manage/trade/order/base");
        loginAs(ConstantRole.ROLE_SELLER, 7, 0);
        assertEquals(Integer.valueOf(7), ContextUtil.getStoreId());
    }

    @Test
    void manageChainRoleNarrowedToOwnStore() {
        // 越权修复：门店(3)原静默返回 0（全表），现收窄为本店
        mockRequest("/manage/trade/order/base");
        loginAs(ConstantRole.ROLE_CHAIN, 7, 0);
        assertEquals(Integer.valueOf(7), ContextUtil.getStoreId());
    }

    @Test
    void manageChainRoleNullStoreFallsToZero() {
        mockRequest("/manage/trade/order/base");
        loginAs(ConstantRole.ROLE_CHAIN, null, 0);
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
    }

    @Test
    void managePlatformAndTenantPassThrough() {
        mockRequest("/manage/trade/order/base");
        loginAs(ConstantRole.ROLE_ADMIN, 0, 0);
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
        loginAs(ConstantRole.ROLE_SITE, 0, 1001);
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
    }

    @Test
    void manageWithoutLoginDegradesToZero() {
        // Task 6 评审 Critical 修复：空身份降级返回 0（不抛异常），鉴权 fail-fast 上移 Web 层拦截器；
        // 本工具在 JWT 预认证 SQL 窗口内被租户拦截器回调，抛异常会被 JWT 过滤器吞成 250 导致 manage 全挂
        mockRequest("/manage/trade/order/base");
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
    }

    @Test
    void manageRoleIdNullDegradesToZero() {
        // 身份存在但 roleId 缺失：同样降级（拦截器侧按无身份拒绝）
        mockRequest("/manage/trade/order/base");
        UserBase user = new UserBase();
        user.setRoleId(null);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
    }

    // ---------- getStoreId：front 侧 ----------

    @Test
    void frontResolvedAttributeWins() {
        // 解析结果（请求属性，T4 StoreAccessService 写入）优先于 query 参数
        MockHttpServletRequest request = mockRequest("/front/pt/product/base");
        request.setParameter("store_id", "9");
        request.setAttribute("STORE_ACCESS_STORE_ID", 5);
        assertEquals(Integer.valueOf(5), ContextUtil.getStoreId());
    }

    @Test
    void frontNoAttributeFallsBackToParam() {
        // 兼容期兜底：无解析属性时沿用 query 参数（strict 越界由 T7 StoreAccessFilter 拦截）
        MockHttpServletRequest request = mockRequest("/front/pt/product/base");
        request.setParameter("store_id", "9");
        assertEquals(Integer.valueOf(9), ContextUtil.getStoreId());
    }

    @Test
    void frontSentinelZeroFallsBackToParam() {
        // 解析未命中缓存 0 哨兵 → 同样落参数兜底（聚合页语义）
        MockHttpServletRequest request = mockRequest("/front/pt/product/base");
        request.setParameter("store_id", "9");
        request.setAttribute("STORE_ACCESS_STORE_ID", 0);
        assertEquals(Integer.valueOf(9), ContextUtil.getStoreId());
    }

    @Test
    void frontAttributeTypeDriftFallsBackToParamWithoutFailOpen() {
        // [healthmall-ext] 类型漂移防护：属性被写成非 Integer（如 String）时不再强转 CCE
        // （原 CCE 被兜底 catch 吞掉静默返回 0 全表），落 warn 日志走 query 参数兜底
        MockHttpServletRequest request = mockRequest("/front/pt/product/base");
        request.setParameter("store_id", "9");
        request.setAttribute("STORE_ACCESS_STORE_ID", "5");
        assertEquals(Integer.valueOf(9), ContextUtil.getStoreId());
    }

    @Test
    void frontNoHintReturnsZero() {
        mockRequest("/front/pt/product/base");
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
    }

    // ---------- getStoreId：非 Web 上下文 ----------

    @Test
    void nonWebContextReturnsZero() {
        // MQ/定时任务无绑定请求：维持 0 放行，由任务侧自行设置租户上下文（spec §8，P2 落地）
        assertEquals(Integer.valueOf(0), ContextUtil.getStoreId());
    }

    // ---------- getSiteId：manage fail-fast / front 保持 R6 决策 ----------

    @Test
    void manageWithoutLoginSiteIdDegradesToZero() {
        // Task 6 评审 Critical 修复：与 getStoreId 同款，空身份降级返回 0
        mockRequest("/manage/trade/order/base");
        assertEquals(Integer.valueOf(0), ContextUtil.getSiteId());
    }

    @Test
    void manageTenantKeepsSiteId() {
        mockRequest("/manage/trade/order/base");
        loginAs(ConstantRole.ROLE_SITE, 0, 1001);
        assertEquals(Integer.valueOf(1001), ContextUtil.getSiteId());
    }

    @Test
    void managePlatformSiteIdZero() {
        mockRequest("/manage/trade/order/base");
        loginAs(ConstantRole.ROLE_ADMIN, 0, 0);
        assertEquals(Integer.valueOf(0), ContextUtil.getSiteId());
    }

    @Test
    void frontSiteIdParamKept() {
        // site_id 为地域维度，front 分支有意保留（R6 决策）
        MockHttpServletRequest request = mockRequest("/front/pt/product/base");
        request.setParameter("site_id", "1001");
        assertEquals(Integer.valueOf(1001), ContextUtil.getSiteId());
    }
}
