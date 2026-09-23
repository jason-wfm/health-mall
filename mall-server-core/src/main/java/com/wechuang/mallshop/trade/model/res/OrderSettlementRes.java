package com.wechuang.mallshop.trade.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "推广订单收益详情表")
public class OrderSettlementRes {

    @Schema(description = "订单分页（items 为列表，对齐 Go / 前端约定）")
    private OrderSettlementPageRes items = new OrderSettlementPageRes();

    @Schema(description = "可结算成本金额")
    private BigDecimal withdrawAmount = BigDecimal.ZERO;

    @Schema(description = "待结算订单成本总额")
    private BigDecimal settleAmount = BigDecimal.ZERO;

    @Schema(description = "待结算退款总额")
    private BigDecimal refundAmount = BigDecimal.ZERO;

    @Schema(description = "待结算订单佣金")
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @Schema(description = "退单号")
    private List<String> returnIds;

}
