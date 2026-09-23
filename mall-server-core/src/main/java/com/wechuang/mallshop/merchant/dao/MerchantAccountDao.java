package com.wechuang.mallshop.merchant.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.merchant.model.entity.MerchantAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家账户表 Mapper 接口
 */
@Mapper
public interface MerchantAccountDao extends BaseMapper<MerchantAccount> {

}
