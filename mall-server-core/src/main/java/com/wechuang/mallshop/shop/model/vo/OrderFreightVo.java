package com.wechuang.mallshop.shop.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderFreightVo implements Serializable {
    @Schema(description = "是否可配送")
    private Boolean canDelivery;

    @Schema(description = "免运费额度")
    private BigDecimal freightFreeMin;

    @Schema(description = "运费")
    private BigDecimal freight = BigDecimal.ZERO;

}
