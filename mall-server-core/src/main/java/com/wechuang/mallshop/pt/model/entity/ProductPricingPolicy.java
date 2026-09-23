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
package com.wechuang.mallshop.pt.model.entity;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 价格策略表-按客户定价
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pt_product_pricing_policy")
@Schema(name = "ProductPricingPolicy对象", description = "价格策略表-按客户定价")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductPricingPolicy implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "policy_id", type = IdType.AUTO)
    private Long policyId;

    @Schema(description = "商品SPU")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "商品")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "所属店铺")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "客户")
    @TableField("customer_id")
    private Integer customerId;

    @Schema(description = "是否启用(BOOL):0-否;1-是")
    @TableField("policy_enable")
    private Boolean policyEnable;

    @Schema(description = "策略价格")
    @TableField("policy_price")
    private BigDecimal policyPrice;

    @Schema(description = "真实折扣率:policy_price/item_unit_price")
    @TableField("policy_discountrate")
    private BigDecimal policyDiscountrate;

    @Schema(description = "起订量")
    @TableField("policy_quantity_min")
    private Integer policyQuantityMin;

    @Schema(description = "限订量")
    @TableField("policy_quantity_max")
    private Integer policyQuantityMax;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private Date updateTime;

}
