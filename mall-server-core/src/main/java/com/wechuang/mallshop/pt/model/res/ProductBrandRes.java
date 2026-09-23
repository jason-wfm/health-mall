package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductBrand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductBrandRes {

    @Schema(description = "品牌分类编号")
    private Integer brandId;

    @Schema(description = "品牌名称")
    private String brandName;

    @Schema(description = "品牌数据")
    private List<ProductBrand> children;

}
