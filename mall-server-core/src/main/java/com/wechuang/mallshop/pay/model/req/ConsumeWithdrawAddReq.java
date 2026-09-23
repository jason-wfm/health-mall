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
package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 提现申请表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "提现申请表参数")
public class ConsumeWithdrawAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "所属店铺")
    private Integer storeId;

    @Schema(description = "所属订单(DOT)")
    private String orderId;

    @Schema(description = "退款单号(DOT)")
    private String returnId;

    @Schema(description = "提现额度")
    private BigDecimal withdrawAmount;

    @Schema(description = "提现状态(ENUM):0-申请中;1-提现通过;2-驳回;3-打款完成")
    private Integer withdrawState;

    @Schema(description = "描述")
    private String withdrawDesc;

    @Schema(description = "银行")
    private String withdrawBank;

    @Schema(description = "银行账户")
    private String withdrawAccountNo;

    @Schema(description = "开户名称")
    private String withdrawAccountName;

    @Schema(description = "提现手续费")
    private BigDecimal withdrawFee;

    @Schema(description = "创建时间")
    private Long withdrawTime;

    @Schema(description = "银行流水账号")
    private String withdrawBankflow;

    @Schema(description = "操作管理员")
    private Integer withdrawUserId;

    @Schema(description = "操作时间")
    private Long withdrawOpertime;

    @Schema(description = "联系手机")
    private String withdrawMobile;

    private String withdrawTransState;

    @Schema(description = "提现方式(ENUM):0-余额提现;1-佣金提现")
    private Integer withdrawMode;

    @Schema(description = "开票方式(ENUM):0-先票后款; 1-先款后票; 2-不开票;")
    private Integer withdrawInvoiceMethod;

    @Schema(description = "发票状态(ENUM):0-待上传; 1-已开票; 2-待审核; 3-已作废; 4-不开票;")
    private Integer withdrawInvoiceState;

    @Schema(description = "绑定对应的发票号")
    private String withdrawInvoiceNo;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;


}
