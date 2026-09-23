package com.wechuang.mallshop.pt.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductBase;
import com.wechuang.mallshop.pt.model.entity.ProductInfo;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductBaseAndInfoOutput {

    private ProductBase productBase;

    private ProductInfo productInfo;

}
