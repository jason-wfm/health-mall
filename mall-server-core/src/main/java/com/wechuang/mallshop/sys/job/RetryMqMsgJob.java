package com.wechuang.mallshop.sys.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.consts.ConstantMq;
import com.wechuang.mallshop.common.utils.SpringUtil;
import com.wechuang.mallshop.common.weblog.MdcTraceFilter;
import com.wechuang.mallshop.sys.model.entity.CrontabBase;
import com.wechuang.mallshop.sys.model.entity.MqMessage;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CrontabBaseService;
import com.wechuang.mallshop.sys.service.MqMessageService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

public class RetryMqMsgJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        MdcTraceFilter.initMdcForTask("RetryMqMsgJob");

        try {
            Logger logger = LoggerFactory.getLogger(RetryMqMsgJob.class);
            RabbitTemplate rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
            MqMessageService mqMessageService = SpringUtil.getBean(MqMessageService.class);
            ConfigBaseService configBaseService = SpringUtil.getBean(ConfigBaseService.class);

            Date now = new Date();
            CrontabBase crontabBase = new CrontabBase();
            crontabBase.setCrontabId(1009);
            crontabBase.setCrontabLastExeTime(now.getTime());
            CrontabBaseService crontabBaseService = SpringUtil.getBean(CrontabBaseService.class);
            crontabBaseService.edit(crontabBase);

            QueryWrapper<MqMessage> mqMessageQueryWrapper = new QueryWrapper<>();
            mqMessageQueryWrapper.eq("message_status", ConstantMq.FAILURE);
            mqMessageQueryWrapper.lt("message_count", ConstantMq.MAX_COUNT);

            List<MqMessage> mqMessages = mqMessageService.find(mqMessageQueryWrapper);

            mqMessages.forEach(mqMessage -> {
                mqMessage.setMessageCount(mqMessage.getMessageCount() + 1);

                if (!mqMessageService.edit(mqMessage)) {
                    logger.error(__("消息消费次数增加失败!"));
                }

                // 重新投递到队列中
                rabbitTemplate.convertAndSend(mqMessage.getMessageToExchane(), mqMessage.getMessageRoutingKey(), mqMessage.getMessageContent(), new CorrelationData(mqMessage.getMessageId()));
            });

            // 清理已经消费成功的消息或早于两个月前的消息
            QueryWrapper<MqMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("message_status", ConstantMq.DELIVERED)
                    .or().lt("create_time", DateUtil.offsetMonth(new Date(), -2));
            List<MqMessage> messages = mqMessageService.lists(queryWrapper, 1, 20).getRecords();
            List<String> messageIds = messages.stream().map(MqMessage::getMessageId).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(messageIds)) {

                if (!mqMessageService.remove(messageIds)) {
                    logger.error(__("消息清理失败!"));
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
