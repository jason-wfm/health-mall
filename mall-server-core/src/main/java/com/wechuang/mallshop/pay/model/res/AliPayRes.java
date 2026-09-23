package com.wechuang.mallshop.pay.model.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "支付宝网页支付结果")
public class AliPayRes {

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "处理其它逻辑状态")
    private Integer statusCode;

    @Schema(description = "订单已支付完成")
    private Boolean paid;

    @Schema(description = "支付跳转链接")
    private String mwebUrl;

}
