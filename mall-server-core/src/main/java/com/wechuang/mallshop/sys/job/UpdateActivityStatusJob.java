package com.wechuang.mallshop.sys.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class UpdateActivityStatusJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("UpdateActivityStatusJob");

        try {
            ActivityBaseRepository activityBaseRepository = SpringUtil.getBean(ActivityBaseRepository.class);
            ActivityBaseService activityBaseService = SpringUtil.getBean(ActivityBaseService.class);
            ConfigBaseService configBaseService = SpringUtil.getBean(ConfigBaseService.class);

            long time = new Date().getTime();

            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1002);
            crontabBase.setCrontabLastExeTime(time);
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            try {
                // 更新活动状态
                QueryWrapper<ActivityBase> baseQueryWrapper = new QueryWrapper<>();
                baseQueryWrapper.eq("activity_state", StateCode.ACTIVITY_STATE_WAITING).lt("activity_starttime", time).ge("activity_endtime", time);
                List<ActivityBase> activityBases = activityBaseRepository.lists(baseQueryWrapper, 1, 20).getRecords();

                if (CollUtil.isNotEmpty(activityBases)) {
                    for (ActivityBase activityBase : activityBases) {
                        activityBase.setActivityState(StateCode.ACTIVITY_STATE_NORMAL);
                        activityBaseService.editActivityBase(activityBase.getActivityId(), activityBase);

                        //清理缓存
                        configBaseService.cleanRequestCache();
                    }
                }

                QueryWrapper<ActivityBase> endQueryWrapper = new QueryWrapper<>();
                endQueryWrapper.eq("activity_state", StateCode.ACTIVITY_STATE_NORMAL).le("activity_endtime", time);
                List<ActivityBase> endActivityBases = activityBaseRepository.lists(endQueryWrapper, 1, 20).getRecords();

                if (CollUtil.isNotEmpty(endActivityBases)) {
                    for (ActivityBase activityBase : endActivityBases) {
                        activityBase.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
                        activityBaseService.editActivityBase(activityBase.getActivityId(), activityBase);

                        //清理缓存
                        configBaseService.cleanRequestCache();
                    }
                }

            } catch (Exception e) {
                LogUtil.error(ConstantLog.TASK, e);
            }
        } finally {
            // 清除MDC信息
            MdcTraceFilter.clearMdc();
        }
    }
}