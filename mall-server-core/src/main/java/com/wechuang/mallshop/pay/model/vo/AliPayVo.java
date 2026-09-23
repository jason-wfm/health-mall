package com.wechuang.mallshop.pay.model.vo;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AliPayVo {

    @Schema(description = "appid")
    private String appId;

    @Schema(description = "私钥")
    private String privateKey;

    @Schema(description = "公钥")
    private String publicKey;

    @Schema(description = "应用证书路径")
    private String appCertPath;

    @Schema(description = "支付宝证书路径")
    private String aliPayCertPath;

    @Schema(description = "支付宝证书根路径")
    private String aliPayRootCertPath;

    @Schema(description = "支付宝网关(固定)")
    private String serverUrl = "https://openapi.alipay.com/gateway.do";

    @Schema(description = "域名")
    private String domain = ConstantConfig.URL_BASE;

    @Schema(description = "回调地址")
    private String notifyUrl = ConstantConfig.URL_BASE + "/front/pay/callback/alipayNotify";
    private String notifyCertUrl = ConstantConfig.URL_BASE + "/front/pay/callback/alipayCertNotify";

    @Schema(description = "返回地址")
    private String returnUrl = ConstantConfig.URL_BASE + "/front/pay/callback/alipayReturn";
    private String returnCertUrl = ConstantConfig.URL_BASE + "/front/pay/callback/alipayCertReturn";
}
