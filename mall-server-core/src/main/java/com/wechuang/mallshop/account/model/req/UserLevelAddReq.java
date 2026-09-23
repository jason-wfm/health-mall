package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户等级表-平台参数")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLevelAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "等级名称")
    private String userLevelName;

    @Schema(description = "升级经验值")
    private Integer userLevelExp;

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
