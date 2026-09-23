package com.wechuang.mallshop.analytics.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AnalyticsNumVo {

    @Schema(description = "本周期")
    private Object current = 0;

    @Schema(description = "上个周期")
    private Object pre = 0;

    @Schema(description = "周期环比")
    private Object daym2m = null;
}
