package com.wechuang.mallshop.account.model.res;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.admin.model.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Schema(name = "", description = "userbase usersns userinfo表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserBaseMixedRes implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否关注")
    private Boolean IsFollow;

    @Schema(description = "角色列表")
    @TableField(exist = false)
    private List<UserRole> roles;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "生日(DATE)")
    private Date userBirthday;

    @Schema(description = "微博数量")
    private Integer userBlog;

    @Schema(description = "是否可以购买商品(BOOL):0-不可以;1-可以")
    private Boolean userBuy;

    @Schema(description = "认证状态(ENUM):0-未认证;1-待审核;2-认证通过;3-认证失败")
    private Integer userIsAuthentication;

    @Schema(description = "是否允许发表言论(BOOL):0-不可以;1-可以")
    private Integer userComment;

    @Schema(description = "国家编码")
    private String userIntl;

    @Schema(description = "用户邮箱(email)")
    private String userEmail;

    @Schema(description = "推广店铺数量")
    private Integer userFansStore;

    @Schema(description = "粉丝数量")
    private Integer userFans;

    @Schema(description = "收藏品牌")
    private Integer userFavoritesBrand;

    @Schema(description = "收藏商品")
    private Integer userFavoritesItem;

    @Schema(description = "好友数量")
    private Integer userFriend;

    @Schema(description = "性别(ENUM):0-保密;1-男;  2-女;")
    private Integer userGender;

    @Schema(description = "成长值")
    private Integer userGrowth;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "身份证")
    private String userIdcard;

    @Schema(description = "身份证图片(DOT)")
    private String userIdcardImages;

    @Schema(description = "等级编号")
    private Integer userLevelId;

    @Schema(description = "手机号码(mobile)")
    private String userMobile;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "背景图片")
    private String userPoster;

    @Schema(description = "用户签名")
    private String userSign;

    @Schema(description = "收藏帖子")
    private Integer userStoryCollection;

    @Schema(description = "帖子点赞")
    private Integer userStoryLike;

    @Schema(description = "帖子转发")
    private Integer userStoryForward;

    @Schema(description = "帖子数量")
    private Integer userStory;

    @Schema(description = "评论数量")
    private Integer userStoryComment;

    @Schema(description = "收藏店铺")
    private Integer userFavoritesStore;


}
