package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.trade.model.entity.OrderReturn;
import com.wechuang.mallshop.trade.model.entity.OrderReturnReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "退货订单详情")
public class OrderReturnVo extends OrderReturn {

    @Schema(description = "客户名称")
    private String buyerUserName;

    @Schema(description = "联系人")
    private String daName;

    @Schema(description = "手机号码")
    private String daMobile;

    @Schema(description = "省份")
    private String daProvince;

    @Schema(description = "市")
    private String daCity;

    @Schema(description = "县区")
    private String daCounty;

    @Schema(description = "详细地址")
    private String daAddress;

    @Schema(description = "商品列表")
    private List<OrderReturnItemVo> returnItemList;

    @Schema(description = "退货原因集合")
    List<OrderReturnReason> returnReasonList;

}
