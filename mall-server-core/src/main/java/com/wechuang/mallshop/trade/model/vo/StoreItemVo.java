package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.shop.model.res.UserVoucherRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "店铺及商品信息", description = "店铺及商品信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreItemVo implements Serializable {
    @Schema(description = "店铺编号")
    Integer storeId = 0;

    @Schema(description = "店铺名称")
    String storeName;

    @Schema(description = "店铺状态(BOOL):0-关闭;  1-运营中")
    private Boolean storeIsOpen = true;

    @Schema(description = "是否自营(ENUM): 1-自营;0-非自营")
    private Boolean storeIsSelfsupport = true;

    @Schema(description = "商品信息")
    List<ProductItemVo> items = new ArrayList<>();

    @Schema(description = "店铺活动")
    ActivitysVo activitys = new ActivitysVo();

    //过程
    @Schema(description = "非排他活动")
    ActivityBase activityBase = null;

    @Schema(description = "满返活动")
    List<Integer> manhuiActivityIds = new ArrayList<>();

    @Schema(description = "满返代金券")
    List<Integer> manhuiVoucherIds = new ArrayList<>();

    @Schema(description = "提货券")
    List<ActivitysVo> redemptionItems = new ArrayList<>();

    @Schema(description = "优惠券")
    List<UserVoucherRes> voucherItems = new ArrayList<>();

    @Schema(description = "商品原价总价")
    BigDecimal productAmount;

    @Schema(description = "运费总价")
    BigDecimal freightAmount;

    @Schema(description = "还差N免运费")
    BigDecimal freightFreeBalance;

    @Schema(description = "优惠总额度")
    BigDecimal discountAmount;

    @Schema(description = "销售员折扣")
    BigDecimal salePersonDiscount;

    @Schema(description = "单品优惠总价:单纯商品优惠后累加")
    BigDecimal moneyItemAmount;

    @Schema(description = "商品最终总价:单品优惠总价+运费-代金券")
    BigDecimal moneyAmount;

    @Schema(description = "需要总积分")
    BigDecimal pointsAmount;

    @Schema(description = "需要总积分2")
    BigDecimal spAmount;

    @Schema(description = "优惠券编号")
    private Integer userVoucherId;

    @Schema(description = "代金券")
    BigDecimal voucherAmount = BigDecimal.ZERO;

    @Schema(description = "订单类型")
    Integer kindId;

    @Schema(description = "是否虚拟")
    Boolean isVirtual;

    @Schema(description = "会员等级优惠总额度")
    BigDecimal levelDiscountAmount;

    @Schema(description = "PLUS优惠总额度")
    BigDecimal plusDiscountAmount;

    @Schema(description = "PLUS运费优惠金额")
    BigDecimal plusShippingDiscount;

}
