package com.wechuang.mallshop.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.shop.model.entity.StoreAccess;
import org.apache.ibatis.annotations.Mapper;

/**
 * [healthmall-ext] C端门店接入定位 Dao
 */
@Mapper
public interface StoreAccessDao extends BaseMapper<StoreAccess> {
}
