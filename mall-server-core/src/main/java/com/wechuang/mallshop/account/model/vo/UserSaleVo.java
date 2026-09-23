package com.wechuang.mallshop.account.model.vo;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "销售员信息", description = "销售员信息")
public class UserSaleVo implements Serializable {

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "手机号码")
    private String userMobile;

    @Schema(description = "国家编码")
    private String userIntl;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "微信二维码")
    private String userWxCard;

}
