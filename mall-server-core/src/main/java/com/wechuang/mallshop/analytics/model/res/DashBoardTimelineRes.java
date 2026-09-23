package com.wechuang.mallshop.analytics.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "仪表板柱形图对象", description = "[订单数据，用户数据，商品数据，销售额数据]")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DashBoardTimelineRes {

    @Schema(description = "最近一周订单增长数据")
    private List<TimelineOutput> orderTimeLine;

    @Schema(description = "最近一周订单用户数据")
    private List<TimelineOutput> userTimeLine;

    @Schema(description = "最近一周订单商品数据")
    private List<TimelineOutput> ptTimeLine;

    @Schema(description = "最近一周销售额增长数据")
    private List<TimelineOutput> payTimeLine;

}
