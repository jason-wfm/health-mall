package com.wechuang.mallshop.shop.repository;

import com.wechuang.mallshop.core.web.repository.IBaseRepository;
import com.wechuang.mallshop.shop.model.entity.StoreAccess;

/**
 * [healthmall-ext] C端门店接入定位 服务类（spec §3.3）
 */
public interface StoreAccessRepository extends IBaseRepository<StoreAccess> {

    /**
     * 按类型与接入键查询启用中的接入记录（取第一条）
     *
     * @param accessType 类型:domain-域名;appid-小程序appid
     * @param accessKey  接入键:域名(不含协议与端口)或小程序appid
     * @return 启用中的接入记录；无匹配返回 null
     */
    StoreAccess getByTypeAndKey(String accessType, String accessKey);
}
