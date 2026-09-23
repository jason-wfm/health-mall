package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreDeliverycreditVo implements Serializable {

    @Schema(description = "")
    private Float credit;

    @Schema(description = "")
    private String percent;

    @Schema(description = "")
    private String percent_class;

    @Schema(description = "")
    private String percent_text;

    @Schema(description = "")
    private String text;

}
