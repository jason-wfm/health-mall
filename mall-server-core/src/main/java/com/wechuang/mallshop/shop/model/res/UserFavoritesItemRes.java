package com.wechuang.mallshop.shop.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.entity.UserFavoritesItem;
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
@Schema(name = "收藏商品列表")
public class UserFavoritesItemRes extends UserFavoritesItem {

    @Schema(description = "商品SKU全名")
    private String productItemName;

    @Schema(description = "商品主图")
    private String productImage;

    @Schema(description = "商品价格")
    private BigDecimal itemUnitPrice;

}
