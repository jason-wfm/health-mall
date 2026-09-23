package com.wechuang.mallshop.pt.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ISpecVo {
    @Schema(description = "规格编号")
    private Integer id;

    @Schema(description = "规格名称")
    private String name;

    @Schema(description = "商品主图")
    private Integer specFormat;

    @Schema(description = "规格值")
    private ISpecItemVo item;

}
