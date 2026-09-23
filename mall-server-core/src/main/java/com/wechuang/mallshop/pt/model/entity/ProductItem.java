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
import com.wechuang.mallshop.marketing.model.vo.ActivityInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品SKU表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pt_product_item")
@Schema(name = "ProductItem对象", description = "商品SKU表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品编号-SKU编号")
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    @Schema(description = "副标题(DOT):SKU名称")
    @TableField("item_name")
    private String itemName;

    @Schema(description = "产品编号")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "颜色SKU，规格值")
    @TableField("color_id")
    private Long colorId;

    @Schema(description = "是否为默认展示的商品，必须为item_enable")
    @TableField("item_is_default")
    private Boolean itemIsDefault;

    @Schema(description = "SKU商家编码:SKU商家编码为非必填项，若不填写，系统会自动生成一个SKU商家编码。")
    @TableField("item_number")
    private String itemNumber;

    @Schema(description = "条形码")
    @TableField("item_barcode")
    private String itemBarcode;

    @Schema(description = "包装规格")
    @TableField("item_pack_spec")
    private String itemPackSpec;

    @Schema(description = "成本价")
    @TableField("item_cost_price")
    private BigDecimal itemCostPrice;

    @Schema(description = "商品价格")
    @TableField("item_unit_price")
    private BigDecimal itemUnitPrice;

    @Schema(description = "市场价")
    @TableField("item_market_price")
    private BigDecimal itemMarketPrice;

    @Schema(description = "积分价格")
    @TableField("item_unit_points")
    private BigDecimal itemUnitPoints = BigDecimal.ZERO;

    @Schema(description = "商品库存")
    @TableField("item_quantity")
    private Integer itemQuantity;

    @Schema(description = "商品冻结库存")
    @TableField("item_quantity_frozen")
    private Integer itemQuantityFrozen;

    @Schema(description = "库存预警值")
    @TableField("item_warn_quantity")
    private Integer itemWarnQuantity;

    @Schema(description = "商品规格序列化(JSON):{spec_id:spec_item_id, spec_id:spec_item_id, spec_id:spec_item_id}")
    @TableField("item_spec")
    private String itemSpec;

    @Schema(description = "商品规格值编号")
    @TableField("spec_item_ids")
    private String specItemIds;

    @Schema(description = "是否启用(LIST):1001-正常;1002-下架仓库中;1000-违规禁售")
    @TableField("item_enable")
    private Integer itemEnable;

    @Schema(description = "被改动(BOOL):0-未改动;1-已改动分销使用")
    @TableField("item_is_change")
    private Boolean itemIsChange;

    @Schema(description = "商品重量:KG")
    @TableField("item_weight")
    private BigDecimal itemWeight;

    @Schema(description = "商品体积:立方米")
    @TableField("item_volume")
    private BigDecimal itemVolume;

    @Schema(description = "微小店分销佣金")
    @TableField("item_fx_commission")
    private BigDecimal itemFxCommission;

    @Schema(description = "返利额度")
    @TableField("item_rebate")
    private BigDecimal itemRebate;

    @Schema(description = "供应商SKU编号")
    @TableField("item_src_id")
    private Long itemSrcId;

    @Schema(description = "商品分类")
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "课程分类")
    @TableField("course_category_id")
    private Integer courseCategoryId;

    @Schema(description = "所属店铺")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "商品会员等级折扣(JSON)")
    @TableField("item_level_discount")
    private String itemLevelDiscount;

    @Schema(description = "可用库存")
    @TableField(exist = false)
    private Integer availableQuantity;

    @Schema(description = "Spec名称")
    @TableField(exist = false)
    private String productItemName;

    @Schema(description = "当前使用活动编号")
    @TableField(exist = false)
    private Integer activityId;

    @Schema(description = "商品描述")
    @TableField(exist = false)
    private String productDetail;


    @Schema(description = "活动信息")
    @TableField(exist = false)
    private ActivityInfoVo activityInfo;

    @Schema(description = "商品销售价")
    @TableField(exist = false)
    private BigDecimal itemSalePrice;

    @Schema(description = "节省单价")
    @TableField(exist = false)
    private BigDecimal itemSavePrice;

    @Schema(description = "标签名称(DOT)")
    @TableField(exist = false)
    private List<String> productTagNames = new ArrayList<>();

    @Schema(description = "商品卖点:商品广告词")
    @TableField(exist = false)
    private String productTips;

    public Integer getAvailableQuantity() {
        return itemQuantity - itemQuantityFrozen;
    }


    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "会员专属价")
    @TableField(exist = false)
    private BigDecimal itemPolicyPrice;

    @Schema(description = "是否启用PLUS折扣")
    @TableField(exist = false)
    private Boolean productPlusEnable;

    @Schema(description = "PLUS单品优惠金额")
    @TableField(exist = false)
    private BigDecimal itemPlusDiscountPrice;

    @Schema(description = "PLUS优惠金额")
    @TableField(exist = false)
    private BigDecimal itemPlusDiscountAmount;

    @Schema(description = "PLUS到手价")
    @TableField(exist = false)
    private BigDecimal itemPlusPrice;

    @Schema(description = "会员抵扣金额")
    @TableField(exist = false)
    private BigDecimal itemLevelDiscountPrice;

    @Schema(description = "会员抵扣总金额")
    @TableField(exist = false)
    private BigDecimal itemLevelDiscountAmount;
}
