package com.wechuang.mallshop.pay.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付结果")
public class ProcessPayOutput {
    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "支付完成")
    private Boolean paid;
}
