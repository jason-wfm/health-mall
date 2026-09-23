package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductAssist;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductAssistRes {

    @Schema(description = "属性分类编号")
    private Integer assistId;

    @Schema(description = "属性名称")
    private String assistName;

    @Schema(description = "属性数据")
    private List<ProductAssist> children;

}
