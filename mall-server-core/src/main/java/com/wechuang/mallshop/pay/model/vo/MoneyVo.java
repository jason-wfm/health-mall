package com.wechuang.mallshop.pay.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MoneyVo implements Serializable {

    @Schema(description = "所属用户")
    private Integer userId;

    @Schema(description = "余额")
    private BigDecimal recordTotal;

    @Schema(description = "交易类型")
    private Integer tradeTypeDeposit;

    @Schema(description = "描述")
    private String recordDesc;

    @Schema(description = "支付方式")
    private Integer paymentTypeId;

    @Schema(description = "佣金")
    private BigDecimal recordCommissionFee;

    @Schema(description = "订单编号")
    private String orderId;
}
