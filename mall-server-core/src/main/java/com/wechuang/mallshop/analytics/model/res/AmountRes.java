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
@Schema(name = "订单销售金额对比图对象")
public class AmountRes {

    @Schema(description = "时间")
    private String time;

    @Schema(description = "金额")
    private Integer amount;

}
