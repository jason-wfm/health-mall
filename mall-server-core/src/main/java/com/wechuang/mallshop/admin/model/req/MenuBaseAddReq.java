package com.wechuang.mallshop.admin.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-28
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "系统菜单表参数")
public class MenuBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单父编号")
    private Integer menuParentId;

    @Schema(description = "菜单名称")
    private String menuTitle;

    @Schema(description = "页面网址")
    private String menuUrl;

    @Schema(description = "组件名称")
    private String menuName;

    @Schema(description = "组件路由")
    private String menuPath;

    @Schema(description = "组件路径")
    private String menuComponent;

    @Schema(description = "重定向")
    private String menuRedirect;

    @Schema(description = "允许关闭(BOOL):0-禁止;1-允许")
    private Boolean menuClose;

    @Schema(description = "是否隐藏(BOOL):0-展示;1-隐藏")
    private Boolean menuHidden;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    private Boolean menuEnable;

    @Schema(description = "样式class")
    private String menuClass;

    @Schema(description = "图标设置")
    private String menuIcon;

    @Schema(description = "是否红点(BOOL):0-隐藏;1-显示")
    private Boolean menuDot;

    @Schema(description = "菜单标签")
    private String menuBubble;

    @Schema(description = "菜单排序")
    private Integer menuSort;

    @Schema(description = "菜单类型(LIST):0-按钮;1-菜单")
    private Boolean menuType;

    @Schema(description = "备注")
    private String menuNote;

    @Schema(description = "功能开启:设置config_key")
    private String menuFunc;

    @Schema(description = "角色类型(LIST):1-平台;2-商户;3-门店")
    private Integer menuRole;

    @Schema(description = "url参数")
    private String menuParam;

    @Schema(description = "权限标识:后端地址")
    private String menuPermission;

    @Schema(description = "最后更新时间")
    private Date menuTime;


}
