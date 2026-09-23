package com.wechuang.mallshop.sys.job;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import com.wechuang.mallshop.trade.model.entity.OrderData;
import com.wechuang.mallshop.trade.repository.OrderDataRepository;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class UpdateActivityPrizeJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 为定时任务初始化MDC追踪信息
        MdcTraceFilter.initMdcForTask("UpdateActivityPrizeJob");

        try {
            Logger logger = LoggerFactory.getLogger(UpdateActivityPrizeJob.class);
            OrderDataRepository orderDataRepository = SpringUtil.getBean(OrderDataRepository.class);
            ActivityBaseService activityBaseService = SpringUtil.getBean(ActivityBaseService.class);
            ConfigBaseService configBaseService = SpringUtil.getBean(ConfigBaseService.class);

            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1013);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            boolean next = true;

            QueryWrapper<OrderData> orderDataQueryWrapper = new QueryWrapper<>();
            orderDataQueryWrapper.eq("order_activity_manhui_state", StateCode.CHECK_STATE_TODO);

            while (next) {
                List<OrderData> orderDataList = orderDataRepository.lists(orderDataQueryWrapper, 1, 20).getRecords();

                if (CollectionUtil.isNotEmpty(orderDataList)) {
                    for (OrderData orderData : orderDataList) {
                        try {
                            activityBaseService.doActivityVoucherPrize(orderData);
                        } catch (Exception e) {
                            orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_ERR);

                            if (!orderDataRepository.edit(orderData)) {
                                logger.error(String.format("订单：%s，更改满返优惠券发放状态异常出错", orderData.getOrderId()));
                            }
                        }
                        //清理缓存
                        configBaseService.cleanRequestCache();
                    }
                } else {
                    next = false;
                }
            }
        } finally {
            // 清除MDC信息
            MdcTraceFilter.clearMdc();
        }
    }
}
