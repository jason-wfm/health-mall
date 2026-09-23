package com.wechuang.mallshop.pt.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ISpecItemVo {
    @Schema(description = "规格值编号")
    private Integer id;

    @Schema(description = "规格值名称")
    private String name;
}
