package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统参数设置表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "系统参数设置表参数")
public class ConfigBaseStateEditReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置编码")
    @NotNull(message = "配置编码不能为空")
    private String configKey;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    @NotNull(message = "是否启用不能为空")
    private Boolean configEnable;
}
