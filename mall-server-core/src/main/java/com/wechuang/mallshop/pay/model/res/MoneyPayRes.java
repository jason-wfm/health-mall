package com.wechuang.mallshop.pay.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "余额支付结果")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MoneyPayRes {
    @Schema(description = "订单编号(DOT)")
    private String orderId;

    @Schema(description = "状态码")
    private Integer statusCode = 200;

    @Schema(description = "订单已支付完成")
    private boolean paid = false;

    @Schema(description = "101：需要支付密码")
    private Integer code = 0;

}
