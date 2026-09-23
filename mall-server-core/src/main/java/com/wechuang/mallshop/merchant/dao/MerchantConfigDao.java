package com.wechuang.mallshop.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.merchant.model.entity.MerchantConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家配置表 Mapper 接口
 */
@Mapper
public interface MerchantConfigDao extends BaseMapper<MerchantConfig> {

}
