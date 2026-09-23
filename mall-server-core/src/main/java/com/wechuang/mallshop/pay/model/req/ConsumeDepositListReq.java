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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 支付表-支付回调callback使用-确认付款
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付表-支付回调callback使用-确认付款分页查询")
public class ConsumeDepositListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "支付流水号")
    private Long depositId;

    @Schema(description = "商城支付编号")
    private String depositNo;

    @Schema(description = "交易号:支付宝etc")
    private String depositTradeNo;

    @Schema(description = "商户网站唯一订单号(DOT):合并支付则为多个订单号, 没有创建联合支付交易号")
    private String orderId;

    @Schema(description = "支付渠道")
    private Integer paymentChannelId;

    @Schema(description = "商品名称")
    @QueryField(type = QueryType.LIKE)
    private String depositSubject;

    @Schema(description = "支付方式(ENUM):1301-货到付款; 1302-在线支付; 1303-白条支付; 1304-现金支付; 1305-线下支付; ")
    private Integer depositPaymentType;

    @Schema(description = "交易状态")
    private String depositTradeStatus;

    @Schema(description = "卖家户号:支付宝etc")
    private String depositSellerId;

    @Schema(description = "卖家支付账号")
    private String depositSellerEmail;

    @Schema(description = "买家支付用户号")
    private String depositBuyerId;

    @Schema(description = "买家支付账号")
    private String depositBuyerEmail;

    @Schema(description = "货币编号")
    private Integer currencyId;

    @Schema(description = "左符号")
    private String currencySymbolLeft;

    @Schema(description = "交易金额")
    private BigDecimal depositTotalFee;

    @Schema(description = "购买数量")
    private Integer depositQuantity;

    @Schema(description = "商品单价")
    private BigDecimal depositPrice;

    @Schema(description = "商品描述")
    private String depositBody;

    @Schema(description = "交易创建时间")
    private Date depositGmtCreate;

    @Schema(description = "交易付款时间")
    private Date depositGmtPayment;

    private Date depositGmtClose;

    @Schema(description = "是否调整总价")
    private Boolean depositIsTotalFeeAdjust;

    @Schema(description = "是否使用红包买家")
    private Boolean depositUseCoupon;

    @Schema(description = "折扣")
    private BigDecimal depositDiscount;

    @Schema(description = "通知时间")
    private Long depositNotifyTime;

    @Schema(description = "通知类型")
    private String depositNotifyType;

    @Schema(description = "通知校验编号")
    private String depositNotifyId;

    @Schema(description = "签名方式")
    private String depositSignType;

    @Schema(description = "签名")
    private String depositSign;

    @Schema(description = "额外参数")
    private String depositExtraParam;

    @Schema(description = "支付")
    private String depositService;

    @Schema(description = "支付状态:0-默认; 1-接收正确数据处理完逻辑; 9-异常订单")
    private Integer depositState;

    @Schema(description = "是否同步(BOOL):0-同步; 1-异步回调使用")
    private Boolean depositAsync;

    @Schema(description = "收款确认(ENUM):0-未确认;1-已确认;2-驳回")
    private Integer depositReview;

    @Schema(description = "是否作废(BOOL):1-正常; 2-作废")
    private Boolean depositEnable;

    @Schema(description = "所属店铺:直接交易起作用")
    private Integer storeId;

    @Schema(description = "所属用户")
    private Integer userId;

    @Schema(description = "所属门店:直接交易起作用")
    private Integer chainId;

    @Schema(description = "所属分站:直接交易起作用")
    private Integer subsiteId;

    @Schema(description = "支付时间")
    private Long depositTime;

    @Schema(description = "是否线下:1-是; 2-否")
    @TableField(exist = false)
    private Integer isOffline;

    public ConsumeDepositListReq() {
        setSidx("deposit_id");
        setDesc();
    }
}
