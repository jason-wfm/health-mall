package com.wechuang.mallshop.pay.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pay.model.entity.UserResource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "用户资源对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResourceRes extends UserResource {

    @Schema(description = "可用佣金")
    private BigDecimal userCommission;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "是否有支付密码")
    private Integer setPayPasswd;

}
