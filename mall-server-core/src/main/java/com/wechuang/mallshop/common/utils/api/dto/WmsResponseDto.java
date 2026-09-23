package com.wechuang.mallshop.common.utils.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "WMS出参", description = "WMS出参")
@XmlRootElement(name = "response")
public class WmsResponseDto {

    @Schema(description = "success|failure")
    private String flag;

    @Schema(description = "错误代码")
    private String code;

    @Schema(description = "返回状态描述")
    private String message;

}
