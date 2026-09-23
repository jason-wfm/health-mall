package com.wechuang.mallshop.sys.job;

import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import com.wechuang.mallshop.trade.service.OrderService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class UpdateOrderStatusJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 为定时任务初始化MDC追踪信息
        MdcTraceFilter.initMdcForTask("UpdateOrderStatusJob");
        
        try {
            OrderService orderService = SpringUtil.getBean(OrderService.class);

            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1001);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            // 自动取消未支付订单
            orderService.autoCancelOrder();

            // 自动确认收货
            orderService.autoReceive();

            // 退货按钮隐藏
            orderService.returnHidden();

            //推送发货信息至微信小程序
            orderService.autoUploadShipping();

            // 开票按钮隐藏
            orderService.invoiceHidden();
        } finally {
            // 清除MDC信息
            MdcTraceFilter.clearMdc();
        }
    }
}