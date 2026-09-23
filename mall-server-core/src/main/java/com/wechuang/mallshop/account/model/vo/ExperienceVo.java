package com.wechuang.mallshop.account.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(name = "操作用户经验对象", description = "操作用户经验对象")
public class ExperienceVo {

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "经验值")
    private BigDecimal exp;

    @Schema(description = "等级编号")
    private Integer expTypeId;

    @Schema(description = "描述")
    private String desc;

}
