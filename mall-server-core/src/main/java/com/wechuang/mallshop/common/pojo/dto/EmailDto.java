package com.wechuang.mallshop.common.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "邮件配置DTO", description = "邮件配置DTO")
public class EmailDto {
    @Schema(description = "发信邮箱")
    private String emailAddr;

    @Schema(description = "发信人")
    private String emailFromname;

    @Schema(description = "SMTP 服务器")
    private String emailHost;

    @Schema(description = "SMTP 端口号")
    private Integer emailPort;

    @Schema(description = "单行SMTP 身份验证用户名")
    private String emailId;

    @Schema(description = "SMTP 身份验证密码")
    private String emailPass;

    @Schema(description = "是否加密")
    private String emailSecure;

    @Schema(description = "调试级别")
    private String emailDebug;

    @Schema(description = "测试接收的邮件地址")
    private String emailToAddress;

    @Schema(description = "邮件主题")
    private String subject;

    @Schema(description = "邮件内容")
    private String content;
}