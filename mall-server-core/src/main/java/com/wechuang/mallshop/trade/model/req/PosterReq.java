package com.wechuang.mallshop.trade.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "海报DTO", description = "海报DTO")
public class PosterReq {
    @Schema(description = "推广员编号")
    private Integer userId;

    @Schema(description = "海报图网址")
    private String posterImg;

    @Schema(description = "文字标题")
    private String posterTitle;

    @Schema(description = "商品价格")
    private BigDecimal posterPrice;

    @Schema(description = "二维码网址")
    private String path;

    @Schema(description = "海报类型:1-H5;2-小程序")
    private Integer posterType;

    @Schema(description = "海报名称")
    private String posterName;
}
