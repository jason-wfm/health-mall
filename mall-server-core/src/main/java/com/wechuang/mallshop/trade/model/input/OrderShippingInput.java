package com.wechuang.mallshop.trade.model.input;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Schema(name = "OrderShippingInput对象", description = "OrderShippingInput对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderShippingInput implements Serializable {

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "出库单编号")
    private Integer stockBillId;

    @Schema(description = "发货地址")
    private Integer ssId;

    @Schema(description = "发货物流编号")
    private Integer logisticsId;

    @Schema(description = "发货时间")
    private Long logisticsTime;

    @Schema(description = "发货类型(ENUM): 1: 手动填写; 2: 电子面单打印")
    private Integer shippingType;

    @Schema(description = "运单号")
    private String orderTrackingNumber;

    @Schema(description = "备注")
    private String logisticsExplain;

    @Schema(description = "发货标记(BOOL):true-默认全发， false-指定发货")
    private Boolean shippingFlag;

}
