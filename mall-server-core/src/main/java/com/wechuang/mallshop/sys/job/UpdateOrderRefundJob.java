package com.wechuang.mallshop.sys.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.pay.service.ConsumeReturnService;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import com.wechuang.mallshop.trade.service.OrderReturnService;
import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class UpdateOrderRefundJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("UpdateOrderRefundJob");

        try {
            // 非生产环境不执行
            Environment environment = SpringUtil.getBean(Environment.class);
            String[] activeProfiles = environment.getActiveProfiles();
            String activeProfile = activeProfiles[0];

            if (!activeProfile.equals("prod")) {
                return;
            }


            OrderReturnService orderReturnService = SpringUtil.getBean(OrderReturnService.class);
            ConsumeReturnService consumeReturnService = SpringUtil.getBean(ConsumeReturnService.class);
            ConfigBaseService configBaseService = SpringUtil.getBean(ConfigBaseService.class);

            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1004);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = com.wechuang.mallshop.common.utils.SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            List<String> returnIds = orderReturnService.getOnlineRefundReturnIds();

            if (CollectionUtil.isNotEmpty(returnIds)) {
                for (String returnId : returnIds) {
                    consumeReturnService.doOnLineRefund(returnId);

                    //清理缓存
                    configBaseService.cleanRequestCache();
                }
            }
        } finally {
            // 清除MDC信息
            MdcTraceFilter.clearMdc();
        }
    }
}
