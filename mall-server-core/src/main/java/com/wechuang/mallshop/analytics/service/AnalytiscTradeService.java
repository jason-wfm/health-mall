package com.wechuang.mallshop.analytics.service;

import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;

public interface AnalytiscTradeService {
    /**
     * 仪表盘销售额统计
     *
     * @return
     */
    DashboardTopRes getSalesAmount();

}
