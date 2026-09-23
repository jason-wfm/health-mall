package com.wechuang.mallshop.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.merchant.model.entity.MerchantApply;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家申请单 Mapper 接口
 */
@Mapper
public interface MerchantApplyDao extends BaseMapper<MerchantApply> {

}
