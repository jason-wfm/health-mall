package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 配置分组表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "配置分组表参数")
public class ConfigTypeEditReq extends ConfigTypeAddReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组编号")
    @NotBlank(message = "请输入分组编号")
    private Integer configTypeId;
}
