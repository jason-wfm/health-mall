package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 用户等级表-平台
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户等级表-平台分页查询")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLevelListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "等级编号")
    private Integer userLevelId;

    @Schema(description = "等级名称")
    private String userLevelName;

    @Schema(description = "累计消费")
    private Integer userLevelSpend;

    @Schema(description = "LOGO")
    private String userLevelLogo;

    @Schema(description = "折扣率百分比")
    private BigDecimal userLevelRate;

    @Schema(description = "修改时间")
    private Long userLevelTime;

    @Schema(description = "系统内置(BOOL):0-否;1-是")
    private Boolean userLevelIsBuildin;


}
