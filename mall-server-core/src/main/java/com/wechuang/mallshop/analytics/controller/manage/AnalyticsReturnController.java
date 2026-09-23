package com.wechuang.mallshop.analytics.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.wechuang.mallshop.analytics.model.input.AnalyticsReturnInput;
import com.wechuang.mallshop.analytics.model.input.OrderItemNumTimelineInput;
import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsOrderItemNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.req.AnalyticsReturnReq;
import com.wechuang.mallshop.analytics.model.req.OrderItemNumTimelineReq;
import com.wechuang.mallshop.analytics.model.req.TimelineReq;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;
import com.wechuang.mallshop.analytics.service.AnalyticsReturnService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manage/analytics")
public class AnalyticsReturnController extends BaseController {

    @Autowired
    private AnalyticsReturnService analyticsReturnService;

    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "退单额")
    @RequestMapping(value = "/return/getReturnAmount", method = RequestMethod.GET)
    public CommonRes<AnalyticsNumOutput> getReturnAmount(AnalyticsReturnReq req) {
        AnalyticsReturnInput timelineInput = BeanUtil.copyProperties(req, AnalyticsReturnInput.class);
        AnalyticsNumOutput timelineRes = analyticsReturnService.getReturnAmount(timelineInput);

        return success(timelineRes);
    }

    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "今日退单量")
    @RequestMapping(value = "/return/getReturnNumToday", method = RequestMethod.GET)
    public CommonRes<DashboardTopRes> getReturnNumToday() {
        DashboardTopRes topRes = analyticsReturnService.getReturnNumToday();

        return success(topRes);
    }

    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "退单量")
    @RequestMapping(value = "/return/getReturnNum", method = RequestMethod.GET)
    public CommonRes<AnalyticsNumOutput> getReturnNum(AnalyticsReturnReq req) {
        AnalyticsReturnInput timelineInput = BeanUtil.copyProperties(req, AnalyticsReturnInput.class);
        AnalyticsNumOutput timelineRes = analyticsReturnService.getReturnNum(timelineInput);

        return success(timelineRes);
    }


    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "退单金额对比图")
    @RequestMapping(value = "/return/getReturnAmountTimeline", method = RequestMethod.GET)
    public CommonRes<List<TimelineOutput>> getReturnAmountTimeline(TimelineReq req) {
        TimelineInput timelineInput = BeanUtil.copyProperties(req, TimelineInput.class);
        List<TimelineOutput> amountRes = analyticsReturnService.getReturnAmountTimeline(timelineInput);

        return success(amountRes);
    }

    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "退单数量统计")
    @RequestMapping(value = "/return/getReturnNumTimeline", method = RequestMethod.GET)
    public CommonRes<List<TimelineOutput>> getReturnNumTimeline(TimelineReq req) {
        TimelineInput timelineInput = BeanUtil.copyProperties(req, TimelineInput.class);
        List<TimelineOutput> timelineRes = analyticsReturnService.getReturnNumTimeline(timelineInput);

        return success(timelineRes);
    }


    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "退单商品销量统计")
    @RequestMapping(value = "/return/getReturnItemNumTimeLine", method = RequestMethod.GET)
    public CommonRes<List<TimelineOutput>> getReturnItemNumTimeLine(OrderItemNumTimelineReq req) {
        OrderItemNumTimelineInput timelineInput = BeanUtil.copyProperties(req, OrderItemNumTimelineInput.class);
        List<TimelineOutput> timelineRes = analyticsReturnService.getReturnItemNumTimeLine(timelineInput);

        return success(timelineRes);
    }


    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "退单商品销量统计")
    @RequestMapping(value = "/return/listReturnItemNum", method = RequestMethod.GET)
    public CommonRes<List<AnalyticsOrderItemNumOutput>> listReturnItemNum(OrderItemNumTimelineReq req) {
        OrderItemNumTimelineInput timelineInput = BeanUtil.copyProperties(req, OrderItemNumTimelineInput.class);
        List<AnalyticsOrderItemNumOutput> timelineRes = analyticsReturnService.listReturnItemNum(timelineInput);

        return success(timelineRes);
    }

    @PreAuthorize("hasAuthority('/manage/analytics/index/list')")
    @Operation(summary = "商品访客数")
    @RequestMapping(value = "/return/getReturnItemNum", method = RequestMethod.GET)
    public CommonRes<AnalyticsNumOutput> getReturnItemNum(OrderItemNumTimelineReq req) {
        OrderItemNumTimelineInput timelineInput = BeanUtil.copyProperties(req, OrderItemNumTimelineInput.class);
        AnalyticsNumOutput topRes = analyticsReturnService.getReturnItemNum(timelineInput);

        return success(topRes);
    }

}
