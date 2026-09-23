package com.wechuang.mallshop.pt.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDataOutput {
    @Schema(description = "商品基础信息")
    private ProductBase productBase;

    @Schema(description = "商品索引")
    private ProductIndex productIndex;

    @Schema(description = "商品信息")
    private ProductInfo productInfo;

    @Schema(description = "商品SKU")
    private List<ProductItem> productItem;

    @Schema(description = "商品图片表")
    private List<ProductImage> productImage;

    @Schema(description = "虚拟商品表")
    private ProductValidPeriod productValidPeriod;

    @Schema(description = "产品图片")
    private Map<Long, ProductImage> specImg;

}
