package com.wechuang.mallshop.account.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.admin.model.entity.MenuBase;
import com.wechuang.mallshop.admin.model.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_user_base")
@Schema(name = "UserBase对象", description = "用户基本信息表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @Schema(description = "用户账号")
    @TableField("user_account")
    private String userAccount;

    @Schema(description = "用户密码")
    @TableField("user_password")
    private String userPassword;

    @Schema(description = "salt值")
    @TableField("user_salt")
    private String userSalt;

    @Schema(description = "角色列表")
    @TableField(exist = false)
    private List<UserRole> roles;

    @Schema(description = "权限列表")
    @TableField(exist = false)
    private List<MenuBase> authorities;

    @Schema(description = "角色编号:0-用户;2-商家;3-门店;8-租户;9-平台;")
    @TableField(exist = false)
    private Integer roleId = 0;

    @Schema(description = "分站编号:0-总账")
    @TableField(exist = false)
    private Integer siteId = 0;

    @Schema(description = "店铺编号")
    @TableField(exist = false)
    private Integer storeId = 0;

    @Schema(description = "门店编号")
    @TableField(exist = false)
    private Integer chainId = 0;

    @Schema(description = "[healthmall-ext] 商家编号:0-平台/无归属")
    @TableField(exist = false)
    private Integer merchantId = 0;

    @Schema(description = "行业编号")
    @TableField(exist = false)
    private Integer industryId = 0;

    @Schema(description = "后台管理:admin=1;移动端front=0")
    @TableField(exist = false)
    private Integer clientId = 0;

    @Schema(description = "用户昵称")
    @TableField(exist = false)
    private String userNickname;

    @Schema(description = "是否启用(BOOL):0-否;1-是")
    @TableField(exist = false)
    private Boolean userEnable;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
