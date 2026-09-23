package com.wechuang.mallshop.pt.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商品折扣", description = "商品折扣")
public class ProductItemLevelDiscountVo implements Serializable {

    @Schema(description = "商品编号-SKU编号")
    private Long itemId;

    @Schema(description = "商品名称")
    private String itemName;

    @Schema(description = "商品价格")
    private BigDecimal itemUnitPrice;

    @Schema(description = "商品等级折扣")
    private Map levelDiscount;
}
