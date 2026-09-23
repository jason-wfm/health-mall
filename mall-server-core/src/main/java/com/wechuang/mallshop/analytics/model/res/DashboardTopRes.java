package com.wechuang.mallshop.analytics.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DashboardTopRes {

    @Schema(description = "今日")
    private Object today = 0;

    @Schema(description = "昨日")
    private Object yestoday = 0;

    @Schema(description = "日环比")
    private Object daym2m = null;

    @Schema(description = "本月")
    private Object month = 0;

}
