package com.wechuang.mallshop.pay.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付方式信息")
public class PayMetVo {

    @Schema(description = "付款账户")
    private Integer paymentMetId;

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

    @Schema(description = "支付渠道")
    private Integer paymentChannelId;

    @Schema(description = "支付方式")
    private Integer paymentTypeId;

    @Schema(description = "支付用户")
    private Integer payUserId;

    @Schema(description = "支付用户昵称")
    private String payUserNickname;

    @Schema(description = "支付店铺编号")
    private Integer payStoreId;
}