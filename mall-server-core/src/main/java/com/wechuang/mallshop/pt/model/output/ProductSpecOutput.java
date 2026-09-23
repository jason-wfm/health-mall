package com.wechuang.mallshop.pt.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductSpec;
import com.wechuang.mallshop.pt.model.entity.ProductSpecItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductSpecOutput extends ProductSpec {

    private List<ProductSpecItem> items;

}
