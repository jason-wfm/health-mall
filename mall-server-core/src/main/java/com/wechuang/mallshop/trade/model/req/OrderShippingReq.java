package com.wechuang.mallshop.trade.model.req;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "订单发货审核接口")
public class OrderShippingReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @NotBlank(message = "请输入订单编号")
    private String orderId;

    @Schema(description = "出库单编号")
    private String stockBillId;

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

    @Schema(description = "发货标记:true-默认全发， false-指定发货")
    private Boolean shippingFlag;
}