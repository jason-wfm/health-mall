package com.wechuang.mallshop.analytics.service;

import com.wechuang.mallshop.analytics.model.input.AccessItemTimelineInput;
import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsAccessItemOutput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;

import java.util.List;

public interface AnalyticsSysService {
    /**
     * 仪表盘 访客统计
     *
     * @return
     */
    DashboardTopRes getVisitor();

    AnalyticsNumOutput getAccessVisitorNum(AccessItemTimelineInput input);

    AnalyticsNumOutput getAccessNum(AccessItemTimelineInput input);

    /**
     * SKU访问量统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getAccessItemTimeLine(AccessItemTimelineInput input);

    AnalyticsNumOutput getAccessItemNum(AccessItemTimelineInput input);


    /**
     * SKU客户访问统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getAccessItemUserTimeLine(AccessItemTimelineInput input);

    AnalyticsNumOutput getAccessItemUserNum(AccessItemTimelineInput input);

    /**
     * 访客量统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getAccessVisitorTimeLine(TimelineInput input);

    /**
     * 商品浏览排行
     *
     * @param timelineInput
     * @return
     */
    List<AnalyticsAccessItemOutput> listAccessItem(AccessItemTimelineInput timelineInput);
}
