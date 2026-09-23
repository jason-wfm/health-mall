package com.wechuang.mallshop.shop.listener;

import com.rabbitmq.client.Channel;
import com.wechuang.mallshop.common.consts.ConstantMq;
import com.wechuang.mallshop.common.pojo.dto.ErrorTypeEnum;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.sys.service.MqMessageService;
import com.wechuang.mallshop.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Service
@Slf4j
//@RabbitListener(queues = ConstantMq.SHOP_PAIDYES_QUEUE, concurrency = "5-10")
public class OrderPaidListener {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MqMessageService mqMessageService;

    @RabbitHandler
    public void listener(String data, Channel channel, Message message) throws IOException, InterruptedException {
        String orderId = data;
        String messageId = message.getMessageProperties().getMessageId();

        try {
            boolean flag = orderService.setPaidYes(orderId);

            if (flag) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                mqMessageService.setMessageStatus(message.getMessageProperties().getMessageId(), ConstantMq.DELIVERED);
            } else {
                LogUtil.error(ErrorTypeEnum.ERR_ORDER_SERVICE.getValue(), String.format(__("消息消费失败，执行setPaidYes异常，当前订单编号：%s"), orderId));

                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                mqMessageService.setMessageStatus(messageId, ConstantMq.FAILURE);
            }
        } catch (Exception e) {
            LogUtil.error(ErrorTypeEnum.ERR_ORDER_SERVICE.getValue(), String.format(__("消息消费失败，执行setPaidYes异常，当前订单编号：%s，失败原因：%s"), orderId, e.getMessage()), e);

            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            mqMessageService.setMessageStatus(messageId, ConstantMq.FAILURE);
        }
    }
}
