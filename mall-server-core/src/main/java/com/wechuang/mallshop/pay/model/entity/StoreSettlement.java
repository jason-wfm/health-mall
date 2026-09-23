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
import java.util.Date;

/**
 * <p>
 * 商户结算单
 * </p>
 *
 * @author jason
 * @since 2026-09-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pay_store_settlement")
@Schema(name = "StoreSettlement对象", description = "商户结算单")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreSettlement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "结算单编号")
    @TableId(value = "settlement_id", type = IdType.AUTO)
    private Long settlementId;

    @Schema(description = "结算单号(JS前缀,NumberSeq生成)")
    @TableField("settlement_number")
    private String settlementNumber;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "账期起(按订单完成时间口径)")
    @TableField("period_start")
    private Date periodStart;

    @Schema(description = "账期止")
    @TableField("period_end")
    private Date periodEnd;

    @Schema(description = "订单数")
    @TableField("order_count")
    private Integer orderCount;

    @Schema(description = "订单实付合计")
    @TableField("order_amount")
    private BigDecimal orderAmount;

    @Schema(description = "平台佣金合计")
    @TableField("commission_amount")
    private BigDecimal commissionAmount;

    @Schema(description = "售后退款合计")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    @Schema(description = "调整金额(违约金/赔付,人工录入)")
    @TableField("adjust_amount")
    private BigDecimal adjustAmount;

    @Schema(description = "应结金额=实付-佣金-退款+调整")
    @TableField("settle_amount")
    private BigDecimal settleAmount;

    @Schema(description = "结算状态:0-待商家确认;1-已确认待出金;2-出金中;3-已完成;4-已驳回(订单回滚)")
    @TableField("settlement_state")
    private Integer settlementState;

    @Schema(description = "商家管理员编号(收款人)")
    @TableField("seller_user_id")
    private Integer sellerUserId;

    @Schema(description = "商家确认时间(毫秒时间戳)")
    @TableField("confirm_time")
    private Long confirmTime;

    @Schema(description = "关联提现单pay_consume_withdraw")
    @TableField("withdraw_id")
    private Long withdrawId;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
