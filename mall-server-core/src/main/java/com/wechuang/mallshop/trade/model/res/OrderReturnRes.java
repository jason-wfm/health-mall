package com.wechuang.mallshop.trade.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.entity.OrderReturn;
import com.wechuang.mallshop.trade.model.vo.OrderReturnItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "售后服务")
public class OrderReturnRes extends OrderReturn {

    @Schema(description = "门店名称")
    private String chainName;

    @Schema(description = "退货商品总数量")
    private Integer returnNum;

    @Schema(description = "退款金额")
    private BigDecimal submitReturnRefundAmount;

    @Schema(description = "售后理由")
    private String returnReasonName;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "退货订单商品信息")
    private List<OrderReturnItemVo> items;

}
