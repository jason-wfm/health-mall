package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 配置分组表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "配置分组表参数")
public class ConfigTypeAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组名称")
    private String configTypeName;

    @Schema(description = "分组排序:从小到大")
    private Integer configTypeSort;

    @Schema(description = "是否有效(BOOL):0-禁用;1-启用")
    private Boolean configTypeEnable;

    @Schema(description = "所属模块(ENUM):1001-站点设置;1002-上传设置;1003-运营设置;1004-财务设置;")
    private Integer configTypeModule;
}
