package com.wechuang.mallshop.sys.job;

import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 开源版已下线拼团营销，此定时任务保留类名以兼容 Quartz 配置，不再执行业务逻辑。
 */
public class CheckGroupBookingJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("CheckGroupBookingJob");
        try {
            // no-op
        } finally {
            MdcTraceFilter.clearMdc();
        }
    }
}
