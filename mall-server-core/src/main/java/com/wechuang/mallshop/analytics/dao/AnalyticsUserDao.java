package com.wechuang.mallshop.analytics.dao;

import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.vo.RegUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnalyticsUserDao {
    RegUserVo getRegUser(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TimelineOutput> getUserTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
