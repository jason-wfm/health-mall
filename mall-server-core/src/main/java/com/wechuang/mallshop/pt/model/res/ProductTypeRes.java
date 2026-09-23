package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductBrand;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import com.wechuang.mallshop.pt.model.output.ProductSpecOutput;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductTypeRes {

    private List<ProductAssistOutput> assists;

    private List<ProductBrand> brands;

    private List<ProductSpecOutput> specs;

}
