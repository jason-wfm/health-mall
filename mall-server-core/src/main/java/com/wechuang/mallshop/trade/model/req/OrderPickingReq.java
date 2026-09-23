package com.wechuang.mallshop.trade.model.req;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "订单出库审核接口")
public class OrderPickingReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @NotBlank(message = "请输入订单编号")
    private String orderId;

    @Schema(description = "出库商品信息")
    private String items;

    @Schema(description = "业务类型")
    private Integer billTypeId;

    @Schema(description = "地址编号")
    private Integer warehouseId;

    @Schema(description = "库存类型")
    private Integer stockTransportTypeId;

    @Schema(description = "出库标记:true-默认全出， false-指定出库")
    private Boolean pickingFlag = true;
}