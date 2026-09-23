package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "配置分组表分页查询")
public class ConfigTypeListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组编号")
    private Integer configTypeId;

    @Schema(description = "分组名称")
    private String configTypeName;

    @Schema(description = "分组排序:从小到大")
    private Integer configTypeSort;

    @Schema(description = "是否有效(BOOL):0-禁用;1-启用")
    private Boolean configTypeEnable;

    public ConfigTypeListReq() {
        setSidx("config_type_sort");
    }
}
