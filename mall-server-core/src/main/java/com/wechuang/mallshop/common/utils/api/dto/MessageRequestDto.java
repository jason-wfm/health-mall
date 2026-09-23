package com.wechuang.mallshop.common.utils.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "消息中心数据推送对象")
public class MessageRequestDto implements Serializable {

    @Schema(description = "业务值")
    private String messageEvent;

    @Schema(description = "sub")
    private String userId;

    @Schema(description = "")
    private String templateId;

    @Schema(description = "数据")
    private Object extData;
}
