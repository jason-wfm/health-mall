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
import java.io.Serializable;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺公司信息表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("shop_store_company")
@Schema(name = "StoreCompany对象", description = "店铺公司信息表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreCompany implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "公司编号")
    @TableId(value = "company_id", type = IdType.AUTO)
    private Integer companyId;

    @Schema(description = "所属用户")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "企业全称(length=>2,50)")
    @TableField("company_name")
    private String companyName;

    @Schema(description = "公司所在地")
    @TableField("company_area")
    private String companyArea;

    @Schema(description = "公司详细地址")
    @TableField("company_address")
    private String companyAddress;

    @Schema(description = "邮编:请输入公司邮编")
    @TableField("company_zipcode")
    private String companyZipcode;

    @Schema(description = "电话:请输入公司电话")
    @TableField("company_phone")
    private String companyPhone;

    @Schema(description = "公司网址:请输入公司网址")
    @TableField("company_website")
    private String companyWebsite;

    @Schema(description = "联系人")
    @TableField("contacts_name")
    private String contactsName;

    @Schema(description = "职位")
    @TableField("contacts_position")
    private String contactsPosition;

    @Schema(description = "联系人电话")
    @TableField("contacts_phone")
    private String contactsPhone;

    @Schema(description = "联系人email")
    @TableField("contacts_email")
    private String contactsEmail;

    @Schema(description = "注册资金")
    @TableField("company_registered_capital")
    private Integer companyRegisteredCapital;

    @Schema(description = "员工总数")
    @TableField("company_employee_count")
    private Integer companyEmployeeCount;

    @Schema(description = "纳税人识别号")
    @TableField("company_taxnum")
    private String companyTaxnum;

    @Schema(description = "发票抬头")
    @TableField("company_invoice")
    private String companyInvoice;

    @Schema(description = "法定代表人姓名")
    @TableField("legal_person")
    private String legalPerson;

    @Schema(description = "法人身份证号")
    @TableField("legal_person_number")
    private String legalPersonNumber;

    @Schema(description = "法人身份证电子版")
    @TableField("legal_person_electronic")
    private String legalPersonElectronic;

    @Schema(description = "营业执照电子版")
    @TableField("business_license_electronic")
    private String businessLicenseElectronic;

    @Schema(description = "其它证件(DOT)")
    @TableField("company_other_files")
    private String companyOtherFiles;

    @Schema(description = "银行开户名")
    @TableField("bank_account_name")
    private String bankAccountName;

    @Schema(description = "公司银行账号")
    @TableField("bank_account_number")
    private String bankAccountNumber;

    @Schema(description = "开户银行支行名称")
    @TableField("bank_name")
    private String bankName;

    @Schema(description = "开户银行支行联行号")
    @TableField("bank_code")
    private String bankCode;

    @Schema(description = "开户银行支行所在地")
    @TableField("bank_address")
    private String bankAddress;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
