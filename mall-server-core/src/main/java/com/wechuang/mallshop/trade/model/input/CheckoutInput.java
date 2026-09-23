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
package com.wechuang.mallshop.trade.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.trade.model.vo.CheckoutItemVo;
import com.wechuang.mallshop.trade.model.vo.FixOrderVo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "结算预览")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckoutInput implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "SKU信息")
    List<CheckoutItemVo> items;

    @Schema(description = "买家编号")
    private Integer userId;

    @Schema(description = "订单销售员")
    private Integer orderSaleId;

    @Schema(description = "买家昵称")
    private String userNickname;

    @Schema(description = "地址编号 或者 地址数据Map")
    private Integer udId;

    @Schema(description = "下单门店")
    Integer chainId = 0;

    @Schema(description = "活动编号")
    private Integer activityId = 0;

    @Schema(description = "拼团编号")
    private Integer gbId = 0;

    @Schema(description = "配送方式:5-自提;10-物流配送")
    private Integer deliveryTypeId;

    @Schema(description = "付款方式")
    private Integer paymentTypeId;


    @Schema(description = "消息")
    private Map<Integer, String> message;

    @Schema(description = "优惠券")
    private List<Integer> userVoucherIds;

    @Schema(description = "发票")
    private Map<Integer, Integer> invoice;


    //生成参数
    @Schema(description = "订单类型")
    Integer orderType = StateCode.ORDER_TYPE_DD;

    @Schema(description = "是否需要计算运费")
    private Boolean calFreight = true;

    @Schema(description = "供应商转单源订单")
    private String srcOrderId;

    @Schema(description = "自提日期")
    private Date virtualServiceDate;

    @Schema(description = "自提日期")
    private Date virtualServiceTime;

    @Schema(description = "快速修正订单传输对象")
    private FixOrderVo fixOrder;
}
