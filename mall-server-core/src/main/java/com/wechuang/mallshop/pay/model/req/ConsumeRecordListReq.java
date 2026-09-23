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
 * 交易明细表-账户收支明细-资金流水表-账户金额变化流水
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "交易明细表-账户收支明细-资金流水表-账户金额变化流水分页查询")
public class ConsumeRecordListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "支出收入:1-支出;2-收入")
    @TableField(exist = false)
    private Integer changeType;

    @Schema(description = "支付流水号")
    private Long consumeRecordId;

    @Schema(description = "商户订单编号")
    private String orderId;

    @Schema(description = "所属用编号")
    private Integer userId;

    @Schema(description = "昵称")
    @QueryField(type = QueryType.LIKE)
    private String userNickname;

    @Schema(description = "货币编号")
    private Integer currencyId;

    @Schema(description = "左符号")
    private String currencySymbolLeft;

    @Schema(description = "金额")
    private BigDecimal recordTotal;

    @Schema(description = "金额:record_total-佣金")
    private BigDecimal recordMoney;

    @Schema(description = "佣金:平台佣金针对销售收款")
    private BigDecimal recordCommissionFee;

    @Schema(description = "分销佣金:针对销售收款")
    private BigDecimal recordDistributionCommissionFee;

    @Schema(description = "年-月-日")
    private Date recordDate;

    @Schema(description = "年")
    private Integer recordYear;

    @Schema(description = "月")
    private Integer recordMonth;

    @Schema(description = "日")
    private Integer recordDay;

    @Schema(description = "标题")
    private String recordTitle;

    @Schema(description = "描述")
    private String recordDesc;

    @Schema(description = "支付时间")
    private Long recordTime;

    @Schema(description = "交易类型(ENUM):1201-购物; 1202-转账; 1203-充值; 1204-提现; 1205-销售; 1206-佣金; 1207-退货付款;1208-退货收款;1209-转账收款")
    @QueryField(type = QueryType.IN_STR)
    private String tradeTypeId;

    @Schema(description = "支付方式(ENUM):1301-货到付款; 1302-在线支付; 1303-白条支付; 1304-现金支付; 1305-线下支付; ")
    private Integer paymentTypeId;

    @Schema(description = "支付渠道")
    private Integer paymentChannelId;

    @Schema(description = "所属店铺")
    private Integer storeId;

    @Schema(description = "所属门店")
    private Integer chainId;

    @Schema(description = "消费类型(ENUM):1-余额支付; 2-充值卡支付; 3-积分支付; 4-信用支付; 5-红包支付")
    private Integer paymentMetId;

    @Schema(description = "状态(BOOL):1-已收款;0-作废")
    private Boolean recordEnable;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "支付时间-开始")
    @QueryField(value = "record_time", type = QueryType.GE)
    private Long recordTimeStart;

    @Schema(description = "支付时间-结束")
    @QueryField(value = "record_time", type = QueryType.LE)
    private Long recordTimeEnd;

    public ConsumeRecordListReq() {
        setSidx("consume_record_id");
        setDesc();
    }
}
