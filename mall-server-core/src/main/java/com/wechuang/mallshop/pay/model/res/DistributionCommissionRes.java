package com.wechuang.mallshop.pay.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分销佣金汇总（开源裁剪版：无分销表，接口保留，数值恒为 0）。
 */
@Data
@Schema(name = "分销佣金汇总")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistributionCommissionRes {

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "佣金总额")
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @Schema(description = "已结算佣金")
    private BigDecimal commissionSettled = BigDecimal.ZERO;

    @Schema(description = "一级消费佣金")
    private BigDecimal commissionBuyAmount0 = BigDecimal.ZERO;

    @Schema(description = "二级消费佣金")
    private BigDecimal commissionBuyAmount1 = BigDecimal.ZERO;

    @Schema(description = "三级消费佣金")
    private BigDecimal commissionBuyAmount2 = BigDecimal.ZERO;

    @Schema(description = "已结算消费佣金")
    private BigDecimal commissionBuySettled = BigDecimal.ZERO;

    @Schema(description = "已提现佣金")
    private BigDecimal commissionWithdrawAmount = BigDecimal.ZERO;

    @Schema(description = "退款冲回佣金")
    private BigDecimal commissionRefundAmount = BigDecimal.ZERO;

    @Schema(description = "冻结佣金")
    private BigDecimal commissionFrozenAmount = BigDecimal.ZERO;

    @Schema(description = "消费佣金合计（多级之和）")
    private BigDecimal commissionBuyAmount = BigDecimal.ZERO;

    @Schema(description = "未结算消费佣金")
    private BigDecimal userCommissionBuy = BigDecimal.ZERO;

    @Schema(description = "当前可提现佣金")
    private BigDecimal userCommissionNow = BigDecimal.ZERO;

    @Schema(description = "本月预估消费佣金")
    private BigDecimal monthCommissionBuy = BigDecimal.ZERO;

    public static DistributionCommissionRes empty(Integer userId) {
        DistributionCommissionRes res = new DistributionCommissionRes();
        res.setUserId(userId);
        return res;
    }
}
