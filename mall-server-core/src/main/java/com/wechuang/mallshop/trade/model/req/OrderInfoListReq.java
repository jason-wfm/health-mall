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
package com.wechuang.mallshop.trade.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author Xinze
 * @since 2021-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "订单信息表分页查询")
public class OrderInfoListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "订单编号集合")
    @TableField(exist = false)
    private String orderIds;

    @Schema(description = "订单标题")
    @QueryField(type = QueryType.LIKE)
    private String orderTitle;

    @Schema(description = "卖家店铺编号")
    private Integer storeId;

    @Schema(description = "门店编号")
    private Integer chainId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "买家编号")
    private Integer userId;

    @Schema(description = "订单销售员")
    private Integer orderSaleId;

    @Schema(description = "订单种类(ENUM): 1201-实物 ; 1202-教育类 ; 1203-电子卡券  ; 1204-其它")
    private Integer kindId;

    @Schema(description = "是否自营(ENUM): 1-自营;0-非自营")
    private Boolean storeIsSelfsupport;

    @Schema(description = "订单状态(LIST):2011-待订单审核;2013-待财务审核;2020-待配货/待出库审核;2030-待发货;2040-已发货/待收货确认;2060-已完成/已签收;2070-已取消/已作废;")
    private Integer orderStateId;

    @Schema(description = "买家昵称")
    @QueryField(type = QueryType.LIKE)
    private String userNickname;

    @Schema(description = "支付方式")
    @QueryField(value = "payment_type_id", type = QueryType.EQ)
    private Integer paymentTypeId;

    @Schema(description = "下单时间")
    @QueryField(value = "create_time", type = QueryType.GE)
    private Long orderStime;

    @Schema(description = "下单时间")
    @QueryField(value = "create_time", type = QueryType.LE)
    private Long orderEtime;

    @Schema(description = "订单审批")
    // [healthmall-ext] 列错名修复：原写法无注解且默认 false，BaseQueryWrapper 对非空默认值恒生成
    // `is_review = ?` 条件，而 trade_order_info 实际列名为 order_is_review，导致订单列表接口
    // 存量 BadSqlGrammar(1054)。改为显式 @QueryField 映射正确列名，且去掉默认值（默认不生成条件，
    // 与同组虚拟字段 isReplace/isChain/reviewState 一致；管理端前端不传该参数）。
    @QueryField(value = "order_is_review", type = QueryType.EQ)
    @TableField(exist = false)
    private Boolean isReview;

    @Schema(description = "审批状态(ENUM): 0-全部; 1-待审批; 2-已审批")
    @TableField(exist = false)
    private Integer reviewState;

    @Schema(description = "代客订单")
    @TableField(exist = false)
    private Boolean isReplace;

    @Schema(description = "门店订单")
    @TableField(exist = false)
    private Boolean isChain;

    @Schema(description = "活动类型(DOT)")
    @QueryField(type = QueryType.FIND_IN_SET_STR)
    private String activityTypeId;

    public OrderInfoListReq() {
        this.setSidx("create_time");
        this.setSort("DESC");
    }
}
