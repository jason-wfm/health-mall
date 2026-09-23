package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductSpecRes {

    @Schema(description = "规格分类编号")
    private Integer specId;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "规格数据")
    private List<ProductSpec> children;

}
