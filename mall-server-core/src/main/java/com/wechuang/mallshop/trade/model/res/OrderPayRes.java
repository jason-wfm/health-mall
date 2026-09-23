package com.wechuang.mallshop.trade.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.trade.model.entity.OrderDeliveryAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "订单支付列表")
public class OrderPayRes implements Serializable {

    @Schema(description = "退货商品总数量")
    private List<ConsumeTrade> consumeTrades;

    @Schema(description = "总付款额度")
    private BigDecimal orderPaymentAmount;

    @Schema(description = "配送地址")
    private OrderDeliveryAddress delivery;

}
