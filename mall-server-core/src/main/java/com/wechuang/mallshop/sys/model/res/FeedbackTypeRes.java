package com.wechuang.mallshop.sys.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.sys.model.entity.FeedbackCategory;
import com.wechuang.mallshop.sys.model.entity.FeedbackType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@Schema(name = "平台反馈")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeedbackTypeRes extends FeedbackType {

    @Schema(description = "反馈分类集合")
    private List<FeedbackCategory> rows;

}
