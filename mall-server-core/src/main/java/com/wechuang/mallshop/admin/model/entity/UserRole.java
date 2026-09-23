package com.wechuang.mallshop.admin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@TableName("admin_user_role")
@Schema(name = "UserRole对象", description = "权限组表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色编号")
    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;

    @Schema(description = "角色名称")
    @TableField("user_role_name")
    private String userRoleName;

    @Schema(description = "角色名称")
    @TableField("user_role_code")
    private String userRoleCode;

    @Schema(description = "请求列表(DOT)")
    @TableField("menu_ids")
    private String menuIds;

    @Schema(description = "创建时间")
    @TableField("user_role_ctime")
    private Date userRoleCTime;

    @Schema(description = "创建时间")
    @TableField("user_role_utime")
    private Date userRoleUtime;

    @Schema(description = "是否内置")
    @TableField("user_role_buildin")
    private Boolean userRoleBuildin;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}