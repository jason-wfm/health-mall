package com.wechuang.mallshop.admin.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "系统菜单表分页查询")
public class MenuTreeReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单名称")
    @QueryField(type = QueryType.LIKE)
    private String menuTitle;

    @Schema(description = "查询标识")
    private Integer type = 1;

    @Schema(description = "权限角色")
    private Integer userRoleId;
}
