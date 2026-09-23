package com.wechuang.mallshop.pay.model.vo;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxPayV3Vo {
    @Schema(description = "appid")
    private String appId;

    @Schema(description = "密钥keyPath")
    private String keyPath;

    @Schema(description = "微信支付证书Path")
    private String certPath;

    @Schema(description = "")
    private String certP12Path;

    @Schema(description = "微信平台证书")
    private String platformCertPath;

    @Schema(description = "商户编号")
    private String mchId;

    @Schema(description = "秘钥")
    private String apiKey;

    @Schema(description = "秘钥")
    private String apiKey3;

    @Schema(description = "域名")
    private String domain = ConstantConfig.URL_BASE;

    @Schema(description = "微信支付公钥")
    private String publicKeyPath;

    @Schema(description = "微信支付公钥ID")
    private String publicKeyId;

    //不是必须，可以从证书中读取
    @Schema(description = "证书序列号")
    private String serialNo;

    @Schema(description = "回调地址")
    private String notifyUrl = ConstantConfig.URL_BASE + "/front/pay/callback/wechatNotify";
}
