package com.wechuang.mallshop.trade.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.vo.OrderDashboardVo;
import com.wechuang.mallshop.trade.model.vo.ProductDashboardVo;
import com.wechuang.mallshop.trade.model.vo.ReturnOrderDashboardVo;
import com.wechuang.mallshop.trade.model.vo.StoreInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商家统计信息")
public class SellerDashboardRes implements Serializable {

    @Schema(description = "店铺信息")
    private StoreInfoVo storeInfo;

    @Schema(description = "订单信息")
    private OrderDashboardVo order;

    @Schema(description = "退货信息")
    private ReturnOrderDashboardVo returnOrder;

    @Schema(description = "商品信息")
    private ProductDashboardVo product;

}
