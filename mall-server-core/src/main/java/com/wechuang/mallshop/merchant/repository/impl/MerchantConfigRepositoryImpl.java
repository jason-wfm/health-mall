package com.wechuang.mallshop.merchant.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.merchant.dao.MerchantConfigDao;
import com.wechuang.mallshop.merchant.model.entity.MerchantConfig;
import com.wechuang.mallshop.merchant.repository.MerchantConfigRepository;
import org.springframework.stereotype.Repository;

/**
 * 商家配置表 服务实现类
 */
@Repository
public class MerchantConfigRepositoryImpl extends BaseRepositoryImpl<MerchantConfigDao, MerchantConfig> implements MerchantConfigRepository {

}
