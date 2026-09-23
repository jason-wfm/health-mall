package com.wechuang.mallshop.audit.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [healthmall-ext] 审计操作日志分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "审计操作日志分页查询")
public class AudOperationLogListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "操作用户编号")
    private Integer userId;

    @Schema(description = "操作动作编码")
    private String action;

    @Schema(description = "资源类型")
    private String resourceType;

    @Schema(description = "资源编号")
    private String resourceId;

    @Schema(description = "执行结果:1-成功;0-失败")
    private Integer result;

    @Schema(description = "链路追踪编号")
    private String traceId;

    @TableField(exist = false)
    private String sidx = "log_id";
}
