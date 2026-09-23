package com.wechuang.mallshop.analytics.dao;

import com.wechuang.mallshop.analytics.model.input.AccessItemTimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsAccessItemOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import com.wechuang.mallshop.analytics.model.vo.VisitorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnalyticsSysDao {
    VisitorVo getVisitor(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    CommonNumVo getVisitorNum(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    CommonNumVo getAccessNum(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TimelineOutput> getAccessItemTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("itemId") Long itemId, @Param("storeId") Integer storeId);

    CommonNumVo getAccessItemNum(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("itemId") Long itemId, @Param("storeId") Integer storeId);

    List<TimelineOutput> getAccessItemUserTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("itemId") Long itemId, @Param("storeId") Integer storeId);

    CommonNumVo getAccessItemUserNum(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("itemId") Long itemId, @Param("storeId") Integer storeId);

    List<TimelineOutput> getAccessVisitorTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<AnalyticsAccessItemOutput> listAccessItem(@Param("params") AccessItemTimelineInput params);
}
