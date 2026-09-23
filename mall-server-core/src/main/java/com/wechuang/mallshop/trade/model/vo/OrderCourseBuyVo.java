package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderCourseBuyVo implements Serializable {

    @Schema(description = "是否购买")
    private Boolean isBuy = false;

    @Schema(description = "是否能学习")
    private Boolean canStudy = false;
}
