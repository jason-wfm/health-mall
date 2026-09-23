package com.wechuang.mallshop.merchant.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.merchant.dao.MerchantAuditLogDao;
import com.wechuang.mallshop.merchant.model.entity.MerchantAuditLog;
import com.wechuang.mallshop.merchant.repository.MerchantAuditLogRepository;
import org.springframework.stereotype.Repository;

/**
 * 商家审核留痕 服务实现类
 */
@Repository
public class MerchantAuditLogRepositoryImpl extends BaseRepositoryImpl<MerchantAuditLogDao, MerchantAuditLog> implements MerchantAuditLogRepository {

}
