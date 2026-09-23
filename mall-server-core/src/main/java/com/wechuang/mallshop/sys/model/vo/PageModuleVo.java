package com.wechuang.mallshop.sys.model.vo;


import cn.hutool.json.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageModuleVo {

    private Integer pmId;

    @Schema(description = "模块名称")
    private String pmName;

    private Long pageId;

    @Schema(description = "所属用户")
    private Integer userId;

    @Schema(description = "颜色")
    private String pmColor;

    @Schema(description = "所在页面")
    private String pmType;

    @Schema(description = "模版")
    private String moduleId;

    @Schema(description = "更新时间")
    private Date pmUtime;

    @Schema(description = "排序")
    private Integer pmOrder;

    @Schema(description = "是否显示")
    private Boolean pmEnable;

    @Schema(description = "模块html代码")
    private String pmHtml;

    @Schema(description = "模块JSON代码(JSON)")
    private JSON pmJson;

    @Schema(description = "所属分站Id:0-总站")
    private Integer subsiteId;

    @Schema(description = "column_left:content_top")
    private String pmPosition;

}
