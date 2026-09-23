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
package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账期申请表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户账期申请表参数")
public class UserCreditAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "授信额度")
    private BigDecimal userCreditTotal;

    @Schema(description = "状态:1-未开通；2-已开通；3-冻结;4-关闭;5-驳回；")
    private Integer userCreditStatus;

    @Schema(description = "可用额度")
    private BigDecimal userCredit;

    @Schema(description = "已用额度")
    private BigDecimal userCreditUsed;

    @Schema(description = "还款日")
    private Integer userRepaymentDate;

    @Schema(description = "申请时间")
    private Long applyTime;

    @Schema(description = "更新时间")
    private Long updateTime;

    @Schema(description = "审核操作管理员user_id")
    private Integer operatorUserId;

    @Schema(description = "申请冻结 0否 1是")
    private Boolean isFrozen;

    @Schema(description = "冻结额度")
    private BigDecimal userCreditFrozen;

    @Schema(description = "年审状态: 0-暂无审核，1-待审核；2-审核通过；3-审核失败")
    private Integer userAnnualReview;

    @Schema(description = "企业全称(length=>2,50)")
    private String userCompanyName;

    @Schema(description = "联系人手机号")
    private String userContactsMobile;

    @Schema(description = "联系人")
    private String contactsName;

    @Schema(description = "租户编号")
    private Integer subsiteId;


}
