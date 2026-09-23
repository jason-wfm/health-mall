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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 语言翻译表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "语言翻译表分页查询")
public class LangStandardListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "中文")
    @QueryField(type = QueryType.LIKE)
    private String zhCN;

    @Schema(description = "繁体中文")
    @QueryField(type = QueryType.LIKE)
    private String zhTW;

    @Schema(description = "英文")
    @QueryField(type = QueryType.LIKE)
    private String enGB;

    @Schema(description = "英文")
    @QueryField(value = "en_GB", type = QueryType.LIKE)
    private String enUS;

    @Schema(description = "泰语")
    @QueryField(type = QueryType.LIKE)
    private String thTH;

    @Schema(description = "西班牙")
    @QueryField(type = QueryType.LIKE)
    private String esMX;

    @Schema(description = "阿拉伯")
    @QueryField(type = QueryType.LIKE)
    private String arSA;

    @Schema(description = "越南语")
    @QueryField(type = QueryType.LIKE)
    private String viVN;

    @Schema(description = "土耳其")
    @QueryField(type = QueryType.LIKE)
    private String trTR;

    @Schema(description = "日语")
    @QueryField(type = QueryType.LIKE)
    private String jaJP;

    @Schema(description = "印尼语")
    @QueryField(type = QueryType.LIKE)
    private String idID;

    @Schema(description = "德语")
    @QueryField(type = QueryType.LIKE)
    private String deDE;

    @Schema(description = "法语")
    @QueryField(type = QueryType.LIKE)
    private String frFR;

    @Schema(description = "葡萄牙")
    @QueryField(type = QueryType.LIKE)
    private String ptPT;

    @Schema(description = "意大利")
    @QueryField(type = QueryType.LIKE)
    private String itIT;

    @Schema(description = "俄罗斯")
    @QueryField(type = QueryType.LIKE)
    private String ruRU;

    @Schema(description = "罗马尼亚")
    @QueryField(type = QueryType.LIKE)
    private String roRO;

    @Schema(description = "阿塞拜疆语")
    @QueryField(type = QueryType.LIKE)
    private String azAZ;

    @Schema(description = "希腊语")
    @QueryField(type = QueryType.LIKE)
    private String elGR;

    @Schema(description = "芬兰语")
    @QueryField(type = QueryType.LIKE)
    private String fiFI;

    @Schema(description = "拉脱维亚语")
    @QueryField(type = QueryType.LIKE)
    private String lvLV;

    @Schema(description = "荷兰语")
    @QueryField(type = QueryType.LIKE)
    private String nlNL;

    @Schema(description = "丹麦语")
    @QueryField(type = QueryType.LIKE)
    private String daDK;

    @Schema(description = "塞尔维亚语")
    @QueryField(type = QueryType.LIKE)
    private String srRS;

    @Schema(description = "波兰语")
    @QueryField(type = QueryType.LIKE)
    private String plPL;

    @Schema(description = "乌克兰语")
    @QueryField(type = QueryType.LIKE)
    private String ukUA;

    @Schema(description = "哈萨克斯坦")
    @QueryField(type = QueryType.LIKE)
    private String kkKZ;

    @Schema(description = "缅甸语")
    @QueryField(type = QueryType.LIKE)
    private String myMM;

    @Schema(description = "韩语")
    @QueryField(type = QueryType.LIKE)
    private String koKR;

    @Schema(description = "马来语")
    @QueryField(type = QueryType.LIKE)
    private String msMY;


    @TableField(exist = false)
    private String sidx = "time";

    @TableField(exist = false)
    private String sort = Constants.ORDER_BY_DESC;
}
