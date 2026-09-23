// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.sys.service.impl;

import cn.hutool.core.util.IdUtil;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.consts.ConstantMq;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.MqMessage;
import com.wechuang.mallshop.sys.model.req.MqMessageListReq;
import com.wechuang.mallshop.sys.model.vo.MqMessageVo;
import com.wechuang.mallshop.sys.repository.MqMessageRepository;
import com.wechuang.mallshop.sys.service.MqMessageService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 此方法不要加入事务，否者会有线程竞态问题
 * </p>
 *
 * @author Xinze
 * @since 2023-12-13
 */
@Service
public class MqMessageServiceImpl extends BaseServiceImpl<MqMessageRepository, MqMessage, MqMessageListReq> implements MqMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * mq发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param data       传输数据
     */
    @Override
    public void sendMqMsg(String exchange, String routingKey, Object data) {
        String uuid = IdUtil.simpleUUID();

        // 转为字符串，存数据库、发 MQ 都用这个统一版本
        String jsonString = data instanceof String ? (String) data : JSONUtil.toJSONString(data);

        // 构建消息记录
        MqMessage mqMessage = new MqMessage();
        mqMessage.setMessageId(uuid);
        mqMessage.setMessageContent(jsonString); // 保存为字符串，避免 MyBatis 插入失败
        mqMessage.setMessageToExchane(exchange);
        mqMessage.setMessageRoutingKey(routingKey);
        mqMessage.setMessageClassType(this.getClass().getSimpleName());
        mqMessage.setMessageStatus(ConstantMq.INIT);

        if (!save(mqMessage)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        // 发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, jsonString, new CorrelationData(uuid));
    }

    /**
     * mq批量发送消息
     */
    @Override
    public void sendBatchMqMsg(List<MqMessageVo> msgVos) {
        List<MqMessage> msgList = new ArrayList<>();
        for (MqMessageVo msg : msgVos) {
            MqMessage mqMessage = new MqMessage();
            String uuid = IdUtil.simpleUUID();
            mqMessage.setMessageId(uuid);
            mqMessage.setMessageContent(msg.getData().toString());
            mqMessage.setMessageToExchane(msg.getExchange());
            mqMessage.setMessageRoutingKey(msg.getRouting_key());
            mqMessage.setMessageClassType(this.getClass().getSimpleName());
            mqMessage.setMessageStatus(ConstantMq.INIT);
            msgList.add(mqMessage);
        }

        if (!repository.saves(msgList)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        for (MqMessage msgVo : msgList) {
            rabbitTemplate.convertAndSend(msgVo.getMessageToExchane(), msgVo.getMessageRoutingKey(), msgVo.getMessageContent().toString(), new CorrelationData(msgVo.getMessageId()));
        }
    }

    /**
     * 设置消息状态
     *
     * @param messageId
     * @param messageStatus
     */
    @Override
    public boolean setMessageStatus(String messageId, Integer messageStatus) {
        MqMessage mqMessage = get(messageId);

        if (mqMessage != null) {
            mqMessage.setMessageStatus(messageStatus);
            return edit(mqMessage);
        }

        return true;
    }
}
