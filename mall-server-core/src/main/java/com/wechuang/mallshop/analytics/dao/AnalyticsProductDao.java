package com.wechuang.mallshop.analytics.dao;

import com.wechuang.mallshop.analytics.model.input.AnalyticsProductInput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnalyticsProductDao {

    List<TimelineOutput> getProductTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("storeId") Integer storeId);

    CommonNumVo getProductNum(@Param("params") AnalyticsProductInput input);
}
