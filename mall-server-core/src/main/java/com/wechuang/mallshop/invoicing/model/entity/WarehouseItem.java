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
package com.wechuang.mallshop.invoicing.model.entity;

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

/**
 * <p>
 * 商品SKU存放详情表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("invoicing_warehouse_item")
@Schema(name = "WarehouseItem对象", description = "商品SKU存放详情表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WarehouseItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "仓库商品编号")
    @TableId(value = "warehouse_item_id", type = IdType.AUTO)
    private Long warehouseItemId;

    @Schema(description = "商品编号（冗余）")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "商品单位id（冗余）")
    @TableField("unit_id")
    private Integer unitId;

    @Schema(description = "SKU")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "所属分类")
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "仓库编号")
    @TableField("warehouse_id")
    private Integer warehouseId;

    @Schema(description = "成本")
    @TableField("warehouse_item_cost")
    private BigDecimal warehouseItemCost;

    @Schema(description = "数量")
    @TableField("warehouse_item_quantity")
    private Integer warehouseItemQuantity;

    @Schema(description = "锁定库存:实际库存-锁定库存=可售库存	")
    @TableField("warehouse_item_lock_quantity")
    private Integer warehouseItemLockQuantity;

    @Schema(description = "最小库存")
    @TableField("warehouse_item_quantity_min")
    private Integer warehouseItemQuantityMin;

    @Schema(description = "最大库存")
    @TableField("warehouse_item_quantity_max")
    private Integer warehouseItemQuantityMax;

    @Schema(description = "所属店铺")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
