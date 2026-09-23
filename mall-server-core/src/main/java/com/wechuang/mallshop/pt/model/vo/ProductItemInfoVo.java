package com.wechuang.mallshop.pt.model.vo;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "满即送", description = "满即送")
public class ProductItemInfoVo implements Serializable {

    @Schema(description = "商品编号-SKU编号")
    private Long itemId;

    @Schema(description = "商品销售价")
    private BigDecimal itemSalePrice;

    @Schema(description = "商品价格")
    private BigDecimal itemUnitPrice;

    @Schema(description = "产品编号")
    private Long productId;

    @Schema(description = "图片信息")
    private String productImage;

    @Schema(description = "Spec名称")
    private String productItemName;

    @Schema(description = "SPU商品名称")
    private String productName;

    @Schema(description = "运费模板")
    private Integer transportTypeId;

    @Schema(description = "商品卖点:商品广告词")
    private String productTips;

    @Schema(description = "商品数量")
    private Integer cartQuantity;
}
