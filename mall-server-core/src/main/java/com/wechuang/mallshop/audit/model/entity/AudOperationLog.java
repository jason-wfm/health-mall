package com.wechuang.mallshop.audit.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * [healthmall-ext] 审计操作日志
 * 对齐《健康商城重构方案》6.5 节：重点记录健康数据查看/导出、退款、改价、派单、提现、权限变更等
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("aud_operation_log")
@Schema(name = "AudOperationLog对象", description = "审计操作日志")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AudOperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "日志编号")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @Schema(description = "操作用户编号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "操作用户账号")
    @TableField("user_account")
    private String userAccount;

    @Schema(description = "角色编号:0-用户;2-商家;3-门店;8-租户;9-平台")
    @TableField("role_id")
    private Integer roleId;

    @Schema(description = "商家编号(=店铺store_id)")
    @TableField("merchant_id")
    private Integer merchantId;

    @Schema(description = "门店编号")
    @TableField("chain_id")
    private Integer chainId;

    @Schema(description = "客户端IP")
    @TableField("client_ip")
    private String clientIp;

    @Schema(description = "请求方法")
    @TableField("http_method")
    private String httpMethod;

    @Schema(description = "请求URI")
    @TableField("request_uri")
    private String requestUri;

    @Schema(description = "操作动作编码")
    @TableField("action")
    private String action;

    @Schema(description = "资源类型")
    @TableField("resource_type")
    private String resourceType;

    @Schema(description = "资源编号")
    @TableField("resource_id")
    private String resourceId;

    @Schema(description = "变更前值(JSON)")
    @TableField("old_value")
    private String oldValue;

    @Schema(description = "变更后值(JSON)")
    @TableField("new_value")
    private String newValue;

    @Schema(description = "执行结果:1-成功;0-失败")
    @TableField("result")
    private Integer result;

    @Schema(description = "失败信息")
    @TableField("error_msg")
    private String errorMsg;

    @Schema(description = "链路追踪编号")
    @TableField("trace_id")
    private String traceId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
