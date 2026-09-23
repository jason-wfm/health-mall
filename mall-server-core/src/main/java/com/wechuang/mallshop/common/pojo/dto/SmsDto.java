package com.wechuang.mallshop.common.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "短信发送DTO", description = "短信发送DTO")
public class SmsDto {

    @Schema(description = "短信类型")
    private Integer smsType;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "短信内容")
    private String content;

    @Schema(description = "模板编号-扩展使用")
    private String tplId;

    @Schema(description = "签名通道号-扩展使用")
    private String messageTplSender;

    @Schema(description = "动态参数-扩展使用")
    private Map tplParas;

    @Schema(description = "账号/平台id")
    private String serviceUserId;

    @Schema(description = "短信KEY/平台KEY")
    private String serviceAppKey;

    @Schema(description = "阿里云模板code")
    private String templateCode;

    @Schema(description = "阿里云短信参数")
    private Map<String, Object> paramMap;

    @Schema(description = "腾讯云模板id")
    private Integer tengxunTemplateId;

}