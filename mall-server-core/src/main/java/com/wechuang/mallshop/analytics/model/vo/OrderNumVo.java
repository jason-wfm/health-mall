package com.wechuang.mallshop.analytics.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderNumVo {

    @Schema(description = "订单数量")
    private BigDecimal orderNum;
}
