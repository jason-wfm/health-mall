package com.wechuang.mallshop.merchant.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.merchant.dao.MerchantBaseDao;
import com.wechuang.mallshop.merchant.model.entity.MerchantBase;
import com.wechuang.mallshop.merchant.repository.MerchantBaseRepository;
import org.springframework.stereotype.Repository;

/**
 * 商家主表 服务实现类
 */
@Repository
public class MerchantBaseRepositoryImpl extends BaseRepositoryImpl<MerchantBaseDao, MerchantBase> implements MerchantBaseRepository {

}
