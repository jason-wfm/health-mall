package com.wechuang.mallshop.shop.service;

/**
 * [healthmall-ext] C端门店定位（spec §3.3/§5.3）
 */
public interface StoreAccessService {

    /** [healthmall-ext] 门店解析结果请求属性名（T6/T7 共用契约）：T7 过滤器触发解析写入，T6 ContextUtil 按同名读取；值=门店号，0=未命中哨兵 */
    String ATTR_STORE_ID = "STORE_ACCESS_STORE_ID";

    /** 按当前请求 Host 或 appid 参数解析唯一启用门店；解析不到返回 null（=平台聚合页） */
    Integer resolveStoreId();

    /** 门店 → 商家推导（查 shop_store_base.merchant_id）；查不到返回 null */
    Integer getMerchantIdByStore(Integer storeId);
}
