package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Schema(name = "批量加购物车", description = "批量加购物车")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartBatVo implements Serializable {
    @Schema(description = "商品编号")
    @NotEmpty(message = "请输入商品编号")
    private Long itemId;

    @Schema(description = "购买商品数量")
    @NotEmpty(message = "请输入商品数量")
    private Integer quantity;
}
