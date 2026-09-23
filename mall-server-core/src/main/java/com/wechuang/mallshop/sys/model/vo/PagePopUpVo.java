package com.wechuang.mallshop.sys.model.vo;

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
public class PagePopUpVo {

    @Schema(description = "是否展示弹窗图片")
    private Boolean popUpEnable;

    @Schema(description = "弹窗图片")
    private String popUpImage;

    @Schema(description = "弹窗网址")
    private String popUpUrl;
}
