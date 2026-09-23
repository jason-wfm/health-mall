package com.wechuang.mallshop.sys.job;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class UpdateProductStatusJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("UpdateProductStatusJob");

        try {
            ProductIndexService productIndexService = SpringUtil.getBean(ProductIndexService.class);
            ConfigBaseService configBaseService = SpringUtil.getBean(ConfigBaseService.class);

            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1003);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = com.wechuang.mallshop.common.utils.SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            try {
                //自动上架
                productIndexService.autoSaleProduct();
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TASK, e);
            }

            //清理缓存
            configBaseService.cleanRequestCache();
        } finally {
            // 清除MDC信息
            MdcTraceFilter.clearMdc();
        }
    }
}
