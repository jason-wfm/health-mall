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
public class ConfigBaseIndexReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "所属模块(ENUM):1001-站点设置;1002-上传设置;1003-运营设置;1004-财务设置;")
    private Integer configTypeModule;
}
