package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductBrand;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import com.wechuang.mallshop.sys.model.entity.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductCategoryFilterRes implements Serializable {
    @Schema(description = "分类辅助属性")
    List<ProductAssistOutput> assists = new ArrayList<>();

    @Schema(description = "服务")
    List<ContractType> contracts = new ArrayList<>();

    @Schema(description = "商圈")
    List<Object> markets = new ArrayList<>();

    @Schema(description = "下级分类")
    private List<ProductCategory> children = new ArrayList<>();

    @Schema(description = "上级分类")
    private List<ProductCategory> parent = new ArrayList<>();

    @Schema(description = "品牌")
    private List<ProductBrand> brands = new ArrayList<>();

    @Schema(description = "信息")
    private ProductCategory info;
}
