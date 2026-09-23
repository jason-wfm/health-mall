package com.wechuang.mallshop.merchant.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.merchant.dao.MerchantApplyDao;
import com.wechuang.mallshop.merchant.model.entity.MerchantApply;
import com.wechuang.mallshop.merchant.repository.MerchantApplyRepository;
import org.springframework.stereotype.Repository;

/**
 * 商家申请单 服务实现类
 */
@Repository
public class MerchantApplyRepositoryImpl extends BaseRepositoryImpl<MerchantApplyDao, MerchantApply> implements MerchantApplyRepository {

}
