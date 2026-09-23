package com.wechuang.mallshop.sys.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class PageDataItemVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "图片地址")
    private String path;

    @Schema(description = "图片地址 fix for swipe")
    @JsonProperty("pathBg")
    private String pathBg;

    @Schema(description = "标题")
    private String name;

    @Schema(description = "市场价")
    @JsonProperty("MarketPice")
    private Integer MarketPice;

    @Schema(description = "销售价")
    @JsonProperty("ItemSalePrice")
    private Integer ItemSalePrice;

    @Schema(description = "广告语")
    @JsonProperty("ProductTips")
    private String ProductTips;

    @Schema(description = "访问网址")
    @JsonProperty("AppUrl")
    private String AppUrl;

    @Schema(description = "小程序AppId")
    @JsonProperty("AppId")
    private String AppId;

    @Schema(description = "小程序跳转的页面")
    @JsonProperty("MinAppUrl")
    private String MinAppUrl;

    @Schema(description = "开始时间")
    @JsonProperty("StartTime")
    private String StartTime;

    @Schema(description = "开始时间")
    @JsonProperty("StartTimeStr")
    private String StartTimeStr;

    @Schema(description = "结束时间")
    @JsonProperty("EndTime")
    private String EndTime;

    @Schema(description = "结束时间")
    @JsonProperty("EndTimeStr")
    private String EndTimeStr;

    @Schema(description = "{{items.UserLimit}}人团")
    @JsonProperty("UserLimit")
    private Integer UserLimit;

    @Schema(description = "已有{{items.OrderCount}}人参加")
    @JsonProperty("OrderCount")
    private Integer OrderCount;


    @Schema(description = "表格宽度")
    @JsonProperty("flexNum")
    private int flexNum;

    @JsonProperty("selectType")
    private int selectType;

    private Long did;

    @Schema(description = "图片规格")
    @JsonProperty("specImg")
    private String specImg;

    @Schema(description = "搜索关键字")
    @JsonProperty("keyWord")
    private String keyWord;

    @Schema(description = "富文本")
    @JsonProperty("words")
    private String words;
}
