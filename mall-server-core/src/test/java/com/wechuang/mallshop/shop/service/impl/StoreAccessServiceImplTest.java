package com.wechuang.mallshop.shop.service.impl;

import com.wechuang.mallshop.shop.model.entity.StoreAccess;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.repository.StoreAccessRepository;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * [healthmall-ext] C端门店定位解析服务单元测试（spec §3.3/§5.3）
 * 覆盖：X-Forwarded-Host 优先/去端口/小写、serverName 兜底、appid 参数兜底、
 * 请求属性缓存（STORE_ACCESS_STORE_ID 哨兵 0）、无请求上下文、商家推导缓存与空值语义
 */
class StoreAccessServiceImplTest {

    private StoreAccessRepository storeAccessRepository;
    private StoreBaseRepository storeBaseRepository;
    private StoreAccessServiceImpl storeAccessService;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        storeAccessRepository = mock(StoreAccessRepository.class);
        storeBaseRepository = mock(StoreBaseRepository.class);
        storeAccessService = new StoreAccessServiceImpl(storeAccessRepository, storeBaseRepository);

        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void resolvesByForwardedHostStrippingPortAndLowercasing() {
        request.addHeader("X-Forwarded-Host", "Shop.Example.COM:8443");
        when(storeAccessRepository.getByTypeAndKey("domain", "shop.example.com"))
                .thenReturn(new StoreAccess().setStoreId(7));

        assertEquals(Integer.valueOf(7), storeAccessService.resolveStoreId());
        assertEquals(7, request.getAttribute("STORE_ACCESS_STORE_ID"));
    }

    @Test
    void fallsBackToServerNameWhenNoForwardedHost() {
        // MockHttpServletRequest 默认 serverName=localhost（与本地种子数据一致）
        when(storeAccessRepository.getByTypeAndKey("domain", "localhost"))
                .thenReturn(new StoreAccess().setStoreId(1));

        assertEquals(Integer.valueOf(1), storeAccessService.resolveStoreId());
    }

    @Test
    void fallsBackToAppidWhenDomainMisses() {
        request.setServerName("unknown.example.com");
        request.setParameter("appid", "wx-local-dev-appid");
        when(storeAccessRepository.getByTypeAndKey("domain", "unknown.example.com")).thenReturn(null);
        when(storeAccessRepository.getByTypeAndKey("appid", "wx-local-dev-appid"))
                .thenReturn(new StoreAccess().setStoreId(1));

        assertEquals(Integer.valueOf(1), storeAccessService.resolveStoreId());
    }

    @Test
    void domainMatchWinsOverAppid() {
        request.addHeader("X-Forwarded-Host", "shop.example.com");
        request.setParameter("appid", "wx-other-appid");
        when(storeAccessRepository.getByTypeAndKey("domain", "shop.example.com"))
                .thenReturn(new StoreAccess().setStoreId(7));

        assertEquals(Integer.valueOf(7), storeAccessService.resolveStoreId());
        verify(storeAccessRepository, never()).getByTypeAndKey(eq("appid"), anyString());
    }

    @Test
    void unresolvedReturnsNullCachesZeroSentinelAndStaysNullOnRecall() {
        request.setServerName("nothing.example.com");
        when(storeAccessRepository.getByTypeAndKey(anyString(), anyString())).thenReturn(null);

        assertNull(storeAccessService.resolveStoreId());
        // 未解析缓存 0 哨兵（T6 过滤器按同名属性读取，0=平台聚合页）
        assertEquals(0, request.getAttribute("STORE_ACCESS_STORE_ID"));
        // 同一请求内再次解析：走缓存，契约保持"解析不到返回 null"
        assertNull(storeAccessService.resolveStoreId());
        verify(storeAccessRepository, times(1)).getByTypeAndKey("domain", "nothing.example.com");
    }

    @Test
    void cachesResolvedStoreIdAcrossCalls() {
        when(storeAccessRepository.getByTypeAndKey("domain", "localhost"))
                .thenReturn(new StoreAccess().setStoreId(1));

        assertEquals(Integer.valueOf(1), storeAccessService.resolveStoreId());
        assertEquals(Integer.valueOf(1), storeAccessService.resolveStoreId());
        verify(storeAccessRepository, times(1)).getByTypeAndKey(anyString(), anyString());
    }

    @Test
    void returnsNullWithoutRequestContext() {
        RequestContextHolder.resetRequestAttributes(); // 非 Web 线程（Quartz/MQ 监听）

        assertNull(storeAccessService.resolveStoreId());
        verifyNoInteractions(storeAccessRepository);
    }

    @Test
    void merchantIdResolvedAndCachedPerStore() {
        when(storeBaseRepository.get(3))
                .thenReturn(new StoreBase().setStoreId(3).setMerchantId(5));

        assertEquals(Integer.valueOf(5), storeAccessService.getMerchantIdByStore(3));
        assertEquals(5, request.getAttribute("STORE_ACCESS_MERCHANT_ID3"));
        assertEquals(Integer.valueOf(5), storeAccessService.getMerchantIdByStore(3));
        verify(storeBaseRepository, times(1)).get(3);
    }

