package com.wechuang.mallshop.trade.model.input;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.vo.PickingItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "OrderPickingInput对象", description = "OrderPickingInput对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderPickingInput implements Serializable {


    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "出库商品信息")
    private List<PickingItem> items;

    @Schema(description = "单据金额")
    private BigDecimal stockBillAmount;

    @Schema(description = "业务类型")
    private Integer billTypeId;

    @Schema(description = "地址编号")
    private Integer warehouseId;

    @Schema(description = "库存类型")
    private Integer stockTransportTypeId;

    @Schema(description = "出库标记(BOOL):true-默认全出， false-指定出库")
    private Boolean pickingFlag = true;

}
