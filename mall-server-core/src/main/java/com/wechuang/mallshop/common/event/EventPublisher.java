package com.wechuang.mallshop.common.event;

import cn.hutool.core.util.IdUtil;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.consts.ConstantEvent;
import com.wechuang.mallshop.common.consts.ConstantMq;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.sys.model.entity.MqMessage;
import com.wechuang.mallshop.sys.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * [healthmall-ext] 领域事件发布器
 *
 * 与存量 MqMessageService.sendMqMsg 的区别：本发布器与业务事务对齐——
 * 1. 事务内调用：先落 outbox（sys_mq_message，状态 INIT），事务提交后再投递 MQ，
 *    保证"业务成功则事件必达"；业务回滚则事件不发出。
 * 2. 无事务调用：落库后立即投递。
 *
 * 可靠性边界（与存量机制一致）：
 * - 投递未确认：RabbitTemplate confirm 回调置 FAILURE，由 RetryMqMsgJob 兜底重投；
 * - 提交后、投递前进程崩溃：消息停留在 INIT，不会发出（需人工/对账介入）；
 * - 重复投递：消费端必须以 eventId 幂等。
 */
@Component
@Slf4j
public class EventPublisher {

    @Autowired
    private MqMessageService mqMessageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布领域事件
     *
     * @param eventType     事件类型（= 路由键），取 ConstantEvent 常量
     * @param aggregateType 聚合类型，如 Order
     * @param aggregateId   聚合编号
     * @param payload       业务载荷，随信封 JSON 序列化
     */
    public void publish(String eventType, String aggregateType, String aggregateId, Object payload) {
        EventEnvelope envelope = new EventEnvelope();
        envelope.setEventId(IdUtil.simpleUUID());
        envelope.setEventType(eventType);
        envelope.setAggregateType(aggregateType);
        envelope.setAggregateId(aggregateId);
        envelope.setPayload(payload);
        envelope.setOccurredAt(new java.util.Date());
        envelope.setTraceId(MDC.get("traceId"));

        String json = JSONUtil.toJSONString(envelope);

        MqMessage message = new MqMessage();
        message.setMessageId(envelope.getEventId());
        message.setMessageContent(json);
        message.setMessageToExchane(ConstantEvent.EVENT_EXCHANGE);
        message.setMessageRoutingKey(eventType);
        message.setMessageClassType(EventPublisher.class.getSimpleName());
        message.setMessageStatus(ConstantMq.INIT);

        if (!Boolean.TRUE.equals(mqMessageService.save(message))) {
            throw new BusinessException(ResultCode.FAILED);
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    doSend(envelope.getEventId(), eventType, json);
                }
            });
        } else {
            doSend(envelope.getEventId(), eventType, json);
        }
    }

    private void doSend(String messageId, String routingKey, String json) {
        try {
            rabbitTemplate.convertAndSend(ConstantEvent.EVENT_EXCHANGE, routingKey, json,
                    new CorrelationData(messageId));
        } catch (Exception e) {
            log.error("领域事件投递失败，messageId={}, eventType={}", messageId, routingKey, e);
            mqMessageService.setMessageStatus(messageId, ConstantMq.FAILURE);
        }
    }
}
