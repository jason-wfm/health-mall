package com.wechuang.mallshop.analytics.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.wechuang.mallshop.analytics.model.input.OrderItemNumTimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsOrderItemNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.AmountRes;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import com.wechuang.mallshop.analytics.model.vo.OrderNumVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AnalyticsOrderDao {
    /**
     * @param startTime
     * @param endTime
     * @param orderStateId
     * @param orderIsPaid
     * @param userId
     * @param kindId
     * @return
     */
    OrderNumVo getOrderNum(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("order_state_id") List<Integer> orderStateId, @Param("order_is_paid") List<Integer> orderIsPaid, @Param("user_id") Integer userId, @Param("kind_id") Integer kindId, @Param("chainId") Integer chainId, @Param("storeId") Integer storeId);

    List<TimelineOutput> getPayTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("chainId") Integer chainId, @Param("storeId") Integer storeId);

    @InterceptorIgnore(tenantLine = "true")
    List<AmountRes> getSaleOrderAmount(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("chainId") Integer chainId, @Param("storeId") Integer storeId);

    CommonNumVo getOrderAmount(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("storeId") Integer storeId);

    List<TimelineOutput> getOrderTimeLine(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("chainId") Integer chainId, @Param("storeId") Integer storeId);

    List<TimelineOutput> getOrderItemNumTimeLine(@Param("params") OrderItemNumTimelineInput params);

    CommonNumVo getOrderItemNum(@Param("params") OrderItemNumTimelineInput params);

    List<AnalyticsOrderItemNumOutput> listOrderItemNum(@Param("params") OrderItemNumTimelineInput params);

    @InterceptorIgnore(tenantLine = "true")
    List<TimelineOutput> getOrderCustomerNumTimeline(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("chainId") Integer chainId, @Param("storeId") Integer storeId);

    CommonNumVo getVoucherActiveNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("storeId") Integer storeId);
}
