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
@Schema(name = "仪表板看板对象")
public class AdminDashBoardRes {

    @Schema(description = "总交易额")
    private Long tradeAmount;

    @Schema(description = "总交易额增长率")
    private Double tradeAmountIncreaseRate;

    @Schema(description = "总成交")
    private Long orderFinishNum;

    @Schema(description = "总成交增长率")
    private Double orderFinishNumIncreaseRate;

    @Schema(description = "会员总数")
    private Long userCertificationNum;

    @Schema(description = "会员总数增长率")
    private Double userCertificationNumIncreaseRate;

    @Schema(description = "订单总量")
    private Long orderNum;

    @Schema(description = "订单总量增长率")
    private Double orderNumIncreaseRate;

}
