package com.wechuang.mallshop.common.config;

import com.wechuang.mallshop.common.consts.ConstantMq;
import com.wechuang.mallshop.common.pojo.dto.ErrorTypeEnum;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.sys.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Configuration
@Slf4j
public class RabbitMqConfig {

    @Autowired
    private MqMessageService mqMessageService;

    /**
     * 序列化
     */
    /*
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 消息抵达确认通知
        rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
            @Override
            public void confirm(CorrelationData data, boolean ack, String cause) {
                if (ack) {
                    String msgId = data != null ? data.getId() : null;
                    //mqMessageService.setMessageStatus(msgId, MqConstant.DELIVERED);
                } else {
                    LogUtil.error(ErrorTypeEnum.ERR_ORDER_SERVICE.getValue(),
                            String.format(__("消息未能发送成功，消息编号：{}，失败原因：{}"),
                                    data != null ? data.getId() : "null", cause));

                    String msgId = data != null ? data.getId() : null;
                    mqMessageService.setMessageStatus(msgId, ConstantMq.FAILURE);
                }
            }
        });

        // 消息投递失败通知
        rabbitTemplate.setReturnsCallback(new ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                LogUtil.error(ErrorTypeEnum.ERR_ORDER_SERVICE.getValue(),
                        String.format(__("交换机抵达队列失败，消息编号：{}，状态码：{}，失败原因：{}，当前交换机：{}，当前路由键：{}"),
                                returned.getMessage(),
                                returned.getReplyCode(),
                                returned.getReplyText(),
                                returned.getExchange(),
                                returned.getRoutingKey()));

                String msgId = returned.getMessage().getMessageProperties().getMessageId();
                mqMessageService.setMessageStatus(msgId, ConstantMq.FAILURE);
            }
        });

        // 配置序列化配置
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
    */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 消息抵达确认通知
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            if (ack) {
                String msgId = data != null ? data.getId() : null;
                //mqMessageService.setMessageStatus(msgId, MqConstant.DELIVERED);
            } else {
                LogUtil.error(ErrorTypeEnum.ERR_ORDER_SERVICE.getValue(),
                        String.format(__("消息未能发送成功，消息编号：{}，失败原因：{}"),
                                data != null ? data.getId() : "null", cause));

                String msgId = data != null ? data.getId() : null;
                mqMessageService.setMessageStatus(msgId, ConstantMq.FAILURE);
            }
        });

        // 消息投递失败通知
        rabbitTemplate.setReturnsCallback(returned -> {
            LogUtil.error(ErrorTypeEnum.ERR_ORDER_SERVICE.getValue(),
                    String.format(__("交换机抵达队列失败，消息编号：{}，状态码：{}，失败原因：{}，当前交换机：{}，当前路由键：{}"),
                            returned.getMessage(),
                            returned.getReplyCode(),
                            returned.getReplyText(),
                            returned.getExchange(),
                            returned.getRoutingKey()));

            String msgId = returned.getMessage().getMessageProperties().getMessageId();
            mqMessageService.setMessageStatus(msgId, ConstantMq.FAILURE);
        });

        // 配置序列化配置
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    /**
     * 反序列化
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    /**
     * 默认交换机
     */
    @Bean
    public Exchange eventExchange() {
        return new TopicExchange(ConstantMq.SHOP_EXCHANGE, true, false);
    }

    /**
     * 处理支付回调队列配置
     */
    @Bean
    public Queue paidYesQueue() {
        return new Queue(ConstantMq.SHOP_PAIDYES_QUEUE, true, false, false);
    }

    /**
     * 处理支付回调路由键配置
     */
    @Bean
    public Binding paidYesBinding() {
        return new Binding(ConstantMq.SHOP_PAIDYES_QUEUE,
                Binding.DestinationType.QUEUE,
                ConstantMq.SHOP_EXCHANGE,
                ConstantMq.SHOP_PAIDYES_ROUTING_KEY,
                null);
    }

    /**
     * 站内信队列配置
     */
    @Bean
    public Queue msgQueue() {
        return new Queue(ConstantMq.SHOP_MSG_QUEUE, true, false, false);
    }

    /**
     * 站内信路由键配置
     */
    @Bean
    public Binding msgBinding() {
        return new Binding(ConstantMq.SHOP_MSG_QUEUE,
                Binding.DestinationType.QUEUE,
                ConstantMq.SHOP_EXCHANGE,
                ConstantMq.SHOP_MSG_ROUTING_KEY,
                null);
    }

    @Bean
    public Queue plusOpenVoucherQueue() {
        Queue queue = new Queue(ConstantMq.PLUS_OPEN_VOUCHER_QUEUE, true, false, false);
        return queue;
    }

    @Bean
    public Binding plusOpenVoucherBinding() {
        return new Binding(ConstantMq.PLUS_OPEN_VOUCHER_QUEUE,
                Binding.DestinationType.QUEUE,
                ConstantMq.SHOP_EXCHANGE,
                ConstantMq.PLUS_OPEN_VOUCHER_ROUTING_KEY,
                null);
    }

}