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
package com.wechuang.mallshop.shop.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺基础信息表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "店铺基础信息表参数")
public class StoreBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺等级")
    private Integer storeGradeId;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "纬度")
    private Double storeLatitude;

    @Schema(description = "经度")
    private Double storeLongitude;

    @Schema(description = "配送区域(DOT)")
    private String storeDeliverDistrictId;

    @Schema(description = "是否自营(ENUM): 1-自营;0-非自营")
    private Boolean storeIsSelfsupport;

    @Schema(description = "店铺类型(ENUM): 1-卖家店铺; 2-供应商店铺")
    private Integer storeType;

    @Schema(description = "店铺状态(BOOL):0-关闭;  1-运营中")
    private Boolean storeIsOpen;

    @Schema(description = "店铺分类编号")
    private Integer storeCategoryId;

    @Schema(description = "免费服务(DOT)")
    private String storeO2oTags;

    @Schema(description = "是否O2O(BOOL):0-否;1-是")
    private Boolean storeO2oFlag;

    @Schema(description = "店铺资料信息状态(ENUM):3210-待完善资料;   3220-等待审核 ;  3230-资料审核没有通过;     3240-资料审核通过,待付款")
    private Integer storeStateId;

    @Schema(description = "店铺审核备注")
    private String storeStateRemark;

    @Schema(description = "付款状态(ENUM):0-未付款;   1-已付款待审核;   2-审核通过  ")
    private Integer storePaymentState;

    @Schema(description = "所属商圈(DOT)")
    private String storeCircle;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;


    @Schema(description = "国家编码")
    private String storeIntl;

    @Schema(description = "卖家电话")
    private String storeTel;

    @Schema(description = "营业时间")
    private String storeOpeningHours;

    @Schema(description = "打烊时间")
    private String storeCloseHours;

    @Schema(description = "所在区域")
    private String storeArea;

    @Schema(description = "所属地区(DOT)")
    private String storeDistrictId;

    @Schema(description = "店铺详细地址")
    private String storeAddress;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "企业全称(length=>2,50)")
    private String companyName;

    @Schema(description = "公司所在地")
    private String companyArea;

    @Schema(description = "地址:请输入公司详细地址")
    private String companyAddress;

    @Schema(description = "邮编:请输入公司邮编")
    private String companyZipcode;

    @Schema(description = "电话:请输入公司电话")
    private String companyPhone;

    @Schema(description = "公司网址:请输入公司网址")
    private String companyWebsite;

    @Schema(description = "联系人")
    private String contactsName;

    @Schema(description = "职位")
    private String contactsPosition;

    @Schema(description = "联系人电话")
    private String contactsPhone;

    @Schema(description = "联系人email")
    private String contactsEmail;

    @Schema(description = "注册资金")
    private Integer companyRegisteredCapital;

    @Schema(description = "员工总数")
    private Integer companyEmployeeCount;

    @Schema(description = "纳税人识别号")
    private String companyTaxnum;

    @Schema(description = "发票抬头")
    private String companyInvoice;

    @Schema(description = "法定代表人姓名")
    private String legalPerson;

    @Schema(description = "法人身份证号")
    private String legalPersonNumber;

    @Schema(description = "营业执照电子版")
    private String businessLicenseElectronic;

    @Schema(description = "其它证件(DOT)")
    private String companyOtherFiles;

    @Schema(description = "银行开户名")
    private String bankAccountName;

    @Schema(description = "公司银行账号")
    private String bankAccountNumber;

    @Schema(description = "开户银行支行名称")
    private String bankName;

    @Schema(description = "开户银行支行联行号")
    private String bankCode;

    @Schema(description = "开户银行支行所在地")
    private String bankAddress;

}
