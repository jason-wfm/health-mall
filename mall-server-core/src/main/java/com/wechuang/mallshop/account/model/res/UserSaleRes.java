package com.wechuang.mallshop.account.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "用户销售信息表")
public class UserSaleRes implements Serializable {

    @Schema(description = "今日新增客户")
    private Long todayCustomerNum;

    @Schema(description = "总客户数量")
    private Long totalCustomerNum;

    @Schema(description = "今日订单数量")
    private Long todayOrderNum;

    @Schema(description = "总订单数量")
    private Long totalOrderNum;

    @Schema(description = "今日订单额")
    private BigDecimal todayOrderAmount;

    @Schema(description = "总订单额")
    private BigDecimal totalOrderAmount;

}
