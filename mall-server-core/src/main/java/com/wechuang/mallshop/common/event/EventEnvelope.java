package com.wechuang.mallshop.common.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * [healthmall-ext] 领域事件信封：所有经事件总线发布的事件统一包装
 * 消费端约定：以 eventId 做幂等键；payload 按事件类型反序列化为各领域自定义的 Payload 模型
 */
@Data
public class EventEnvelope implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件编号（全局唯一，同时作为 MQ messageId 与消费幂等键）
     */
    private String eventId;

    /**
     * 事件类型 = 路由键，如 order.paid，见 ConstantEvent
     */
    private String eventType;

    /**
     * 聚合类型，如 Order / DispatchTask
     */
    private String aggregateType;

    /**
     * 聚合编号
     */
    private String aggregateId;

    /**
     * 业务载荷（随信封一起 JSON 序列化）
     */
    private Object payload;

    /**
     * 事件发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurredAt;

    /**
     * 链路追踪编号
     */
    private String traceId;
}
