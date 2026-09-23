package com.wechuang.mallshop.trade.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "订单数量")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderNumOutput {

    @Schema(description = "完成订单数")
    private Long finNum = 0L;

    @Schema(description = "完成订单数-实物")
    private Long finNumEntity = 0L;

    @Schema(description = "完成订单数-虚拟")
    private Long finNumV = 0L;


    @Schema(description = "取消订单数")
    private Long cancelNum = 0L;

    @Schema(description = "取消订单数-实物")
    private Long cancelNumEntity = 0L;

    @Schema(description = "取消订单数-虚拟")
    private Long cancelNumV = 0L;

    @Schema(description = "待发货货订单数")
    private Long waitShippingNum = 0L;

    @Schema(description = "待发货货订单数-实物")
    private Long waitShippingNumEntity = 0L;

    @Schema(description = "待发货货订单数-虚拟")
    private Long waitShippingNumV = 0L;

    @Schema(description = "已发货订单数")
    private Long shipNum = 0L;

    @Schema(description = "已发货订单数-实物")
    private Long shipNumEntity = 0L;

    @Schema(description = "已发货订单数-虚拟")
    private Long shipNumV = 0L;

    @Schema(description = "等待支付订单数")
    private Long waitPayNum = 0L;

    @Schema(description = "等待支付订单数-实物")
    private Long waitPayNumEntity = 0L;

    @Schema(description = "等待支付订单数-虚拟")
    private Long waitPayNumV = 0L;


    @Schema(description = "售后订单数")
    private Long returningNum = 0L;
}
