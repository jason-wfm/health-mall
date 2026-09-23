package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "线下支付接口")
public class ConsumeDepositOfflinePayReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @NotBlank(message = "请输入订单编号")
    private String orderId;

    @Schema(description = "支付渠道")
    private Integer paymentChannelId = 1422;

    @Schema(description = "支付方式")
    private Integer depositPaymentType = 1305;

    @Schema(description = "交易凭证号")
    @NotBlank(message = "请输入交易凭证号")
    private String depositTradeNo;

    @Schema(description = "时间")
    private Long depositTime;

    @Schema(description = "交易金额")
    private BigDecimal depositTotalFee;

    @Schema(description = "余额")
    private BigDecimal pmMoney;

    @Schema(description = "充值卡")
    private BigDecimal pmRechargeCard;

    @Schema(description = "积分")
    private BigDecimal pmPoints;

    @Schema(description = "信用账户")
    private BigDecimal pmCredit;

    @Schema(description = "红包账户")
    private BigDecimal pmRedpack;
}
