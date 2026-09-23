package com.wechuang.mallshop.admin.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "前端路由对象")
public class MenuTreeRes {

    @Schema(description = "路径")
    @JsonProperty("menu_id")
    private Integer menuId;

    @Schema(description = "菜单父编号")
    @JsonProperty("menu_parent_id")
    private Integer menuParentId;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "组件名称")
    @JsonProperty("menu_name")
    private String menuName;

    @Schema(description = "组件路由")
    @JsonProperty("menu_path")
    private String menuPath;

    @Schema(description = "组件路径")
    @JsonProperty("menu_component")
    private String menuComponent;

    @Schema(description = "菜单名称")
    @JsonProperty("menu_title")
    private String menuTitle;

    @Schema(description = "图标设置")
    @JsonProperty("menu_icon")
    private String menuIcon;

    @Schema(description = "是否红点(BOOL):0-隐藏;1-显示")
    @JsonProperty("menu_dot")
    private Boolean menuDot;

    @Schema(description = "菜单标签")
    @JsonProperty("menu_bubble")
    private String menuBubble;

    @Schema(description = "菜单排序")
    @JsonProperty("menu_sort")
    private Integer menuSort;

    @Schema(description = "权限标识:后端地址")
    @JsonProperty("menu_permission")
    private String menuPermission;

    @Schema(description = "系统内置(BOOL):1-是; 0-否")
    @JsonProperty("menu_buildin")
    private Boolean menuBuildin;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "重定向地址")
    private String redirect;

    @Schema(description = "菜单类型")
    @JsonProperty("menu_type")
    private Integer menuType;

    @Schema(description = "路由目录属性")
    private Meta meta;

    /*private String children;*/
    @JsonProperty("menu_hidden")
    private boolean menuHidden = false;
    @Schema(description = "路由子集")
    private List<MenuTreeRes> children;
}
