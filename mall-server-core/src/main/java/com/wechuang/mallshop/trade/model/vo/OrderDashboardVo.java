package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商家统计信息")
public class OrderDashboardVo implements Serializable {


    @Schema(description = "昨日订单数")
    private Long yesterdayNum;

    @Schema(description = "当月订单数")
    private Long monthNum;

    @Schema(description = "成交总金额")
    private BigDecimal payAmount;

    @Schema(description = "订单总数")
    private Long totalNum;

    @Schema(description = "等待付款")
    private Long waitPayNum;

    @Schema(description = "已完成")
    private Long finNum;

    @Schema(description = "待评价")
    private Long evaNum;

    @Schema(description = "已发货")
    private Long shipNum;

    @Schema(description = "待发货")
    private Long waitShippingNum;

}
