package com.wechuang.mallshop.sys.job;

import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.pay.service.StoreSettlementService;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 商户结算单周期生成任务（sys_crontab_base: crontab_id=1002, 每周一 02:00）
 * 平台态运行(无登录上下文,租户拦截器全跳过),可全量扫描结算池
 */
public class SettlementGenerateJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("SettlementGenerateJob");

        try {
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1002);
            crontabBase.setCrontabLastExeTime(new Date().getTime());
            crontabBaseService.edit(crontabBase);

            StoreSettlementService settlementService = SpringUtil.getBean(StoreSettlementService.class);
            settlementService.generateSettlements();
        } finally {
            MdcTraceFilter.clearMdc();
        }
    }
}
