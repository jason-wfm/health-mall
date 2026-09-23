package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户详细信息表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户详细信息表参数")
public class UserInfoAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户账号")
    private String userAccount;


    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "状态(ENUM):0-锁定;1-已激活;2-未激活;")
    private Integer userState;

    @Schema(description = "性别(ENUM):0-保密;1-男;  2-女;")
    private Integer userGender;

    @Schema(description = "生日(DATE)")
    private Date userBirthday;

    @Schema(description = "国家编码")
    private String userIntl;

    @Schema(description = "手机号码(mobile)")
    private String userMobile;

    @Schema(description = "用户邮箱(email)")
    private String userEmail;

    @Schema(description = "用户等级")
    private Integer userLevelId;

    @Schema(description = "真实姓名")
    private String userRealname;

    @Schema(description = "身份证")
    private String userIdcard;

    @Schema(description = "身份证图片(DOT)")
    private String userIdcardImages;

    @Schema(description = "认证状态(ENUM):0-未认证;1-待审核;2-认证通过;3-认证失败")
    private Integer userIsAuthentication;

    @Schema(description = "用户标签(DOT)")
    private String tagIds;

    @Schema(description = "行业编号")
    private Integer industryId;

    @Schema(description = "用户来源(ENUM):2310-其它;2311-pc;2312-H5;2313-APP;2314-小程序;2315-公众号")
    private Integer userFrom;

    @Schema(description = "新人标识(BOOL):0-不是;1-是")
    private Boolean userNew;

    @Schema(description = "上级用户编号")
    private Integer userParentId;

    @Schema(description = "是否销售员(BOOL):0-不是;1-是")
    private Boolean userIsSale;

    @Schema(description = "销售员id")
    private Integer userSaleId;

    @Schema(description = "微信二维码")
    private String userWxCard;
}
