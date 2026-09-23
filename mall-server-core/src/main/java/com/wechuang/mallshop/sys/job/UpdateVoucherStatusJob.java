package com.wechuang.mallshop.sys.job;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.repository.UserVoucherRepository;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UpdateVoucherStatusJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("UpdateVoucherStatusJob");

        try {
            Logger logger = LoggerFactory.getLogger(UpdateVoucherStatusJob.class);
            UserVoucherRepository userVoucherRepository = SpringUtil.getBean(UserVoucherRepository.class);
            ConfigBaseService configBaseService = SpringUtil.getBean(ConfigBaseService.class);

            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1005);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            QueryWrapper<UserVoucher> voucherQueryWrapper = new QueryWrapper<>();
            voucherQueryWrapper.eq("voucher_state_id", StateCode.VOUCHER_STATE_UNUSED)
                    .lt("voucher_end_date", new Date().getTime());
            List<Serializable> userVoucherIds = userVoucherRepository.findKey(voucherQueryWrapper);

            if (CollectionUtil.isNotEmpty(userVoucherIds)) {
                UserVoucher voucher = new UserVoucher();
                voucher.setVoucherStateId(StateCode.VOUCHER_STATE_TIMEOUT);

                QueryWrapper<UserVoucher> userVoucherQueryWrapper = new QueryWrapper<>();
                userVoucherQueryWrapper.in("user_voucher_id", userVoucherIds);

                if (!userVoucherRepository.edit(voucher, userVoucherQueryWrapper)) {
                    logger.error(String.format("voucher_ids : %s 失效出错", userVoucherIds));
                }

                //清理缓存
                configBaseService.cleanRequestCache();
            }
        } finally {
            // 清除MDC信息
            MdcTraceFilter.clearMdc();
        }
    }
}