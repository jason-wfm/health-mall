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
@Schema(name = "仪表板看板折线图对象")
public class TimelineRes {

    @Schema(description = "时间")
    private String time;

    @Schema(description = "数量")
    private Integer num;


}
