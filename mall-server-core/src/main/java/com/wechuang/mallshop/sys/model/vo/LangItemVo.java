package com.wechuang.mallshop.sys.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LangItemVo {
    @Schema(description = "编码")
    private String lang;

    @Schema(description = "国际编码")
    private Integer currencyId;

    @Schema(description = "货币左符号")
    private String symbol;

    @Schema(description = "货币右符号")
    private String symbolRight;

    @Schema(description = "名称")
    private String label;

    @Schema(description = "标准模板")
    private Boolean standard;

    @Schema(description = "标准模板")
    private String img;
}
