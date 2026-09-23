package com.wechuang.mallshop.analytics.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TimelineOutput {

    @Schema(description = "时间")
    private String time;

    @Schema(description = "数量")
    private Integer num;

}
