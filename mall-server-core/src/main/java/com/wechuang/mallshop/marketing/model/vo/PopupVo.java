package com.wechuang.mallshop.marketing.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "弹窗")
public class PopupVo {

    @Schema(description = "弹窗图片")
    private String popUpImage;

    @Schema(description = "弹窗网址")
    private String popUpUrl;

    @Schema(description = "弹窗活动类型(ENUM):0-新人礼包;1-其他活动")
    private Integer popUpType;
}
