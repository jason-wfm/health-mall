package com.wechuang.mallshop.pay.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商户结算单明细（订单级快照）
 * </p>
 *
 * @author jason
 * @since 2026-09-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pay_store_settlement_order")
@Schema(name = "StoreSettlementOrder对象", description = "商户结算单明细")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreSettlementOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "自增编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "结算单编号")
    @TableField("settlement_id")
    private Long settlementId;

    @Schema(description = "订单编号")
    @TableField("order_id")
    private String orderId;

    @Schema(description = "订单实付金额")
    @TableField("order_payment_amount")
    private BigDecimal orderPaymentAmount;

    @Schema(description = "平台佣金")
    @TableField("order_commission_fee")
    private BigDecimal orderCommissionFee;

    @Schema(description = "该单售后退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    @Schema(description = "该单应结金额")
    @TableField("order_settle_amount")
    private BigDecimal orderSettleAmount;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
