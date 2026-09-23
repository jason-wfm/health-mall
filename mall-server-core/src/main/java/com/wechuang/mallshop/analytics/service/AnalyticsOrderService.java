package com.wechuang.mallshop.analytics.service;

import com.wechuang.mallshop.analytics.model.input.AnalyticsOrderInput;
import com.wechuang.mallshop.analytics.model.input.OrderItemNumTimelineInput;
import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsOrderItemNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.AmountRes;
import com.wechuang.mallshop.analytics.model.res.DashBoardTimelineRes;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;

import java.util.List;

public interface AnalyticsOrderService {

    /**
     * 仪表盘数据
     *
     * @param input
     * @return
     */
    DashBoardTimelineRes getDashboardTimeLine(TimelineInput input);

    /**
     * 订单销售额统计
     *
     * @param input
     * @return
     */
    List<AmountRes> getSaleOrderAmount(TimelineInput input);

    /**
     * 订单数量统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getOrderNumTimeline(TimelineInput input);

    /**
     * 商品销量统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getOrderItemNumTimeLine(OrderItemNumTimelineInput input);

    AnalyticsNumOutput getOrderItemNum(OrderItemNumTimelineInput input);

    List<AnalyticsOrderItemNumOutput> listOrderItemNum(OrderItemNumTimelineInput input);

    /**
     * 订单客户统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getOrderCustomerNumTimeline(TimelineInput input);

    /**
     * 订单数量 - 计算本月订单量，今日订单量，昨日订单量，以及日环比
     *
     * @return
     */
    DashboardTopRes getOrderDashboardNum();

    AnalyticsNumOutput getOrderDashboardNum(AnalyticsOrderInput input);

    AnalyticsNumOutput getOrderDashboardAmount(AnalyticsOrderInput input);

    DashboardTopRes getVoucherActiveDashboardNum();
}
