package com.wechuang.mallshop.pt.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商品下单使用", description = "商品下单使用")
public class ProductItemVo extends ProductItem {

    @Schema(description = "SPU商品名称")
    private String productName;

    @Schema(description = "商品卖点:商品广告词")
    private String productTips;

    @Schema(description = "图片信息")
    private String productImage;

    @Schema(description = "运费模板")
    private Integer transportTypeId;

    @Schema(description = "商品标签")
    private String productTags;

    @Schema(description = "商品状态")
    private Integer productStateId;

    @Schema(description = "冻结库存")
    private Integer productInventoryLock;

    @Schema(description = "类型编号")
    private Integer kindId;

    @Schema(description = "购物车编号")
    private Long cartId;

    @Schema(description = "可用库存")
    private Integer availableQuantity;

    @Schema(description = "起订量")
    private Integer productMinimumOrder;

    @Schema(description = "购物数量")
    private Integer cartQuantity = 0;

    @Schema(description = "是否选中")
    private Boolean cartSelect = true;

    @Schema(description = "超出配送区域")
    private Boolean isOos = false;

    @Schema(description = "销售中")
    private Boolean isOnSale = true;

    @Schema(description = "三级分销允许分销(BOOL):1-启用分销;0-禁用分销")
    private Boolean productDistEnable;

    @Schema(description = "平台佣金比率")
    private BigDecimal productCommissionRate;

    @Schema(description = "折扣率")
    private BigDecimal itemPolicyDiscountrate;

    @Schema(description = "优惠总额:只考虑单品的，订单及店铺总活动优惠不影响")
    private BigDecimal itemDiscountAmount;

    @Schema(description = "金额小计:salePrice * cartQuantity - 折扣")
    private BigDecimal itemSubtotal;

    @Schema(description = "应付积分小计")
    private BigDecimal itemPointsSubtotal;

    @Schema(description = "分配优惠券额度")
    private BigDecimal itemVoucher = BigDecimal.ZERO;

    @Schema(description = "分配满减额度")
    private BigDecimal itemReduction = BigDecimal.ZERO;

    @Schema(description = "底价")
    private BigDecimal itemRatePrice;

    @Schema(description = "服务类型(ENUM):1001-到店服务;1002-上门服务")
    private Integer productValidType;

    @Schema(description = "填写预约日期(BOOL):0-否;1-是")
    private Boolean productServiceDateFlag;

    @Schema(description = "填写联系人(BOOL):0-否;1-是")
    private Boolean productServiceContactorFlag;

    @Schema(description = "课程类型(ENUM) : 0-视频;1-图文;2-音频;3-图文音频）")
    private Integer courseFileType;

    @Schema(description = "章节数量")
    private Integer courseChapterNum;

    @Schema(description = "满即送")
    private List<ProductItemVo> pulseGiftCart = new ArrayList<>();

    @Schema(description = "满即送选择")
    private List<ProductItemInfoVo> selectInfo = new ArrayList<>();

    @Schema(description = "选择")
    private Boolean selectable;

    @Schema(description = "最大数量")
    private Integer maxnum;

    @Schema(description = "数量")
    private Integer num;

    @Schema(description = "金额")
    private BigDecimal subtotal;

    @Schema(description = "购买限制")
    private Integer buyLimit;

    @Schema(description = "满返活动id")
    private Integer giveId;

    @Schema(description = "礼品-废弃")
    private List<Object> pulseReduction = new ArrayList<>();
    private List<Object> pulseMultple = new ArrayList<>();
    private List<Object> pulseBargainsCart = new ArrayList<>();
    private List<Object> pulseBargains = new ArrayList<>();

    @Schema(description = "行业编号集合")
    private String industryIds;

    @Schema(description = "所属店铺")
    private Integer storeId;

    @Schema(description = "价格类型")
    private String priceType;

    @Schema(description = "展示标签")
    private String priceTag;

}
