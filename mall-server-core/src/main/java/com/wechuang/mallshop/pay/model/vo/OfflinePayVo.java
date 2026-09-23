package com.wechuang.mallshop.pay.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "线下支付信息")
public class OfflinePayVo {

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "支付渠道")
    private Integer paymentChannelId = 26;

    @Schema(description = "交易号")
    private String depositTradeNo;

    @Schema(description = "通知时间")
    private Long depositNotifyTime;

    @Schema(description = "交易金额")
    private BigDecimal depositTotalFee;
}
