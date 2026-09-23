package com.wechuang.mallshop.trade.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.entity.BatchOrder;
import com.wechuang.mallshop.trade.model.entity.OrderInvoice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "批量下单")
public class BatchOrderRes extends BatchOrder {

    @Schema(description = "SPU商品名称")
    private String productName;

    @Schema(description = "SKU商品名称")
    private String itemName;

    @Schema(description = "图片信息")
    private String productImage;

    @Schema(description = "商品价格")
    private BigDecimal itemUnitPrice;

}