package com.wechuang.mallshop.sys.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@Schema(name = "站点设置前端对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConfigBaseIndexRes {


    @Schema(description = "分组编号")
    private Integer configTypeId;

    @Schema(description = "分组名称")
    private String configTypeName;

    @Schema(description = "配置集合")
    private List<Map<String, Object>> items;


}
