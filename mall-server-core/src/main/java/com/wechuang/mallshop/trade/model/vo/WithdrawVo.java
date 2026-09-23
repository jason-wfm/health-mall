package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(name = "店铺及商品信息", description = "店铺及商品信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WithdrawVo {

    @Schema(description = "最低提现额度")
    private BigDecimal min_withdraw = BigDecimal.ZERO;

    @Schema(description = "最低提现额度")
    private BigDecimal plantform_fx_withdraw_min_amount = BigDecimal.ZERO;

    @Schema(description = "提现最低金额")
    private BigDecimal withdraw_min_amount = BigDecimal.ZERO;

    @Schema(description = "客户确认收货后N天可提现")
    private Float withdraw_received_day = 0F;

    @Schema(description = "佣金提现日期")
    private BigDecimal withdraw_monthday = BigDecimal.ZERO;

    @Schema(description = "提现佣金比例")
    private BigDecimal withdraw_fee_rate = BigDecimal.ZERO;

    @Schema(description = "总消费佣金")
    private BigDecimal commission_buy_amount = BigDecimal.ZERO;

    @Schema(description = "总消费佣金-已经结算")
    private BigDecimal user_commission_buy = BigDecimal.ZERO;

    @Schema(description = "当前可用佣金余额")
    private BigDecimal user_commission_now = BigDecimal.ZERO;

    @Schema(description = "历史总额度")
    private BigDecimal commission_amount = BigDecimal.ZERO;

}
