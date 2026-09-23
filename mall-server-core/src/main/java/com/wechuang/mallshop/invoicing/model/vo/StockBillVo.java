package com.wechuang.mallshop.invoicing.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.invoicing.model.entity.StockBill;
import com.wechuang.mallshop.invoicing.model.entity.StockBillItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StockBillVo extends StockBill {
    @Schema(description = "出库订单SKU")
    private List<StockBillItem> items;
}
