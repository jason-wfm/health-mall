package com.wechuang.mallshop.common.utils.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "积分使用对象")
public class PointsUseDto implements Serializable {

    @Schema(description = "业务上唯一 id")
    private String bizId;

    @Schema(description = "对使用积分的描述")
    private String description;

    @Schema(description = "用户唯一 oneId")
    private String oneId;

    @Schema(description = "需要使用的积分数额")
    private Integer points;
}
