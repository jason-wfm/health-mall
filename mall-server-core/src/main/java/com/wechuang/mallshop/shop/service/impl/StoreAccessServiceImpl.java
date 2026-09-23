package com.wechuang.mallshop.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wechuang.mallshop.shop.model.entity.StoreAccess;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.repository.StoreAccessRepository;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.service.StoreAccessService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * [healthmall-ext] C端门店定位实现（spec §3.3/§5.3）
 * 解析结果缓存于请求属性：STORE_ACCESS_STORE_ID（未解析缓存 0 哨兵，供 T6 过滤器按同名属性读取）
 */
@Slf4j
@Service
public class StoreAccessServiceImpl implements StoreAccessService {

    // ATTR_STORE_ID 常量上移至 StoreAccessService 接口（T6/T7 共用契约），实现类按简单名继承引用
    private static final String ATTR_MERCHANT_ID = "STORE_ACCESS_MERCHANT_ID";
    // [healthmall-ext] 商家推导重入哨兵：破除"查 shop_store_base → 租户拦截器回调 getMerchantId →
    // 再入本方法"的无限递归（StackOverflowError，Task 6 评审阻断级问题）
    private static final String ATTR_MERCHANT_RESOLVING = "STORE_ACCESS_MERCHANT_RESOLVING";

    private final StoreAccessRepository storeAccessRepository;
    private final StoreBaseRepository storeBaseRepository;

    public StoreAccessServiceImpl(StoreAccessRepository storeAccessRepository, StoreBaseRepository storeBaseRepository) {
        this.storeAccessRepository = storeAccessRepository;
        this.storeBaseRepository = storeBaseRepository;
    }

    @Override
    public Integer resolveStoreId() {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return null;
        }
        Object cached = request.getAttribute(ATTR_STORE_ID);
        // [healthmall-ext] 缓存类型漂移防护（Task 6 评审移交项，比照 ContextUtil Fix 4）：
        // 非 Integer 不强转（原裸强转 CCE 直接打断请求），按未缓存重新解析并覆写为正确类型
        if (cached instanceof Integer cachedId) {
            // 门店号从 1 起，缓存哨兵 0 表示本次请求已解析过但未命中，保持"解析不到返回 null"契约一致
            return cachedId > 0 ? cachedId : null;
        }
        if (cached != null) {
            log.warn("[healthmall-ext] STORE_ACCESS_STORE_ID 缓存类型异常（{}），按未缓存重新解析",
                    cached.getClass().getName());
        }
        Integer storeId = null;
        // 1) Host（nginx 前置，优先 X-Forwarded-Host），去掉端口
        String host = request.getHeader("X-Forwarded-Host");
        if (StrUtil.isBlank(host)) {
            host = request.getServerName();
        }
        if (StrUtil.isNotBlank(host) && host.contains(":")) {
            host = host.substring(0, host.indexOf(':'));
        }
        if (StrUtil.isNotBlank(host)) {
            StoreAccess access = storeAccessRepository.getByTypeAndKey("domain", host.toLowerCase());
            storeId = access != null ? access.getStoreId() : null;
        }
        // 2) 小程序 appid 参数兜底
        if (storeId == null) {
            String appid = request.getParameter("appid");
            if (StrUtil.isNotBlank(appid)) {
                StoreAccess access = storeAccessRepository.getByTypeAndKey("appid", appid);
                storeId = access != null ? access.getStoreId() : null;
            }
        }
        // 未解析缓存 0 哨兵（0=平台聚合页），避免同一请求内重复查库
        request.setAttribute(ATTR_STORE_ID, storeId != null ? storeId : 0);
        return storeId;
    }

    /**
     * [healthmall-ext] 重入破环原理：getMerchantIdByStore 查 shop_store_base 的 SQL 会经过
     * MybatisPlusConfig merchantScope 拦截器，其 ignoreTable 回调 DataScopeContext.getMerchantId()，
     * C 端分支再次调到本方法形成递归。查询前置请求级哨兵 ATTR_MERCHANT_RESOLVING，重入（哨兵已存在，
     * 即本查询自身触发的拦截器回调）直接返回 null 破环——返回 null 使拦截器对本次 SQL 不加商家过滤，
     * 不影响外层查询语义；外层 finally 清哨兵并照常写缓存。无请求上下文（MQ/定时任务）不存在回调
     * 递归路径，直接查。
     */
    @Override
    public Integer getMerchantIdByStore(Integer storeId) {
        if (storeId == null || storeId <= 0) {
            return null;
        }
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return queryMerchantId(storeId);
        }
        Object cached = request.getAttribute(ATTR_MERCHANT_ID + storeId);
        // [healthmall-ext] 缓存类型漂移防护：同 resolveStoreId，非 Integer 按未缓存走正常查询
        if (cached instanceof Integer cachedId) {
            return cachedId;
        }
        if (cached != null) {
            log.warn("[healthmall-ext] STORE_ACCESS_MERCHANT_ID{} 缓存类型异常（{}），按未缓存重新查询",
                    storeId, cached.getClass().getName());
        }
        // 重入哨兵已存在：本查询自身触发的拦截器回调，返回 null 破环（不再查库）
        if (request.getAttribute(ATTR_MERCHANT_RESOLVING) != null) {
            return null;
        }
        request.setAttribute(ATTR_MERCHANT_RESOLVING, Boolean.TRUE);
        try {
            Integer merchantId = queryMerchantId(storeId);
            if (merchantId != null) {
                // 仅命中时缓存：merchant_id=0 是合法值（平台/无归属），不能当"查不到"哨兵；查不到不缓存以保证多次调用语义一致
                request.setAttribute(ATTR_MERCHANT_ID + storeId, merchantId);
            }
            return merchantId;
        } finally {
            request.removeAttribute(ATTR_MERCHANT_RESOLVING);
        }
    }

    private Integer queryMerchantId(Integer storeId) {
        StoreBase store = storeBaseRepository.get(storeId);
        return store != null && store.getMerchantId() != null ? store.getMerchantId() : null;
    }

    /**
     * [healthmall-ext] 可空获取当前请求：非 Web 线程（Quartz/MQ 监听）无绑定请求时返回 null。
     * 不用 HttpServletUtils.getRequest()——其在无绑定时会 NPE，使无请求判空形同虚设
     */
    private HttpServletRequest currentRequest() {
        return RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes
                ? attributes.getRequest() : null;
    }
}
