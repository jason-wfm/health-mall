package com.wechuang.mallshop.merchant.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.merchant.dao.MerchantAccountDao;
import com.wechuang.mallshop.merchant.model.entity.MerchantAccount;
import com.wechuang.mallshop.merchant.repository.MerchantAccountRepository;
import org.springframework.stereotype.Repository;

/**
 * 商家账户表 服务实现类
 */
@Repository
public class MerchantAccountRepositoryImpl extends BaseRepositoryImpl<MerchantAccountDao, MerchantAccount> implements MerchantAccountRepository {

}
