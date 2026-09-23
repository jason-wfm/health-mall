package com.wechuang.mallshop.trade.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderAddOutput extends CheckoutOutput {

    @Schema(description = "订单编号")
    List<String> orderIds = new ArrayList<>();

    @Schema(description = "是否绑定手机")
    boolean mobileIsBind = true;

    @Schema(description = "拼团编号")
    Integer gbId;

    @Schema(description = "订单是否审核")
    Boolean orderIsReview = false;
}