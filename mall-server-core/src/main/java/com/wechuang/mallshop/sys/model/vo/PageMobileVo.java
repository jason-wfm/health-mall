package com.wechuang.mallshop.sys.model.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "手机装修")
public class PageMobileVo {

    @Schema(description = "页面编号")
    @TableId(value = "page_id", type = IdType.AUTO)
    @JsonProperty("Id")
    private Long pageId;

    @Schema(description = "页面名称")
    @TableField("page_name")
    @JsonProperty("PageTitle")
    private String pageName;

    @Schema(description = "所属店铺")
    @TableField("store_id")
    @JsonProperty("StoreId")
    private Integer storeId;

    @Schema(description = "所属用户")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "所属分站:0-总站")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "是否内置(BOOL):0-否;1-是")
    @TableField("page_buildin")
    private Boolean pageBuildin;

    @Schema(description = "类型(ENUM):1-WAP;2-PC;3-APP")
    @TableField("page_type")
    private Integer pageType = 3;

    @Schema(description = "页面布局模板")
    @TableField("page_tpl")
    private Integer pageTpl = 0;

    @Schema(description = "所属APP")
    @TableField("app_id")
    @JsonProperty("AppId")
    private Integer appId;

    @Schema(description = "页面代码")
    @TableField("page_code")
    @JsonProperty("PageCode")
    private String pageCode;

    @TableField("page_nav")
    @JsonProperty("PageNav")
    private String pageNav;

    @TableField("page_config")
    @JsonProperty("PageConfig")
    private String pageConfig;

    @TableField("page_share_title")
    @JsonProperty("ShareTitle")
    private String pageShareTitle;

    @TableField("page_share_image")
    @JsonProperty("ShareImg")
    private String pageShareImage;

    @TableField("page_qrcode")
    @JsonProperty("PageQRCode")
    private String pageQrcode;

    @Schema(description = "是否首页(BOOL):0-非首页;1-首页")
    @JsonProperty("IsHome")
    @TableField("page_index")
    private Boolean pageIndex;

    @Schema(description = "是否用户中心(BOOL):0-非用户中心;1-用户中心")
    @JsonProperty("IsPersonalCenter")
    @TableField(exist = false)
    private Boolean isUserCenter;

    @Schema(description = "拼团首页(BOOL):0-非首页;1-首页")
    @JsonProperty("IsGb")
    @TableField("page_gb")
    private Boolean pageGb;

    @Schema(description = "活动首页(BOOL):0-非首页;1-首页")
    @JsonProperty("IsActivity")
    @TableField("page_activity")
    private Boolean pageActivity;

    @Schema(description = "积分首页(BOOL):0-非首页;1-首页")
    @JsonProperty("IsPoint")
    @TableField("page_point")
    private Boolean pagePoint;

    @Schema(description = "团购首页(BOOL):0-非首页;1-首页")
    @TableField("page_gbs")
    private Boolean pageGbs;

    @Schema(description = "组合套餐(BOOL):0-非首页;1-首页")
    @TableField("page_package")
    private Boolean pagePackage;

    @Schema(description = "批发团购首页(BOOL):0-非首页;1-首页")
    @TableField("page_pfgb")
    private Boolean pagePfgb;

    @Schema(description = "社区(BOOL):0-非首页;1-首页")
    @TableField("page_sns")
    @JsonProperty("IsSns")
    private Boolean pageSns;

    @Schema(description = "资讯(BOOL):0-非首页;1-首页")
    @TableField("page_article")
    @JsonProperty("IsArticle")
    private Boolean pageArticle;

    @Schema(description = "零元购区(BOOL):0-否;1-是")
    @TableField("page_zerobuy")
    private Boolean pageZerobuy;

    @Schema(description = "高额返区(BOOL):0-否;1-是")
    @TableField("page_higharea")
    private Boolean pageHigharea;

    @Schema(description = "今日爆款(BOOL):0-否;1-是")
    @TableField("page_taday")
    private Boolean pageTaday;

    @Schema(description = "每日好店(BOOL):0-否;1-是")
    @TableField("page_everyday")
    private Boolean pageEveryday;

    @Schema(description = "整点秒杀(BOOL):0-否;1-是")
    @TableField("page_secondkill")
    @JsonProperty("SecondKill")
    private Boolean pageSecondkill;

    @Schema(description = "天天秒淘(BOOL):0-否;1-是")
    @TableField("page_secondday")
    private Boolean pageSecondday;

    @Schema(description = "设置土特产(BOOL):0-否;1-是")
    @TableField("page_rura")
    private Boolean pageRura;

    @Schema(description = "用户页banner(BOOL):0-否;1-是")
    @TableField("page_likeyou")
    private Boolean pageLikeyou;

    @Schema(description = "兑换专区(BOOL):0-否;1-是")
    @TableField("page_exchange")
    private Boolean pageExchange;

    @Schema(description = "新品首发(BOOL):0-否;1-是")
    @TableField("page_new")
    private Boolean pageNew;

    @Schema(description = "新人优惠(BOOL):0-否;1-是")
    @TableField("page_newperson")
    private Boolean pageNewperson;

    @Schema(description = "升级VIP(BOOL):0-否;1-是")
    @TableField("page_upgrade")
    @JsonProperty("IsUpgrade")
    private Boolean pageUpgrade;

    @Schema(description = "信息发布(BOOL):0-否;1-是")
    @TableField("page_message")
    private Boolean pageMessage;

    @Schema(description = "是否发布(BOOL):0-否;1-是")
    @JsonProperty("IsRelease")
    @TableField("page_release")
    private Boolean pageRelease;

}
