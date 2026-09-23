package com.wechuang.mallshop.trade.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "课程")
public class OrderCourseRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "课程名称")
    private String orderTitle;

    @Schema(description = "状态(ENUM): 1-学习中; 2-已学完; 3-已过期")
    private Integer studyState;

    @Schema(description = "学习进度")
    private String courseSchedule;
}