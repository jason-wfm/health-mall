// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 页面表
 * </p>
 *
 * @author Xinze
 * @since 2021-07-03
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "页面表参数")
public class PageBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "页面名称")
    private String pageName;

    @Schema(description = "所属店铺")
    private Integer storeId;

    @Schema(description = "所属用户")
    private Integer userId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "是否内置(BOOL):0-否;1-是")
    private Boolean pageBuildin;

    @Schema(description = "类型(ENUM):1-WAP;2-PC;3-APP")
    private Integer pageType;

    @Schema(description = "页面布局模板")
    private Integer pageTpl;

    @Schema(description = "所属APP")
    private Integer appId;

    @Schema(description = "页面代码")
    private String pageCode;

    private String pageNav;

    private String pageConfig;

    private String pageShareTitle;

    private String pageShareImage;

    private String pageQrcode;

    @Schema(description = "是否首页(BOOL):0-非首页;1-首页")
    private Boolean pageIndex;

    @Schema(description = "拼团首页(BOOL):0-非首页;1-首页")
    private Boolean pageGb;

    @Schema(description = "活动首页(BOOL):0-非首页;1-首页")
    private Boolean pageActivity;

    @Schema(description = "积分首页(BOOL):0-非首页;1-首页")
    private Boolean pagePoint;

    @Schema(description = "团购首页(BOOL):0-非首页;1-首页")
    private Boolean pageGbs;

    @Schema(description = "组合套餐(BOOL):0-非首页;1-首页")
    private Boolean pagePackage;

    @Schema(description = "批发团购首页(BOOL):0-非首页;1-首页")
    private Boolean pagePfgb;

    @Schema(description = "社区(BOOL):0-非首页;1-首页")
    private Boolean pageSns;

    @Schema(description = "资讯(BOOL):0-非首页;1-首页")
    private Boolean pageArticle;

    @Schema(description = "零元购区(BOOL):0-否;1-是")
    private Boolean pageZerobuy;

    @Schema(description = "高额返区(BOOL):0-否;1-是")
    private Boolean pageHigharea;

    @Schema(description = "今日爆款(BOOL):0-否;1-是")
    private Boolean pageTaday;

    @Schema(description = "每日好店(BOOL):0-否;1-是")
    private Boolean pageEveryday;

    @Schema(description = "整点秒杀(BOOL):0-否;1-是")
    private Boolean pageSecondkill;

    @Schema(description = "天天秒淘(BOOL):0-否;1-是")
    private Boolean pageSecondday;

    @Schema(description = "设置土特产(BOOL):0-否;1-是")
    private Boolean pageRura;

    @Schema(description = "用户页banner(BOOL):0-否;1-是")
    private Boolean pageLikeyou;

    @Schema(description = "兑换专区(BOOL):0-否;1-是")
    private Boolean pageExchange;

    @Schema(description = "新品首发(BOOL):0-否;1-是")
    private Boolean pageNew;

    @Schema(description = "新人优惠(BOOL):0-否;1-是")
    private Boolean pageNewperson;

    @Schema(description = "升级VIP(BOOL):0-否;1-是")
    private Boolean pageUpgrade;

    @Schema(description = "信息发布(BOOL):0-否;1-是")
    private Boolean pageMessage;

    @Schema(description = "是否发布(BOOL):0-否;1-是")
    private Boolean pageRelease;

    @Schema(description = "行业编号集合")
    private String industryIds;

}
