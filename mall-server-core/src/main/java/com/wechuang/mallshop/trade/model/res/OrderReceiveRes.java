package com.wechuang.mallshop.trade.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "订单确认收货接口响应")
public class OrderReceiveRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    private List<String> orderId = new ArrayList<>();
}