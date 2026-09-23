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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品基础表-SPU表
 * </p>
 *
 * @author Xinze
 * @since 2021-03-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商品基础表-SPU表参数")
public class ProductBaseAddReq implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Schema(description = "SPU商家编码:货号")
    @JsonProperty("product_number")
    private String productNumber;

    @Schema(description = "产品名称- 必填")
    @JsonProperty("product_name")
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    @Schema(description = "商品卖点:商品广告词")
    @JsonProperty("product_tips")
    private String productTips;

    @Schema(description = "商品主图")
    @JsonProperty("product_image")
    private String productImage;

    @Schema(description = "产品视频")
    @JsonProperty("product_video")
    private String productVideo;

    @Schema(description = "选择售卖区域:完成售卖区域及运费设置")
    @JsonProperty("transport_type_id")
    private Integer transportTypeId;

    @Schema(description = "每人限购")
    @JsonProperty("product_buy_limit")
    private Integer productBuyLimit;

    @Schema(description = "平台佣金比率")
    @JsonProperty("product_commission_rate")
    private BigDecimal productCommissionRate;

    @Schema(description = "起订量")
    @JsonProperty("product_minimum_order")
    private Integer productMinimumOrder;

    @Schema(description = "产品文件")
    @JsonProperty("product_file")
    private String productFile;

    //index
    @Schema(description = "产品编号:定为SPU编号")
    @JsonProperty("product_id")
    private Long productId;

    @Schema(description = "商品分类")
    @JsonProperty("category_id")
    @NotBlank(message = "商品分类不能为空")
    private Integer categoryId;

    @Schema(description = "品牌编号")
    @JsonProperty("brand_id")
    private Integer brandId;

    @Schema(description = "售后服务(DOT)")
    @JsonProperty("product_service_type_ids")
    private String productServiceTypeIds;

    @Schema(description = "商品状态:1001-正常;1002-下架仓库中;1003-待审核; 1000-违规禁售")
    @JsonProperty("product_state_id")
    private Integer productStateId;

    @Schema(description = "销售区域(DOT): district_id=1000全部区域")
    @JsonProperty("product_sale_district_ids")
    private String productSaleDistrictIds;

    @Schema(description = "商品种类:1201-实物;1202-虚拟")
    @JsonProperty("kind_id")
    @NotBlank(message = "商品种类不能为空")
    private Integer kindId;

    @Schema(description = "消费者保障(DOT):由店铺映射到商品")
    @JsonProperty("contract_type_ids")
    private List<Integer> contractTypeIds;

    @Schema(description = "所属区域(DOT)")
    @JsonProperty("product_region_district_ids")
    private String productRegionDistrictIds;

    @Schema(description = "商品标签(DOT)")
    @JsonProperty("product_tags")
    private String productTags;

    @Schema(description = "允许分销(BOOL):1-启用分销;0-禁用分销")
    @JsonProperty("product_sp_enable")
    private Boolean productSpEnable;

    @Schema(description = "三级分销允许分销(BOOL):1-启用分销;0-禁用分销")
    @JsonProperty("product_dist_enable")
    private Boolean productDistEnable;

    @Schema(description = "上架时间:预设上架时间,可以动态修正状态")
    @JsonProperty("product_sale_time")
    private Long productSaleTime;

    @Schema(description = "所属商圈(DOT)")
    @JsonProperty("market_category_id")
    private String marketCategoryId;

    @Schema(description = "配送服务(ENUM):1001-快递发货;1002-到店自提;1003-上门服务")
    @JsonProperty("product_transport_id")
    private List<Integer> productTransportId;

    @Schema(description = "库存锁定(ENUM):1001-下单锁定;1002-支付锁定;")
    @JsonProperty("product_inventory_lock")
    @NotNull(message = "库存锁定方式不能为空")
    private Integer productInventoryLock;

    // info
    @Schema(description = "属性(JSON) - 辅助属性及VAL")
    @JsonProperty("product_assist")
    private String productAssist;

    @Schema(description = "规格(JSON)-规格、规格值、goods_id  规格不需要全选就可以添加对应数据")
    @JsonProperty("product_spec")
    private String productSpec;

    @Schema(description = "商品描述")
    @JsonProperty("product_detail")
    private String productDetail;

    @Schema(description = "Meta Tag 标题")
    @JsonProperty("product_meta_title")
    private String productMetaTitle;

    @Schema(description = "Meta Tag 描述")
    @JsonProperty("product_meta_description")
    private String productMetaDescription;

    @Schema(description = "Meta Tag 关键字")
    @JsonProperty("product_meta_keyword")
    private String productMetaKeyword;

    //虚拟
    @Schema(description = "有效期:1001-长期有效;1002-自定义有效期;1003-购买起有效时长年单位")
    @JsonProperty("product_valid_period")
    private Integer productValidPeriod;

    @Schema(description = "开始时间")
    @JsonProperty("product_validity_start")
    private Long productValidityStart;

    @Schema(description = "失效时间")
    @JsonProperty("product_validity_end")
    private Long productValidityEnd;

    @Schema(description = "以天为单位")
    @JsonProperty("product_validity_duration")
    private Integer productValidityDuration;

    @Schema(description = "服务类型(ENUM):1001-到店服务;1002-上门服务")
    @JsonProperty("product_valid_type")
    private Integer productValidType;

    @Schema(description = "填写预约日期(BOOL):0-否;1-是")
    @JsonProperty("product_service_date_flag")
    private Boolean productServiceDateFlag;

    @Schema(description = "填写联系人(BOOL):0-否;1-是")
    @JsonProperty("product_service_contactor_flag")
    private Boolean productServiceContactorFlag;

    @Schema(description = "支持过期退款(BOOL):0-否;1-是")
    @JsonProperty("product_valid_refund_flag")
    private Boolean productValidRefundFlag;
}
