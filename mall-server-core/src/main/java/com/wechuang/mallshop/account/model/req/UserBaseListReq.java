package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
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
public class UserBaseListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "salt值")
    private String userSalt;


}
