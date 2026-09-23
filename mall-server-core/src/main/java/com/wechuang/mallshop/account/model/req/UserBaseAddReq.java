package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户基本信息表参数")
public class UserBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "salt值")
    private String userSalt;


}
