package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户等级表-平台
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户等级表-平台参数")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLevelEditReq extends UserLevelAddReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "等级编号")
    private Integer userLevelId;


}
