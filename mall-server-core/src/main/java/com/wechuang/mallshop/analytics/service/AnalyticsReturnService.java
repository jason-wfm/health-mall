package com.wechuang.mallshop.analytics.service;

import com.wechuang.mallshop.analytics.model.input.AnalyticsReturnInput;
import com.wechuang.mallshop.analytics.model.input.OrderItemNumTimelineInput;
import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsOrderItemNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;

import java.util.List;

public interface AnalyticsReturnService {

    /**
     * 退单销售额统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getReturnAmountTimeline(TimelineInput input);

    /**
     * 退单数量统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getReturnNumTimeline(TimelineInput input);

    /**
     * 商品销量统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getReturnItemNumTimeLine(OrderItemNumTimelineInput input);

    AnalyticsNumOutput getReturnItemNum(OrderItemNumTimelineInput input);

    /**
     * 退单商品销量及额度统计
     *
     * @param input
     * @return
     */
    List<AnalyticsOrderItemNumOutput> listReturnItemNum(OrderItemNumTimelineInput input);

    /**
     * 退单客户统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getReturnCustomerNumTimeline(TimelineInput input);

    /**
     * 退单数量
     *
     * @return
     */
    AnalyticsNumOutput getReturnNum(AnalyticsReturnInput input);


    /**
     * 退款总额
     *
     * @return
     */
    AnalyticsNumOutput getReturnAmount(AnalyticsReturnInput input);

    /**
     * 当天退单数量
     *
     * @return
     */
    DashboardTopRes getReturnNumToday();
}
