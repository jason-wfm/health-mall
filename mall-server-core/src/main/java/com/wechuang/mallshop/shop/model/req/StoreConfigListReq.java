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
package com.wechuang.mallshop.shop.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 店铺参数设置表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "店铺参数设置表分页查询")
public class StoreConfigListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "销售员购买权限(BOOL):0-关闭;1-开启,购买权限开启状态下，销售员自己购买的订单将会算入业绩")
    private Boolean scAllowSellerBuy;

    @Schema(description = "分销申请消费额")
    private Integer scExpenditure;

    @Schema(description = "一级佣金比例")
    private Float scCpsRate;

    @Schema(description = "二级销售(BOOL): 0-关闭; 1-开启")
    private Boolean scSecondIsEnable;

    @Schema(description = "二级佣金比例")
    private Float scSecondCpsRate;

    @Schema(description = "三级分佣比例")
    private Float scThirdCpsRate;

    @Schema(description = "客户关系 期限， 销售员带来的客户（成为店铺的消费者开始计算时间）超过一定期限后，则不再享受分佣。 消费者在店铺消费第一单时间后，在某个期限内消费才可以产生佣金。 ")
    private Integer scDirectsellerCustomerExptime;

    @Schema(description = "客户有效期(ENUM):1-永久，建立客户关系,客户以后在店铺的购买都分佣; 2-短期，只根据链接购买获取佣金， 且一定期限后，链接失效。 不需要建立客户关系")
    private Boolean scDirectsellerExptimeType;

    @Schema(description = "客户关系保护期 - 带来的客户关系在一定期限内不给抢走， 其它销售可以通过购买链接生效，但是在保护期内部更改关系")
    private Integer scDirectsellerRelExptime;

    @Schema(description = "销售员审核(BOOL):0-不需要审核; 1-需要审核")
    private Boolean scIsVerify;

    @Schema(description = "结算方式(BOOL):0-手动结算 ;1-自动结算")
    private Boolean scAutoSettle;

    @Schema(description = "结算时间")
    private Integer scSettleTimeType;

    @Schema(description = "启用商品起订量")
    private Boolean scIsUseMinQuantity;

    @Schema(description = "启用商品限购量")
    private Boolean scIsUseMaxQuantity;

    @Schema(description = "启用商品品牌")
    private Boolean scIsUseProductBrand;

    @Schema(description = "启用商品重量字段")
    private Boolean scIsUseProductWeight;

    @Schema(description = "启用商品自定义字段")
    private Boolean scIsUseProductDefined;

    @Schema(description = "商品标签管理(JSON)")
    private String scProductTags;

    @Schema(description = "交货日期必填")
    private Date scIsNecessaryDeliveryDate;

    @Schema(description = "启用最低下单金额限制(BOOL):0-关闭;1-启用")
    private Boolean scIsUseOrderMinMoney;

    @Schema(description = "启用最低下单金额限制")
    private BigDecimal scOrderMinMoney;

    @Schema(description = "订货仓库限制(ENUM):1-按总仓库订购商品,不限制仓库定向供货指定的客户;2-按客户对应仓库订购商品,不限制仓库定向供货指定的客户")
    private Boolean scDeductInventoryMode;

    @Schema(description = "客户订货系统(ENUM):0-不显示库存,客户订货无库存功能;1-显示存库有无客户而在商品下单时显示商品库存有无，但不显示具体数量，未填库存信息显示无库存信息;2-显示存库数量客户订货系统可在商品、下单时显示商品库存数量，未填写库存信息显示无库存信息")
    private Boolean scInventoryAgentShowType;

    @Schema(description = "商品库存扣减预购值锁定(ENUM):1-下单扣减锁定;2-支付扣减锁定,数量小于等于零，禁止订货")
    private Boolean scIsEnabledInventoryCheck;

    @Schema(description = "客户资金账户(JSON):[{\"baseId\":1,\"name\":\"预付款账户\",\"code\":\"0001\",\"can_pay\":\"1\",\"canRecharge\":\"1\",\"enableStatus\":\"1\",\"autoAuditFlag\":\"1\"},{\"baseId\":2,\"name\":\"返点账户\",\"code\":\"0002\",\"can_pay\":\"1\",\"canRecharge\":\"0\",\"enableStatus\":\"1\",\"autoAuditFlag\":\"1\"},{\"baseId\":3,\"name\":\"保证金账户\",\"code\":\"0003\",\"can_pay\":\"0\",\"canRecharge\":\"1\",\"enableStatus\":\"1\",\"autoAuditFlag\":\"0\"}]")
    private String scFundAccountSettings;

    @Schema(description = "订货流程设置(DOT)")
    private String scOrderProcess;

    @Schema(description = "退货流程设置(DOT)")
    private String scOrderReturnProcess;

    @Schema(description = "启用客户商品分享(BOOL):1-启用;0-禁用")
    private Boolean scIsEnabledShare;

    @Schema(description = "启用进销存管理(BOOL):1-启用;0-禁用")
    private Boolean scIsEnabledInvoicing;

    @Schema(description = "结算周期:30为整月")
    private Integer scSettleCircle;

    @Schema(description = "上次结算时间")
    private Long scSettleLastTime;

    @Schema(description = "下次结算时间")
    private Long scSettleNextTime;

    @Schema(description = "消费送积分比率")
    private BigDecimal scPointsConsumeRate;

    @Schema(description = "跑腿-起送距离")
    private BigDecimal scStartDistance;

    @Schema(description = "跑腿-起送金额")
    private BigDecimal scStartAmount;

    @Schema(description = "跑腿-超出起送距离外每公里金额")
    private BigDecimal scExtraAmount;

    @Schema(description = "节日优惠-活动名称")
    private String scFestivalName;

    @Schema(description = "节日优惠-开始时间")
    private String scFestivalStart;

    @Schema(description = "节日优惠-结束时间")
    private String scFestivalEnd;

    @Schema(description = "节日优惠-金额上限")
    private BigDecimal scFestivalAmountUpper;

    @Schema(description = "节日优惠-节日下限")
    private BigDecimal scFestivalAmountDown;

    @Schema(description = "节日优惠-每日浮动比例")
    private BigDecimal scFestivalFloatProportion;

    @Schema(description = "节日优惠-浮动方式")
    private Boolean scFestivalFloatType;

    @Schema(description = "店铺免运费(BOOL):0-关闭 ;1-开启")
    private Boolean scIsFreeFreight;

    @Schema(description = "采集默认分类")
    private Integer scCrawlCategoryId;

    @Schema(description = "是否开票(BOOL):1-启用;0-禁用")
    private Boolean scIsEnabledInvoice;


}
