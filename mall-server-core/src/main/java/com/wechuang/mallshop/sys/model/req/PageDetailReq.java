package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "编号管理表参数")
public class PageDetailReq implements Serializable {
    @Schema(description = "页面编号")
    private Long pageId;

    @Schema(description = "主页类型")
    private String pageIndex;

    @Schema(description = "分类编号")
    private Integer categoryId;

}
