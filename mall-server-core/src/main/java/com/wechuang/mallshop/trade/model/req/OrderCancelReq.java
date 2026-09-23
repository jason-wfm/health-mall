package com.wechuang.mallshop.trade.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "订单取消接口")
public class OrderCancelReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    @NotBlank(message = "请输入订单编号")
    private String orderId;

    @Schema(description = "取消原因")
    private String orderCancelReason;
}