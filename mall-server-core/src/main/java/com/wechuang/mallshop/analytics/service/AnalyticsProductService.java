package com.wechuang.mallshop.analytics.service;

import com.wechuang.mallshop.analytics.model.input.AnalyticsProductInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;

public interface AnalyticsProductService {
    /**
     * 商品数量
     *
     * @return
     */
    AnalyticsNumOutput getProductDashboardNum(AnalyticsProductInput input);
}
