package com.wechuang.mallshop.account.model.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户基本信息表参数")
public class UserBaseEditInput extends UserBaseAddInput {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @JsonProperty("user_id")
    private Integer userId;


}
