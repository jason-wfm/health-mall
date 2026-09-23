package com.wechuang.mallshop.pay.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "云闪付支付结果")
public class UnionPayRes extends MoneyPayRes {

    @Schema(description = "云闪付支付结果")
    private Map<String, String> data;


    @Schema(description = "支付链接")
    private String codeUrl;

    @Schema(description = "付款额度")
    private BigDecimal paymentAmount;
}
