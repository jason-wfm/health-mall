package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "退货申请对象", description = "退货申请")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderReturnItemInputVo {
    @Schema(description = "订单商品表编号")
    private Long orderItemId;

    @Schema(description = "退款金额")
    private BigDecimal returnRefundAmount;

    @Schema(description = "退货商品数量")
    private Integer returnItemNum;
}
