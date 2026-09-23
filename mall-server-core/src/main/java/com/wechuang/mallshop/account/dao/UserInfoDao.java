package com.wechuang.mallshop.account.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户详细信息表 Mapper 接口
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {

    long getUserNum(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("saleId") Integer saleId);

}
