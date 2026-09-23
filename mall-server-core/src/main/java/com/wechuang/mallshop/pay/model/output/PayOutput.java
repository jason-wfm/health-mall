package com.wechuang.mallshop.pay.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付业务处理返回")
public class PayOutput {

    @Schema(description = "交易订单号")
    private String tradeNo;

    @Schema(description = "订单标题")
    private String title;

    @Schema(description = "支付金额")
    private int amount;

}
