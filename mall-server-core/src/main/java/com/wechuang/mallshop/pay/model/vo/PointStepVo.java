package com.wechuang.mallshop.pay.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PointStepVo {

    @Schema(description = "时间")
    private String times;

    @Schema(description = "天数")
    private Integer days;

    @Schema(description = "倍数")
    private String multiples;

    @Schema(description = "前端映射 天数或倍数")
    private String valueStr;

}
