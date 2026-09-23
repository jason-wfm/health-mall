package com.wechuang.mallshop.sys.job;

import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 开源版已移除分销：保留类名与 Quartz 任务 id 兼容，业务为空。
 */
public class UpdateDistributionOrderJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("UpdateDistributionOrderJob");
        try {
            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1017);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);
        } finally {
            MdcTraceFilter.clearMdc();
        }
    }
}
