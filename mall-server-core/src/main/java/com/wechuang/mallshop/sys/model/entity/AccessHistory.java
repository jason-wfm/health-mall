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
package com.wechuang.mallshop.sys.model.entity;

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
import java.util.Map;

/**
 * <p>
 * 访问日志表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_access_history")
@Schema(name = "AccessHistory对象", description = "访问日志表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "编号")
    @TableId(value = "access_id", type = IdType.AUTO)
    private Long accessId;

    @Schema(description = "用户编号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "唯一客户编号")
    @TableField("access_client_id")
    private String accessClientId;

    @Schema(description = "操作系统")
    @TableField("access_os")
    private String accessOs;

    @Schema(description = "浏览器名称")
    @TableField("access_browser_name")
    private String accessBrowserName;

    @Schema(description = "浏览器版本")
    @TableField("access_browser_version")
    private String accessBrowserVersion;

    @Schema(description = "搜索引擎")
    @TableField("access_spider")
    private String accessSpider;

    @Schema(description = "国家")
    @TableField("access_country")
    private String accessCountry;

    @Schema(description = "省份")
    @TableField("access_province")
    private String accessProvince;

    @Schema(description = "市")
    @TableField("access_city")
    private String accessCity;

    @Schema(description = "区")
    @TableField("access_county")
    private String accessCounty;

    @Schema(description = "语言")
    @TableField("access_lang")
    private String accessLang;

    @Schema(description = "访问IP")
    @TableField("access_ip")
    private String accessIp;

    @Schema(description = "访问地址")
    @TableField("access_url")
    private String accessUrl;

    @Schema(description = "访问SKU")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "所属店铺")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "请求方法")
    @TableField("access_method")
    private String accessMethod;

    @Schema(description = "访问时间")
    @TableField("access_time")
    private Long accessTime;

    @Schema(description = "年")
    @TableField("access_year")
    private Integer accessYear;

    @Schema(description = "月")
    @TableField("access_month")
    private Integer accessMonth;

    @Schema(description = "日")
    @TableField("access_day")
    private Integer accessDay;

    @Schema(description = "时")
    @TableField("access_hour")
    private Integer accessHour;

    @Schema(description = "年月日")
    @TableField("access_date")
    private Date accessDate;

    @Schema(description = "时间")
    @TableField("access_datetime")
    private Date accessDatetime;

    @Schema(description = "来源")
    @TableField("access_refer_domain")
    private String accessReferDomain;

    @Schema(description = "来源")
    @TableField("access_refer_url")
    private String accessReferUrl;

    @Schema(description = "是否手机")
    @TableField("access_mobile")
    private Boolean accessMobile;

    @Schema(description = "是否平板")
    @TableField("access_pad")
    private Boolean accessPad;

    @Schema(description = "是否PC")
    @TableField("access_pc")
    private Boolean accessPc;

    @Schema(description = "终端(ENUM):1-Phone;2-Pad;3-Pc")
    @TableField("access_device")
    private Integer accessDevice;

    @Schema(description = "终端来源(ENUM):2310-其它;2311-pc;2312-H5;2313-APP;2314-小程序")
    @TableField("access_type")
    private Integer accessType;

    @Schema(description = "终端来源(ENUM):2320-其它;2321-微信;2322-百度;2323-支付宝;2324-头条")
    @TableField("access_from")
    private Integer accessFrom;

    @Schema(description = "请求数据")
    @TableField("access_data")
    private String accessData;

    @Schema(description = "请求数据")
    @TableField(exist = false)
    private Map<String, String> accessReq;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
