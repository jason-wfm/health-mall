package com.wechuang.mallshop.pay.model.vo;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UnionPayVo {
    @Schema(description = "商户号")
    private String machId;

    @Schema(description = "密钥")
    private String key;

    @Schema(description = "serverUrl")
    private String serverUrl = "https://qra.95516.com/pay/gateway";
    ;

    @Schema(description = "域名外网访问项目的域名，支付通知中会使用")
    private String domain = ConstantConfig.URL_BASE;

    @Schema(description = "回调地址")
    private String notifyUrl = ConstantConfig.URL_BASE + "/front/pay/callback/unionpayNotify";
    ;

}
