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

/**
 * <p>
 * 产品索引表-不读取数据只读主键
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pt_product_index")
@Schema(name = "ProductIndex对象", description = "产品索引表-不读取数据只读主键")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductIndex implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "产品编号:定为SPU编号")
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    @Schema(description = "SPU商家编码:货号")
    @TableField("product_number")
    private String productNumber;

    @Schema(description = "产品名称 - 必填")
    @TableField("product_name")
    private String productName;

    @Schema(description = "名称索引关键字(DOT)")
    @TableField("product_name_index")
    private String productNameIndex;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "店铺状态(BOOL):0-关闭;1-运营中")
    @TableField("store_is_open")
    private Boolean storeIsOpen;

    @Schema(description = "店铺类型(ENUM): 1-卖家店铺; 2-供应商店铺")
    @TableField("store_type")
    private Integer storeType;

    @Schema(description = "店铺分类(DOT)")
    @TableField("store_category_ids")
    private String storeCategoryIds;

    @Schema(description = "商品分类 - 必填")
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "课程分类")
    @TableField("course_category_id")
    private Integer courseCategoryId;

    @Schema(description = "类型编号:冗余检索")
    @TableField("type_id")
    private Integer typeId;

    @Schema(description = "商品库存:冗余计算")
    @TableField("product_quantity")
    private Integer productQuantity;

    @Schema(description = "预警数量")
    @TableField("product_warn_quantity")
    private Integer productWarnQuantity;

    @Schema(description = "品牌编号")
    @TableField("brand_id")
    private Integer brandId;

    @Schema(description = "售后服务(DOT)")
    @TableField("product_service_type_ids")
    private String productServiceTypeIds;

    @Schema(description = "商品状态(ENUM):1001-正常;1002-下架仓库中;1003-待审核; 1000-违规禁售")
    @TableField("product_state_id")
    private Integer productStateId;

    @Schema(description = "违规备注")
    @TableField("product_state_remark")
    private String productStateRemark;

    @Schema(description = "销售区域(DOT): district_id=1000全部区域")
    @TableField("product_sale_district_ids")
    private String productSaleDistrictIds;

    @Schema(description = "商品审核(ENUM):3001-审核通过;3002-审核中;3000-审核未通过")
    @TableField("product_verify_id")
    private Integer productVerifyId;

    @Schema(description = "审核备注")
    @TableField("product_verify_remark")
    private String productVerifyRemark;

    @Schema(description = "是否开票(BOOL): 1-是; 0-否")
    @TableField("product_is_invoices")
    private Boolean productIsInvoices;

    @Schema(description = "允许退换货(BOOL): 1-是; 0-否")
    @TableField("product_is_return")
    private Boolean productIsReturn;

    @Schema(description = "商品推荐(BOOL):1-是; 0-否")
    @TableField("product_is_recommend")
    private Boolean productIsRecommend;

    @Schema(description = "缺货状态(ENUM):1-有现货;2-预售商品;3-缺货;4-2至3天")
    @TableField("product_stock_status")
    private Boolean productStockStatus;

    @Schema(description = "商品种类:1201-实物;1202-虚拟 - 必填")
    @TableField("kind_id")
    private Integer kindId;

    @Schema(description = "参与活动(DOT)")
    @TableField("activity_type_ids")
    private String activityTypeIds;

    @Schema(description = "消费者保障(DOT):由店铺映射到商品")
    @TableField("contract_type_ids")
    private String contractTypeIds;

    @Schema(description = "辅助属性值列(DOT):assist_item_id每个都不用 , setFilter(tagid, array(2,3,4));是表示含有标签值2,3,4中的任意一个即符合筛选，这里是or关系。 setFilter(‘tagid’, array(2)); setFilter(‘tagid’, array(3)); 形成and关系| msyql where FIND_IN_SET('1', product_assist_data) ")
    @TableField("product_assist_data")
    private String productAssistData;

    @Schema(description = "最低单价")
    @TableField("product_unit_price_min")
    private BigDecimal productUnitPriceMin;

    @Schema(description = "最高单价")
    @TableField("product_unit_price_max")
    private BigDecimal productUnitPriceMax;

    @Schema(description = "商品积分")
    @TableField("product_unit_points_min")
    private BigDecimal productUnitPointsMin;

    @Schema(description = "商品积分")
    @TableField("product_unit_points_max")
    private BigDecimal productUnitPointsMax;

    @Schema(description = "销售数量")
    @TableField("product_sale_num")
    private Integer productSaleNum;

    @Schema(description = "收藏数量")
    @TableField("product_favorite_num")
    private Integer productFavoriteNum;

    @Schema(description = "点击数量")
    @TableField("product_click")
    private Integer productClick;

    @Schema(description = "评价次数")
    @TableField("product_evaluation_num")
    private Integer productEvaluationNum = 0;

    @Schema(description = "所属区域(DOT)")
    @TableField("product_region_district_ids")
    private String productRegionDistrictIds;

    @Schema(description = "运费:包邮为0")
    @TableField("product_freight")
    private BigDecimal productFreight;

    @Schema(description = "商品标签(DOT)")
    @TableField("product_tags")
    private String productTags;

    @Schema(description = "是否自营(BOOL):1-自营;0-非自营")
    @TableField("store_is_selfsupport")
    private Boolean storeIsSelfsupport;

    @Schema(description = "允许分销(BOOL):1-启用分销;0-禁用分销")
    @TableField("product_sp_enable")
    private Boolean productSpEnable;

    @Schema(description = "三级分销允许分销(BOOL):1-启用分销;0-禁用分销")
    @TableField("product_dist_enable")
    private Boolean productDistEnable;

    @Schema(description = "添加时间")
    @TableField("product_add_time")
    private Long productAddTime;

    @Schema(description = "上架时间:预设上架时间,可以动态修正状态")
    @TableField("product_sale_time")
    private Long productSaleTime;

    @Schema(description = "排序:越小越靠前")
    @TableField("product_order")
    private Integer productOrder;

    @Schema(description = "产品来源编号")
    @TableField("product_src_id")
    private Long productSrcId;

    @Schema(description = "所属商圈(DOT)")
    @TableField("market_category_id")
    private String marketCategoryId;

    @Schema(description = "纬度")
    @TableField("store_latitude")
    private Double storeLatitude;

    @Schema(description = "经度")
    @TableField("store_longitude")
    private Double storeLongitude;

    @Schema(description = "是否视频(BOOL):1-有视频;0-无视频")
    @TableField("product_is_video")
    private Boolean productIsVideo;

    @Schema(description = "配送服务(ENUM):1001-快递发货;1002-到店自提;1003-上门服务")
    @TableField("product_transport_id")
    private String productTransportId;

    @Schema(description = "是否锁定(BOOL):0-未锁定; 1-锁定,参加团购的商品不予许修改")
    @TableField("product_is_lock")
    private Boolean productIsLock;

    @Schema(description = "库存锁定(ENUM):1001-下单锁定;1002-支付锁定;")
    @TableField("product_inventory_lock")
    private Integer productInventoryLock;

    @Schema(description = "商品来源(ENUM):1000-发布;1001-天猫;1002-淘宝;1003-阿里巴巴;1004-京东;")
    @TableField("product_from")
    private Integer productFrom;

    @Schema(description = "行业编号集合")
    @TableField(value = "industry_ids")
    private String industryIds;

    @Schema(description = "等级会员(ENUM):1-默认；2-自定义")
    @TableField("product_level_membership")
    private Integer productLevelMembership;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "是否启用PLUS折扣")
    @TableField("product_plus_enable")
    private Boolean productPlusEnable;

}
