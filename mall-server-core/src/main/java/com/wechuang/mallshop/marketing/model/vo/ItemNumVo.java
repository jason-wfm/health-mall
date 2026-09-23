package com.wechuang.mallshop.marketing.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@Schema(name = "规则")
public class ItemNumVo {
    @Schema(description = "SKU编号")
    private Long itemId;

    @Schema(description = "活动编号")
    private Integer activityId;

    @Schema(description = "数量")
    private Integer num;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "商品换购价格")
    private BigDecimal itemReplacePrice;

    @Schema(description = "商品名称")
    private String productItemName;

}