    @Test
    void merchantIdZeroIsAValidValueNotTreatedAsMissing() {
        // merchant_id=0 是合法值（平台/无归属），不得当作"查不到"
        when(storeBaseRepository.get(1))
                .thenReturn(new StoreBase().setStoreId(1).setMerchantId(0));

        assertEquals(Integer.valueOf(0), storeAccessService.getMerchantIdByStore(1));
        assertEquals(Integer.valueOf(0), storeAccessService.getMerchantIdByStore(1));
        verify(storeBaseRepository, times(1)).get(1);
    }

    @Test
    void merchantIdNullForInvalidStoreId() {
        assertNull(storeAccessService.getMerchantIdByStore(null));
        assertNull(storeAccessService.getMerchantIdByStore(0));
        assertNull(storeAccessService.getMerchantIdByStore(-1));
        verifyNoInteractions(storeBaseRepository);
    }

    @Test
    void merchantIdNullWhenStoreMissingAndConsistentOnRecall() {
        when(storeBaseRepository.get(9)).thenReturn(null);

        assertNull(storeAccessService.getMerchantIdByStore(9));
        // 门店不存在不写缓存（0 是合法商家值，不能当哨兵），多次调用语义一致
        assertNull(storeAccessService.getMerchantIdByStore(9));
        verify(storeBaseRepository, times(2)).get(9);
    }

    // ---------- 重入哨兵（Task 6 评审阻断级修复：C 端 store_id 触发拦截器回调递归 StackOverflowError） ----------

    @Test
    void reentrantCallWithSentinelPresetReturnsNullWithoutQuery() {
        // 预设哨兵 = 本查询自身触发的租户拦截器回调重入：直接返回 null 破环，不查库
        request.setAttribute("STORE_ACCESS_MERCHANT_RESOLVING", Boolean.TRUE);

        assertNull(storeAccessService.getMerchantIdByStore(3));
        verifyNoInteractions(storeBaseRepository);
    }

    @Test
    void queryCallbackRecursionBrokenBySentinelAndClearedAfterwards() {
        // 模拟真实递归链路：查 shop_store_base 执行期间，merchant 拦截器 ignoreTable 回调
        // DataScopeContext.getMerchantId() 再次进入本方法（重入应被哨兵破掉返回 null）
        when(storeBaseRepository.get(3)).thenAnswer(invocation -> {
            assertNull(storeAccessService.getMerchantIdByStore(3));
            return new StoreBase().setStoreId(3).setMerchantId(5);
        });

        // 外层正常返回，且哨兵在 finally 中清除、缓存照常写入、后续调用走缓存
        assertEquals(Integer.valueOf(5), storeAccessService.getMerchantIdByStore(3));
        assertNull(request.getAttribute("STORE_ACCESS_MERCHANT_RESOLVING"));
        assertEquals(5, request.getAttribute("STORE_ACCESS_MERCHANT_ID3"));
        assertEquals(Integer.valueOf(5), storeAccessService.getMerchantIdByStore(3));
        verify(storeBaseRepository, times(1)).get(3);
    }

    @Test
    void sentinelClearedEvenWhenQueryThrows() {
        when(storeBaseRepository.get(3)).thenThrow(new RuntimeException("db down"));

        assertThrows(RuntimeException.class, () -> storeAccessService.getMerchantIdByStore(3));
        // 异常路径哨兵也必须清除，否则同一请求后续正常推导会被误判为重入
        assertNull(request.getAttribute("STORE_ACCESS_MERCHANT_RESOLVING"));
    }

    // ---------- 缓存属性类型漂移防护（Task 6 评审移交项，比照 ContextUtil Fix 4） ----------

    @Test
    void resolveStoreIdNonIntegerCacheTreatedAsUncached() {
        // 属性被写成非 Integer（类型漂移）：不得 CCE，按未缓存重新解析并覆写为正确类型
        request.setAttribute("STORE_ACCESS_STORE_ID", "1");
        when(storeAccessRepository.getByTypeAndKey("domain", "localhost"))
                .thenReturn(new StoreAccess().setStoreId(7));

        assertEquals(Integer.valueOf(7), storeAccessService.resolveStoreId());
        assertEquals(7, request.getAttribute("STORE_ACCESS_STORE_ID"));
    }

    @Test
    void getMerchantIdByStoreNonIntegerCacheTreatedAsUncached() {
        request.setAttribute("STORE_ACCESS_MERCHANT_ID3", "5");
        when(storeBaseRepository.get(3))
                .thenReturn(new StoreBase().setStoreId(3).setMerchantId(5));

        assertEquals(Integer.valueOf(5), storeAccessService.getMerchantIdByStore(3));
        verify(storeBaseRepository).get(3);
    }
}
