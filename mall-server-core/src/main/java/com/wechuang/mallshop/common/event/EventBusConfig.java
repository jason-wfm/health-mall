package com.wechuang.mallshop.common.event;

import com.wechuang.mallshop.common.consts.ConstantEvent;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [healthmall-ext] 领域事件总线基础设施
 * 只声明交换机；队列与绑定由各领域消费者在自己的模块内声明
 * （如 dispatch 域声明 dsp.queue 并绑定 "dispatch.#"），
 * 消费写法参照 shop/listener/OrderPaidListener：手动 ack + setMessageStatus 回写 outbox。
 */
@Configuration
public class EventBusConfig {

    @Bean
    public Exchange healthmallEventExchange() {
        return new TopicExchange(ConstantEvent.EVENT_EXCHANGE, true, false);
    }
}
