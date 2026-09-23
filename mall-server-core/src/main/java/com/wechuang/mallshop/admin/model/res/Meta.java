package com.wechuang.mallshop.admin.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "前端路由对象属性")
public class Meta {

    @Schema(description = "菜单编号")
    @JsonProperty("menu_id")
    private Integer menuId;

    @Schema(description = "红点")
    private Boolean dot;

    @Schema(description = "路由目录中文标题")
    private String title;

    @Schema(description = "路由目录图片")
    private String icon;

    @Schema(description = "路由目录能否被关闭")
    private Boolean noClosable;

    @Schema(description = "是否隐藏")
    private Boolean hidden;

    @Schema(description = "badge")
    private String badge;
}
