package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "电子面单对象")
public class CommodityVo implements Serializable {

    @Schema(description = "商品品类")
    private String GoodsName;

    @Schema(description = "商品件数")
    private Integer Goodsquantity;

    @Schema(description = "商品重量")
    private BigDecimal GoodsWeight;

}
