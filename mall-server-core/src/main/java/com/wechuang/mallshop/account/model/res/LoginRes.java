package com.wechuang.mallshop.account.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "登录返回参数")
public class LoginRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "access token")
    private String token;

    @Schema(description = "注册用户编号")
    private Integer userId;
}
