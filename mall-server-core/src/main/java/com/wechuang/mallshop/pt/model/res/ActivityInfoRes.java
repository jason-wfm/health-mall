package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.vo.ActivityInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActivityInfoRes {
    @Schema(description = "活动列表")
    List<ActivityInfoVo> items = new ArrayList<>();
}
