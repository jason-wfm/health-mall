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
package com.wechuang.mallshop.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_lang_standard")
@Schema(name = "LangStandard对象", description = "语言翻译表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LangStandard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "中文")
    @TableId(value = "zh_CN", type = IdType.AUTO)
    private String zhCN;

    @Schema(description = "繁体中文")
    @TableField("zh_TW")
    private String zhTW;

    @Schema(description = "英文")
    @TableField("en_GB")
    private String enGB;

    @Schema(description = "泰语")
    @TableField("th_TH")
    private String thTH;

    @Schema(description = "西班牙")
    @TableField("es_MX")
    private String esMX;

    @Schema(description = "阿拉伯")
    @TableField("ar_SA")
    private String arSA;

    @Schema(description = "越南语")
    @TableField("vi_VN")
    private String viVN;

    @Schema(description = "土耳其")
    @TableField("tr_TR")
    private String trTR;

    @Schema(description = "日语")
    @TableField("ja_JP")
    private String jaJP;

    @Schema(description = "印尼语")
    @TableField("id_ID")
    private String idID;

    @Schema(description = "德语")
    @TableField("de_DE")
    private String deDE;

    @Schema(description = "法语")
    @TableField("fr_FR")
    private String frFR;

    @Schema(description = "葡萄牙")
    @TableField("pt_PT")
    private String ptPT;

    @Schema(description = "意大利")
    @TableField("it_IT")
    private String itIT;

    @Schema(description = "俄罗斯")
    @TableField("ru_RU")
    private String ruRU;

    @Schema(description = "罗马尼亚")
    @TableField("ro_RO")
    private String roRO;

    @Schema(description = "阿塞拜疆语")
    @TableField("az_AZ")
    private String azAZ;

    @Schema(description = "希腊语")
    @TableField("el_GR")
    private String elGR;

    @Schema(description = "芬兰语")
    @TableField("fi_FI")
    private String fiFI;

    @Schema(description = "拉脱维亚语")
    @TableField("lv_LV")
    private String lvLV;

    @Schema(description = "荷兰语")
    @TableField("nl_NL")
    private String nlNL;

    @Schema(description = "丹麦语")
    @TableField("da_DK")
    private String daDK;

    @Schema(description = "塞尔维亚语")
    @TableField("sr_RS")
    private String srRS;

    @Schema(description = "波兰语")
    @TableField("pl_PL")
    private String plPL;

    @Schema(description = "乌克兰语")
    @TableField("uk_UA")
    private String ukUA;

    @Schema(description = "哈萨克斯坦")
    @TableField("kk_KZ")
    private String kkKZ;

    @Schema(description = "缅甸语")
    @TableField("my_MM")
    private String myMM;

    @Schema(description = "韩语")
    @TableField("ko_KR")
    private String koKR;

    @Schema(description = "马来语")
    @TableField("ms_MY")
    private String msMY;

    @Schema(description = "时间")
    @TableField("time")
    private Date time;

    @Schema(description = "重要文字")
    @TableField("is_imp")
    private Boolean isImp;

    @Schema(description = "是否启用(BOOL):1-启用;0-禁用")
    @TableField("is_used")
    private Boolean isUsed;

    @Schema(description = "前端启用(BOOL):1-启用;0-禁用")
    @TableField("frontend")
    private Boolean frontend;

    @Schema(description = "后端启用(BOOL):1-启用;0-禁用")
    @TableField("backend")
    private Boolean backend;

    @Schema(description = "服务端启用(BOOL):1-启用;0-禁用")
    @TableField("java")
    private Boolean java;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
