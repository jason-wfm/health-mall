package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.common.api.StateCode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付接口")
public class PaymentReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号(DOT)")
    @NotEmpty(message = "请输入订单编号")
    private String orderId;

    @Schema(description = "支付渠道")
    private Integer paymentChannelId = StateCode.PAYMENT_CHANNEL_MONEY;

    @Schema(description = "openid")
    private String openid;

    @Schema(description = "支付方式(ENUM):1301-货到付款; 1302-在线支付; 1303-白条支付; 1304-现金支付; 1305-线下支付;")
    private Integer depositPaymentType = 1302;

    @Schema(description = "支付密码")
    private String password;

    @Schema(description = "余额支付")
    private BigDecimal pmMoney;

    @Schema(description = "充值卡支付")
    private BigDecimal pmRechargeCard;

    @Schema(description = "积分支付")
    private BigDecimal pmPoints;

    @Schema(description = "积分支付")
    private BigDecimal pmCredit;

    @Schema(description = "支付流水号")
    private String depositTradeNo;

    @Schema(description = "支付凭证")
    private String depositPaymentVoucher;
}
