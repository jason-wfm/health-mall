package com.wechuang.mallshop.pay.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pay.model.vo.PointStepVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "签到信息对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignInfoRes implements Serializable {

    @Schema(description = "连续签到天数")
    private Integer continueSignDays;

    @Schema(description = "已签到日期集合")
    private List<String> signDayArr;

    @Schema(description = "签到规则")
    private List<PointStepVo> signList;

    @Schema(description = "今日是否签到")
    private Integer todayIsSign;

}
