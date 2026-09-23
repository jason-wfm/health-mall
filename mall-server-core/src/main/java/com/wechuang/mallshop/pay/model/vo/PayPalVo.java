package com.wechuang.mallshop.pay.model.vo;

import com.ijpay.paypal.PayPalApiConfig;
import com.ijpay.paypal.PayPalApiConfigKit;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PayPalVo {
    @Schema(description = "clientId")
    private String clientId;

    @Schema(description = "私钥")
    private String secret;

    @Schema(description = "sandBox")
    private Boolean sandBox = false;

    @Schema(description = "域名")
    private String domain = ConstantConfig.URL_BASE;

    @Schema(description = "回调地址")
    private String notifyUrl = ConstantConfig.URL_BASE + "/front/pay/callback/paypalNotify";
    private String notifyCertUrl = ConstantConfig.URL_BASE + "/front/pay/callback/paypalCertNotify";

    @Schema(description = "返回地址")
    private String returnUrl = ConstantConfig.URL_BASE + "/front/pay/callback/paypalReturn";
    private String returnCertUrl = ConstantConfig.URL_BASE + "/front/pay/callback/paypalCertReturn";

    @Schema(description = "取消地址")
    private String cancelUrl = ConstantConfig.URL_BASE + "/front/pay/callback/paypalCancel";


    public PayPalApiConfig getConfig() {
        PayPalApiConfig config = new PayPalApiConfig();
        config.setClientId(getClientId());
        config.setSecret(getSecret());
        config.setSandBox(getSandBox());
        config.setDomain(getDomain());
        PayPalApiConfigKit.setThreadLocalApiConfig(config);
        return config;
    }
}
