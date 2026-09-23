package com.wechuang.mallshop.common.security;

import com.wechuang.mallshop.common.config.ConfigProperties;
import com.wechuang.mallshop.common.utils.RequestUtil;
import com.wechuang.mallshop.shop.service.StoreAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * [healthmall-ext] C端门店定位过滤器测试（spec §5.3，Task 7）
 * 覆盖：/front 前置解析接线（lenient 也解析供 ContextUtil 属性优先）、
 * strict 越界拒绝（query store_id ≠ 定位结果）、未定位/匹配/未传不拦、非 /front 路径跳过
 */
class StoreAccessFilterTest {

    private ConfigProperties configProperties;
    private StoreAccessService storeAccessService;
    private StoreAccessFilter filter;

    @BeforeEach
    void setUp() {
        configProperties = new ConfigProperties();
        storeAccessService = mock(StoreAccessService.class);
        filter = new StoreAccessFilter();
        ReflectionTestUtils.setField(filter, "configProperties", configProperties);
        ReflectionTestUtils.setField(filter, "storeAccessService", storeAccessService);
    }

    @AfterEach
    void tearDown() {
        // 解绑测试期间为 RequestUtil 静态绑定的请求，避免污染其他测试类
        ReflectionTestUtils.setField(RequestUtil.class, "staticServletRequest", null);
    }

    /**
     * [healthmall-ext] 过滤器经 RequestUtil.getParameter 读 query store_id（与 ContextUtil 兜底同通道），
     * 单测无 Spring 上下文需手动把 mock 请求绑定进 RequestUtil 静态字段
     */
    private void bindRequest(MockHttpServletRequest request) {
        RequestUtil requestUtil = new RequestUtil();
        ReflectionTestUtils.setField(requestUtil, "servletRequest", request);
        requestUtil.init();
    }

    private MockHttpServletResponse run(MockHttpServletRequest request, MockFilterChain chain)
            throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        bindRequest(request);
        filter.doFilter(request, response, chain);
        return response;
    }

    private MockHttpServletRequest frontRequest(String... params) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/front/pt/product/list");
        // Spring 6.2 MockHttpServletRequest 不再从 requestURI 推导 servletPath（真实容器如 Tomcat
        // 会填全路径，JwtAuthenticationFilter /manage 分支生产已实证），测试显式对齐容器行为
        request.setServletPath("/front/pt/product/list");
        for (int i = 0; i < params.length; i += 2) {
            request.setParameter(params[i], params[i + 1]);
        }
        return request;
    }

    // ---------- lenient（默认，兼容期）----------

    @Test
    void lenientResolvesStoreAndPassesThrough() throws Exception {
        // lenient 也必须解析：T6 ContextUtil front "属性优先"分支依赖本过滤器触发解析写属性
        configProperties.setStoreAccessStrict(false);
        when(storeAccessService.resolveStoreId()).thenReturn(1);
        MockFilterChain chain = new MockFilterChain();

        MockHttpServletResponse response = run(frontRequest(), chain);

        verify(storeAccessService).resolveStoreId();
        assertTrue(chain.getRequest() != null);
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @Test
    void lenientMismatchedQueryStoreStillPasses() throws Exception {
        // 兼容 mall-mobile query 传 store_id 的现状：lenient 不拒绝，仅属性优先覆盖
        configProperties.setStoreAccessStrict(false);
        when(storeAccessService.resolveStoreId()).thenReturn(1);
        MockFilterChain chain = new MockFilterChain();

        MockHttpServletResponse response = run(frontRequest("store_id", "999"), chain);

        assertTrue(chain.getRequest() != null);
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    // ---------- strict（收紧）----------

    @Test
    void strictMismatchedQueryStoreRejectedAndChainNotInvoked() throws Exception {
        configProperties.setStoreAccessStrict(true);
        when(storeAccessService.resolveStoreId()).thenReturn(1);
        MockFilterChain chain = new MockFilterChain();

        MockHttpServletResponse response = run(frontRequest("store_id", "999"), chain);

        // 调用形态对齐 JwtAuthenticationFilter 既有惯例：HTTP 200 + body status=250 + msg
        assertNull(chain.getRequest());
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("无权访问该门店"));
        assertTrue(response.getContentAsString().contains("\"status\":250"));
    }

    @Test
    void strictMatchingQueryStorePasses() throws Exception {
        configProperties.setStoreAccessStrict(true);
        when(storeAccessService.resolveStoreId()).thenReturn(1);
        MockFilterChain chain = new MockFilterChain();

        MockHttpServletResponse response = run(frontRequest("store_id", "1"), chain);

        assertTrue(chain.getRequest() != null);
        assertEquals("", response.getContentAsString());
    }

    @Test
    void strictAbsentOrZeroQueryStorePasses() throws Exception {
        // 未传 / store_id=0（未指定语义）不拦：已定位门店时属性优先已覆盖，无需拒绝
        configProperties.setStoreAccessStrict(true);
        when(storeAccessService.resolveStoreId()).thenReturn(1);
        MockFilterChain noParamChain = new MockFilterChain();
        MockFilterChain zeroChain = new MockFilterChain();

        run(frontRequest(), noParamChain);
        run(frontRequest("store_id", "0"), zeroChain);

        assertTrue(noParamChain.getRequest() != null);
        assertTrue(zeroChain.getRequest() != null);
    }

    @Test
    void strictUnresolvedHostNotIntercepted() throws Exception {
        // 未定位（平台聚合页语义）：strict 不拦，query store_id 兼容放行
        configProperties.setStoreAccessStrict(true);
        when(storeAccessService.resolveStoreId()).thenReturn(null);
        MockFilterChain chain = new MockFilterChain();

        MockHttpServletResponse response = run(frontRequest("store_id", "1"), chain);

        assertTrue(chain.getRequest() != null);
        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    // ---------- 非 /front 路径 ----------

    @Test
    void nonFrontPathSkipsResolutionAndPasses() throws Exception {
        configProperties.setStoreAccessStrict(true);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/manage/admin/menu/tree");
        request.setServletPath("/manage/admin/menu/tree");
        MockFilterChain chain = new MockFilterChain();

        MockHttpServletResponse response = run(request, chain);

        assertTrue(chain.getRequest() != null);
        assertEquals("", response.getContentAsString());
        verifyNoInteractions(storeAccessService);
    }
}
