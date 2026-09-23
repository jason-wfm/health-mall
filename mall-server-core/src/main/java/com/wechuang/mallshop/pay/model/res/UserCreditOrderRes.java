package com.wechuang.mallshop.pay.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.CreditInfo;
import com.wechuang.mallshop.pay.model.entity.CreditOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "用户资源对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCreditOrderRes extends BaseListRes<CreditOrder> {
    @Schema(description = "账单信息")
    private CreditInfo info;
}
