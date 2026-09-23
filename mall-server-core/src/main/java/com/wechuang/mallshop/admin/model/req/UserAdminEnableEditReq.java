package com.wechuang.mallshop.admin.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "UserAdminEnable参数")
public class UserAdminEnableEditReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @NotNull(message = "用户编号不能为空")
    private Integer userId;

    @Schema(description = "是否启用(BOOL):0-否;1-是")
    @NotNull(message = "是否启用不能为空")
    private Boolean userEnable;

}
