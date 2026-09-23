package com.wechuang.mallshop.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.merchant.model.entity.MerchantBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家主表 Mapper 接口
 */
@Mapper
public interface MerchantBaseDao extends BaseMapper<MerchantBase> {

}
