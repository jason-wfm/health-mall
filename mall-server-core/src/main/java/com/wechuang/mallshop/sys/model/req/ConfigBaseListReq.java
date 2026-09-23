package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "系统参数设置表分页查询")
public class ConfigBaseListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置编码")
    private String configKey;

    @Schema(description = "配置标题")
    private String configTitle;

    @Schema(description = "所属分类")
    private Integer configTypeId;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    private Boolean configEnable;

    public ConfigBaseListReq() {
        setSidx("config_sort");
    }
}
