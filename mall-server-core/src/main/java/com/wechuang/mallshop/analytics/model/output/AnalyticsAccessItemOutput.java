package com.wechuang.mallshop.analytics.model.output;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Schema(name = "商品浏览统计", description = "商品浏览统计")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnalyticsAccessItemOutput {
    @Schema(description = "产品编号")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "商品名称")
    @TableField("product_name")
    private String productName;

    @Schema(description = "货品编号")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "商品名称")
    @TableField("item_name")
    private String itemName;

    @Schema(description = "商品价格")
    @TableField("item_unit_price")
    private BigDecimal itemUnitPrice;

    @Schema(description = "浏览量")
    @TableField("num")
    private Long num;
}

