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
package com.wechuang.mallshop.shop.model.entity;

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
import java.util.Date;

/**
 * <p>
 * 店铺表-用户可以多店铺-需要分表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("shop_store_info")
@Schema(name = "StoreInfo对象", description = "店铺表-用户可以多店铺-需要分表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺编号")
    @TableId(value = "store_id", type = IdType.AUTO)
    private Integer storeId;

    @Schema(description = "店铺口号")
    @TableField("store_slogan")
    private String storeSlogan;

    @Schema(description = "店铺banner")
    @TableField("store_banner")
    private String storeBanner;

    @Schema(description = "国家编码")
    @TableField("store_intl")
    private String storeIntl;

    @Schema(description = "卖家电话")
    @TableField("store_tel")
    private String storeTel;

    @Schema(description = "免运费额度")
    @TableField("store_free_shipping")
    private Integer storeFreeShipping;

    @Schema(description = "店铺绑定模板")
    @TableField("store_template")
    private String storeTemplate;

    @Schema(description = "本次开始时间")
    @TableField("store_start_time")
    private Date storeStartTime;

    @Schema(description = "有效期截止时间")
    @TableField("store_end_time")
    private Date storeEndTime;

    @Schema(description = "关闭原因")
    @TableField("store_close_reason")
    private String storeCloseReason;

    @Schema(description = "购买须知")
    @TableField("store_notice")
    private String storeNotice;

    @Schema(description = "营业时间")
    @TableField("store_opening_hours")
    private String storeOpeningHours;

    @Schema(description = "打烊时间")
    @TableField("store_close_hours")
    private String storeCloseHours;

    @Schema(description = "所在区域")
    @TableField("store_area")
    private String storeArea;

    @Schema(description = "所属地区(DOT)")
    @TableField("store_district_id")
    private String storeDistrictId;

    @Schema(description = "店铺详细地址")
    @TableField("store_address")
    private String storeAddress;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "店铺移动端banner")
    @TableField("store_mobile_banners")
    private String storeMobileBanners;

    @Schema(description = "热卖背景色")
    @TableField("hot_sale_color")
    private String hotSaleColor;

    @Schema(description = "移动端顶部banner")
    @TableField("mobile_top_banner")
    private String mobileTopBanner;

}
