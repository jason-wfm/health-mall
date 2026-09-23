package com.wechuang.mallshop.pay.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "微信支付回调响应")
public class PaymentWechatNotifyRes {

    @Schema(description = "微信V3通知响应")
    private String v3NotifyRsp;

    // Getters and setters
}