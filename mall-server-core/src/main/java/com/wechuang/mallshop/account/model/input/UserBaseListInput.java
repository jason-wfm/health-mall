package com.wechuang.mallshop.account.model.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wechuang.mallshop.core.web.model.input.BaseListInput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户基本信息表分页查询")
public class UserBaseListInput extends BaseListInput {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @JsonProperty("user_id")
    private Integer userId;

    @Schema(description = "用户账号")
    @JsonProperty("user_account")
    private String userAccount;

    @Schema(description = "用户密码")
    @JsonProperty("user_password")
    private String userPassword;

    @Schema(description = "salt值")
    @JsonProperty("user_salt")
    private String userSalt;


}
