package com.wechuang.mallshop.shop.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.entity.UserProductBrowse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "我的足迹列表")
public class UserProductBrowseRes extends UserProductBrowse {

    @Schema(description = "商品销售价")
    private BigDecimal itemSalePrice;

    @Schema(description = "图片信息")
    private String productImage;

    @Schema(description = "Spec名称")
    private String productItemName;

    @Schema(description = "SPU商品名称")
    private String productName;

    @Schema(description = "副标题(DOT):SKU名称")
    private String itemName;

    @Schema(description = "活动类型编号")
    private Integer activityTypeId;

    @Schema(description = "活动类型名称")
    private String activityTypeName;

}
