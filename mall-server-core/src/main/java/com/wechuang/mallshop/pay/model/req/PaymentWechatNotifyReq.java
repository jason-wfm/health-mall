package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "微信支付回调接口")
public class PaymentWechatNotifyReq {

    @Schema(description = "微信支付回调")
    private String wechatNotify;

    // Getters and setters
}
