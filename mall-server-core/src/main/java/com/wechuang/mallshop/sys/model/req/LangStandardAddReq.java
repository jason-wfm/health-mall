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
import java.util.Date;

/**
 * <p>
 * 语言翻译表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "语言翻译表参数")
public class LangStandardAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "繁体中文")
    private String zhTw;

    @Schema(description = "英文")
    private String enGb;

    @Schema(description = "泰语")
    private String thTh;

    @Schema(description = "西班牙")
    private String esMx;

    @Schema(description = "阿拉伯")
    private String arSa;

    @Schema(description = "越南语")
    private String viVn;

    @Schema(description = "土耳其")
    private String trTr;

    @Schema(description = "日语")
    private String jaJp;

    @Schema(description = "印尼语")
    private String idId;

    @Schema(description = "德语")
    private String deDe;

    @Schema(description = "法语")
    private String frFr;

    @Schema(description = "葡萄牙")
    private String ptPt;

    @Schema(description = "意大利")
    private String itIt;

    @Schema(description = "俄罗斯")
    private String ruRu;

    @Schema(description = "罗马尼亚")
    private String roRo;

    @Schema(description = "阿塞拜疆语")
    private String azAz;

    @Schema(description = "希腊语")
    private String elGr;

    @Schema(description = "芬兰语")
    private String fiFi;

    @Schema(description = "拉脱维亚语")
    private String lvLv;

    @Schema(description = "荷兰语")
    private String nlNl;

    @Schema(description = "丹麦语")
    private String daDk;

    @Schema(description = "塞尔维亚语")
    private String srRs;

    @Schema(description = "波兰语")
    private String plPl;

    @Schema(description = "乌克兰语")
    private String ukUa;

    @Schema(description = "哈萨克斯坦")
    private String kkKz;

    @Schema(description = "缅甸语")
    private String myMm;

    @Schema(description = "韩语")
    private String koKr;

    @Schema(description = "马来语")
    private String msMy;

    @Schema(description = "时间")
    private Date time;

    @Schema(description = "重要文字")
    private Boolean isImp;

    @Schema(description = "是否启用(BOOL):1-启用;0-禁用")
    private Boolean isUsed;

    @Schema(description = "前端启用(BOOL):1-启用;0-禁用")
    private Boolean frontend;

    @Schema(description = "后端启用(BOOL):1-启用;0-禁用")
    private Boolean backend;

    @Schema(description = "服务端启用(BOOL):1-启用;0-禁用")
    private Boolean java;
}
