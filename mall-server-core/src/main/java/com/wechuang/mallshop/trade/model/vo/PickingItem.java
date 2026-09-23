package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(name = "PickingItem对象", description = "PickingItem对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PickingItem implements Serializable {

    @Schema(description = "SKU编号")
    private Long itemId;

    @Schema(description = "订单SKU编号")
    private Long orderItemId;

    @Schema(description = "商品数量")
    private Integer billItemQuantity;

    @Schema(description = "商品单价")
    private BigDecimal billItemPrice;

    @Schema(description = "商品编号")
    private Long productId;

}