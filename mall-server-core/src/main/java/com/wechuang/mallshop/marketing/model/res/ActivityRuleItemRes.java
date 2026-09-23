package com.wechuang.mallshop.marketing.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "活动规则列表")
public class ActivityRuleItemRes implements Serializable {

    @Schema(description = "活动编号")
    private Integer activityId;

    @Schema(description = "商品编号-SKU编号")
    private Long itemId;

    @Schema(description = "SPU商品名称")
    private String productName;

    @Schema(description = "副标题(DOT):SKU名称")
    private String itemName;

    @Schema(description = "图片信息")
    private String productImage;

    @Schema(description = "可用库存")
    private Integer availableQuantity;

    @Schema(description = "商品价格")
    private BigDecimal itemUnitPrice;

    @Schema(description = "换购价")
    private BigDecimal itemReplacePrice;
}
