package com.wechuang.mallshop.pay.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "微信jsapi支付结果")
public class WechatJSAPIPayRes extends MoneyPayRes {

    @Schema(description = "微信V3支付结果")
    private Map<String, String> data;

    @Schema(description = "微信V3支付响应")
    private String response;

    @Schema(description = "支付跳转链接")
    private String mwebUrl;

}
