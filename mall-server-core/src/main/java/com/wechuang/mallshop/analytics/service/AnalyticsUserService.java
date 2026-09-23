package com.wechuang.mallshop.analytics.service;

import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;

import java.util.List;

public interface AnalyticsUserService {

    DashboardTopRes getRegUser();

    /**
     * 会员数统计
     *
     * @param input
     * @return
     */
    List<TimelineOutput> getUserTimeLine(TimelineInput input);


    /**
     * 会员数
     *
     * @param input
     * @return
     */
    AnalyticsNumOutput getUserNum(TimelineInput input);

}
