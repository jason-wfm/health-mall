package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "登录参数")
public class ResetPasswordReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户密码")
    @NotEmpty(message = "请输入用户密码")
    private String password;

    @Schema(description = "用户密码")
    private String oldPassword;


    @Schema(description = "验证码")
    private String verifyCode;

    @Schema(description = "验证码KEY")
    private String verifyKey;

    @Schema(description = "密码是否加密")
    private Boolean encrypt = false;

    @Schema(description = "注册方式=>BindConnectCode")
    private Integer bindType;
}
