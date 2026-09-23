package com.wechuang.mallshop.account.model.vo;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "指定导出用户详细信息", description = "指定导出用户详细信息")
public class UserInfoVo {

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "状态")
    private String userState;

    @Schema(description = "手机号码")
    private String userMobile;

    @Schema(description = "国家编码")
    private String userIntl;

    @Schema(description = "性别")
    private String userGender;

    @Schema(description = "生日")
    private String userBirthday;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "等级编号")
    private Integer userLevelId;

    @Schema(description = "认证状态")
    private String userIsAuthentication;

    @Schema(description = "用户标签")
    private String tagIds;

    @Schema(description = "用户来源")
    private Integer userFrom;

    @Schema(description = "新人标识")
    private String userNew;

    @Schema(description = "上级用户编号")
    private Integer userParentId;

    @Schema(description = "平台标识")
    private Integer puid;

    @Schema(description = "用户标识")
    private Integer suid;

    @Schema(description = "用户编号")
    private Integer id;

    @Schema(description = "用户头像")
    private String avatar;
    @Schema(description = "用户备注")
    private String remark;
    @Schema(description = "用户昵称")
    private String username;

    @Schema(description = "用户状态")
    private String status;
}
