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

/**
 * <p>
 * 物流 = shop_store_express
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("shop_store_express_logistics")
@Schema(name = "StoreExpressLogistics对象", description = "物流 = shop_store_express")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreExpressLogistics implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "物流编号")
    @TableId(value = "logistics_id", type = IdType.AUTO)
    private Integer logisticsId;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "店铺名称")
    @TableField(exist = false)
    private String storeName;

    @Schema(description = "门店编号")
    @TableField("chain_id")
    private Integer chainId;

    @Schema(description = "物流名称")
    @TableField("logistics_name")
    private String logisticsName;

    @Schema(description = "物流拼音")
    @TableField("logistics_pinyin")
    private String logisticsPinyin;

    @Schema(description = "公司编号")
    @TableField("logistics_number")
    private Integer logisticsNumber;

    @Schema(description = "面单状态(BOOL):1-启用;0-禁用")
    @TableField("logistics_state")
    private Boolean logisticsState;

    @Schema(description = "快递编号")
    @TableField("express_id")
    private Integer expressId;

    @Schema(description = "快递名称")
    @TableField("express_name")
    private String expressName;

    @Schema(description = "是否为默认(BOOL):1-默认;0-非默认")
    @TableField("logistics_is_default")
    private Boolean logisticsIsDefault;

    @Schema(description = "国家编码")
    @TableField("logistics_intl")
    private String logisticsIntl;

    @Schema(description = "联系手机")
    @TableField("logistics_mobile")
    private String logisticsMobile;

    @Schema(description = "联系人")
    @TableField("logistics_contacter")
    private String logisticsContacter;

    @Schema(description = "联系地址")
    @TableField("logistics_address")
    private String logisticsAddress;

    @Schema(description = "物流运费")
    @TableField("logistics_fee")
    private String logisticsFee;

    @Schema(description = "是否启用(BOOL):1-启用;0-禁用")
    @TableField("logistics_is_enable")
    private Boolean logisticsIsEnable;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
