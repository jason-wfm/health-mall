package com.wechuang.mallshop.marketing.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.entity.ActivityItem;
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
@Schema(name = "活动商品列表")
public class ActivityItemRes extends ActivityItem {

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "商品价格")
    private BigDecimal itemUnitPrice;

    @Schema(description = "商品主图")
    private String productImage;

    @Schema(description = "销售中")
    private Boolean isOnSale = true;

    @Schema(description = "副标题(DOT):SKU名称")
    private String itemName;

    @Schema(description = "是否启用(LIST):1001-正常;1002-下架仓库中;1000-违规禁售")
    private Integer itemEnable;

    @Schema(description = "可用库存")
    private Integer availableQuantity;
}
