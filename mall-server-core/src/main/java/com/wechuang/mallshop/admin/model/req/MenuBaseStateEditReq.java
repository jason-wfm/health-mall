package com.wechuang.mallshop.admin.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "系统菜单表参数")
public class MenuBaseStateEditReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单编号")
    @NotNull(message = "是否读取不能为空")
    private Integer menuId;

    @Schema(description = "允许关闭(BOOL):0-禁止;1-允许")
    @NotNull(message = "允许关闭不能为空")
    private Boolean menuClose;

    @Schema(description = "是否隐藏(BOOL):0-展示;1-隐藏")
    @NotNull(message = "是否隐藏不能为空")
    private Boolean menuHidden;

    @Schema(description = "是否红点(BOOL):0-隐藏;1-显示")
    @NotNull(message = "是否红点不能为空")
    private Boolean menuDot;

}
