package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ConfigBaseEditReq extends ConfigBaseAddReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置编码")
    private String configKey;


}
