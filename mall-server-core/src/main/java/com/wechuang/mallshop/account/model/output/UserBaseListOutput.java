package com.wechuang.mallshop.account.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.output.BaseListOutput;
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
@Schema(description = "用户基本信息表分页结果")
public class UserBaseListOutput extends BaseListOutput {

    private static final long serialVersionUID = 1L;
}
