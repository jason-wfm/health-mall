package com.wechuang.mallshop.account.model.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.account.model.req.LoginReq;
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
@Schema(name = "")
public class RegInput extends LoginReq implements Serializable {

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "国家编码")
    private String userIntl;

    @Schema(description = "手机号")
    private Long userMobile;

    @Schema(description = "邮箱")
    private String userEmail;

    @Schema(description = "活动编号")
    private Integer activityId;

    @Schema(description = "来源用户编号")
    private Integer userParentId;

    @Schema(description = "注册方式=>BindConnectCode")
    private Integer bindType = 3;

    @Schema(description = "角色编号:0-用户;2-商家;3-门店;8-租户;9-平台;")
    private Integer roleId = 0;

    @Schema(description = "店铺编号")
    private Integer storeId = 0;

    @Schema(description = "门店编号")
    private Integer chainId = 0;

    @Schema(description = "分站编号")
    private Integer subSiteId = 0;

    @Schema(description = "行业编号")
    private Integer industryId;
}
