package com.wechuang.mallshop.common.utils.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "卡券核销")
public class UserCouponValidationDto implements Serializable {
    @Schema(description = "应用id")
    private String appId;

    @Schema(description = "用户id")
    private String oneId;

    @Schema(description = "请采用不重复的业务ID，方便后续查询跟踪")
    private String bizId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "用户类型")
    private String userType = "ALL";

    @Schema(description = "卡券验证")
    private String validationCode;

    @Schema(description = "卡券列表")
    private String[] snList;
}
