package com.wechuang.mallshop.core.web.model;

import lombok.Data;

import java.util.Map;

/**
 * 短信 DTO（core 侧，自研重写，对齐 core-3.0.27908；
 * 注意：业务侧更常用的短信模型是 common/pojo/dto/SmsDto，勿混淆）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class SmsDto {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 模板编号
     */
    private String tplId;

    /**
     * 模板参数
     */
    private Map tplParas;

    /**
     * 直接发送内容（无模板时）
     */
    private String content;

    /**
     * 云服务用户编号
     */
    private String serviceUserId;

    /**
     * 云服务应用密钥
     */
    private String serviceAppKey;

    /**
     * 模板发送方
     */
    private String messageTplSender;
}
