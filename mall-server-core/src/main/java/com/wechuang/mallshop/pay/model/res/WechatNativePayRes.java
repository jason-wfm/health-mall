package com.wechuang.mallshop.pay.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "微信jsapi支付结果")
public class WechatNativePayRes extends MoneyPayRes {
    @Schema(description = "微信V3支付响应")
    private String response;

    @Schema(description = "支付链接")
    private String codeUrl;

    @Schema(description = "付款额度")
    private BigDecimal paymentAmount;
}
