package com.wechuang.mallshop.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.merchant.model.entity.MerchantAuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家审核留痕 Mapper 接口
 */
@Mapper
public interface MerchantAuditLogDao extends BaseMapper<MerchantAuditLog> {

}
