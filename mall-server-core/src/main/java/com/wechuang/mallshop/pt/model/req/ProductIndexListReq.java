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
package com.wechuang.mallshop.pt.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 产品索引表-不读取数据只读主键
 * </p>
 *
 * @author Xinze
 * @since 2021-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "产品索引表-不读取数据只读主键分页查询")
public class ProductIndexListReq extends BaseListReq {
    private static final long serialVersionUID = 1L;

    @Schema(description = "产品编号:定为SPU编号")
    private Long productId;

    @Schema(description = "SPU商家编码:货号")
    private String productNumber;

    @Schema(description = "产品名称:店铺平台先在对用表中检索后通过id检索,检索使用")
    private String productName;

    @Schema(description = "搜索关键词")
    private String keywords;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "店铺状态(BOOL):0-关闭;1-运营中")
    private Boolean storeIsOpen;

    @Schema(description = "店铺类型(ENUM): 1-卖家店铺; 2-供应商店铺")
    private Integer storeType;

    @Schema(description = "店铺分类编号(DOT)")
    private String storeCategoryIds;

    @Schema(description = "商品分类")
    private Integer categoryId;

    @Schema(description = "课程分类")
    private Integer courseCategoryId;

    @Schema(description = "类型编号:冗余检索")
    private Integer typeId;

    @Schema(description = "商品库存:冗余计算")
    private Integer productQuantity;

    @Schema(description = "预警数量")
    private Integer productWarnQuantity;

    @Schema(description = "品牌编号")
    private String brandId;

    @Schema(description = "售后服务(DOT)")
    private String productServiceTypeIds;

    @Schema(description = "商品状态:1001-正常;1002-下架仓库中;1003-待审核; 1000-违规禁售")
    private Integer productStateId;

    @Schema(description = "销售区域(DOT): district_id=1000全部区域")
    private String productSaleDistrictIds;

    @Schema(description = "商品审核(ENUM):3001-审核通过;3002-审核中;3000-审核未通过")
    private Integer productVerifyId;

    @Schema(description = "是否开票(BOOL): 1-是; 0-否")
    private Boolean productIsInvoices;

    @Schema(description = "是否允许退换货(BOOL): 1-是; 0-否")
    private Boolean productIsReturn;

    @Schema(description = "商品推荐(BOOL):1-是; 0-否")
    private Boolean productIsRecommend;

    @Schema(description = "缺货状态(ENUM):1-有现货;2-预售商品;3-缺货;4-2至3天")
    private Boolean productStockStatus;

    @Schema(description = "商品种类:1201-实物;1202-虚拟")
    private Integer kindId;

    @Schema(description = "参与活动(DOT)")
    private String activityTypeIds;

    @Schema(description = "消费者保障(DOT):由店铺映射到商品")
    private String contractTypeIds;

    @Schema(description = "辅助属性值列(DOT):assist_item_id每个都不用 , setFilter(tagid, array(2,3,4));是表示含有标签值2,3,4中的任意一个即符合筛选，这里是or关系。 setFilter(‘tagid’, array(2)); setFilter(‘tagid’, array(3)); 形成and关系| msyql where FIND_IN_SET('1', product_assist_data) ")
    @TableField(exist = false)
    private String assist;

    //@Schema(description = "辅助属性值列(DOT):assist_item_id每个都不用 , setFilter(tagid, array(2,3,4));是表示含有标签值2,3,4中的任意一个即符合筛选，这里是or关系。 setFilter(‘tagid’, array(2)); setFilter(‘tagid’, array(3)); 形成and关系| msyql where FIND_IN_SET('1', product_assist_data) ")
    //private String productAssistData;

    @Schema(description = "商品单价")
    @QueryField(type = QueryType.GE)
    private BigDecimal productUnitPriceMin;

    @Schema(description = "商品最高单价")
    @QueryField(type = QueryType.LE)
    private BigDecimal productUnitPriceMax;

    @Schema(description = "商品积分")
    @QueryField(type = QueryType.GE)
    private BigDecimal productUnitPointsMin;

    @Schema(description = "商品积分")
    @QueryField(type = QueryType.LE)
    private BigDecimal productUnitPointsMax;

    @Schema(description = "销售量")
    private Integer productSaleNum;

    @Schema(description = "收藏数量人气")
    private Integer productFavoriteNum;

    @Schema(description = "商品点击数量")
    private Integer productClick;

    @Schema(description = "评价次数")
    private Integer productEvaluationNum;

    @Schema(description = "所属区域(DOT)")
    private String productRegionDistrictIds;

    @Schema(description = "运费:包邮为0，检索使用")
    private BigDecimal productFreight;

    @Schema(description = "商品标签(DOT)")
    private String productTags;

    @Schema(description = "是否自营(BOOL):1-自营;0-非自营")
    private Boolean storeIsSelfsupport;

    @Schema(description = "允许分销(BOOL):1-启用分销;0-禁用分销")
    private Boolean productSpEnable;

    @Schema(description = "三级分销允许分销(BOOL):1-启用分销;0-禁用分销")
    private Boolean productDistEnable;

    @Schema(description = "添加时间")
    private Long productAddTime;

    @Schema(description = "上架时间:预设上架时间,可以动态修正状态")
    private Long productSaleTime;

    @Schema(description = "排序:越小越靠前")
    private Integer productOrder;

    @Schema(description = "产品来源编号")
    private Long productSrcId;

    @Schema(description = "所属商圈(DOT)")
    private String marketCategoryId;

    @Schema(description = "纬度")
    private Double storeLatitude;

    @Schema(description = "经度")
    private Double storeLongitude;

    @Schema(description = "是否视频(BOOL):1-有视频;0-无视频")
    private Boolean productIsVideo;

    @Schema(description = "配送服务(ENUM):1001-快递发货;1002-到店自提;1003-上门服务")
    private String productTransportId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "分站")
    @TableField(exist = false)
    private Integer siteId = 0;

    @Schema(description = "商品编号(DOT)")
    private String itemIds;

    @Schema(description = "行业编号集合")
    private String industryIds;

    @Schema(description = "是否参与PLUS折扣(BOOL): 1-是; 0-否")
    private Boolean productPlusEnable;

}
