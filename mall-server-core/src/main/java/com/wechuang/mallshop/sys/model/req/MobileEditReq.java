package com.wechuang.mallshop.sys.model.req;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "移动页面保存")
public class MobileEditReq implements Serializable {
    @Schema(description = "JSON字符串")
    private String appPageList;

    @Schema(description = "JSON字符串")
    private String appMemberCenter;
}
