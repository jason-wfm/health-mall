package com.wechuang.mallshop.sys.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageMenuVo {

    @Schema(description = "编号")
    private Integer id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否显示")
    @JsonProperty("isShow")
    private Boolean isShow;

    @Schema(description = "分类")
    private Integer cat;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "键")
    @JsonProperty("FeatureKey")
    private String featureKey;

    @Schema(description = "访问路径")
    private String url;
}
