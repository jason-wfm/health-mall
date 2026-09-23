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
package com.wechuang.mallshop.account.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.common.annotation.XssSafe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户地址表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_user_delivery_address")
@Schema(name = "UserDeliveryAddress对象", description = "用户地址表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDeliveryAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "地址编号")
    @TableId(value = "ud_id", type = IdType.AUTO)
    private Integer udId;

    @Schema(description = "用户编号")
    @TableField("user_id")
    private Integer userId;

    @XssSafe(mode = XssSafe.Mode.ESCAPE)
    @Schema(description = "联系人")
    @TableField("ud_name")
    private String udName;

    @Schema(description = "国家编码")
    @TableField("ud_intl")
    private String udIntl;

    @Schema(description = "手机号码")
    @TableField("ud_mobile")
    private String udMobile;

    @Schema(description = "联系电话")
    @TableField("ud_telephone")
    private String udTelephone;

    @Schema(description = "省编号")
    @TableField("ud_province_id")
    private Integer udProvinceId;

    @Schema(description = "省份")
    @TableField("ud_province")
    private String udProvince;

    @Schema(description = "市编号")
    @TableField("ud_city_id")
    private Integer udCityId;

    @Schema(description = "市")
    @TableField("ud_city")
    private String udCity;

    @Schema(description = "县")
    @TableField("ud_county_id")
    private Integer udCountyId;

    @Schema(description = "县区")
    @TableField("ud_county")
    private String udCounty;

    @XssSafe(mode = XssSafe.Mode.ESCAPE)
    @Schema(description = "详细地址")
    @TableField("ud_address")
    private String udAddress;

    @Schema(description = "邮政编码")
    @TableField("ud_postalcode")
    private String udPostalcode;

    @Schema(description = "地址标签(ENUM):1001-家里;1002-公司")
    @TableField("ud_tag_name")
    private String udTagName;

    @Schema(description = "经度")
    @TableField("ud_longitude")
    private Double udLongitude;

    @Schema(description = "纬读")
    @TableField("ud_latitude")
    private Double udLatitude;

    @Schema(description = "添加时间")
    @TableField("ud_time")
    private Date udTime;

    @Schema(description = "是否默认(BOOL):0-非默认;1-默认")
    @TableField("ud_is_default")
    private Boolean udIsDefault;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
