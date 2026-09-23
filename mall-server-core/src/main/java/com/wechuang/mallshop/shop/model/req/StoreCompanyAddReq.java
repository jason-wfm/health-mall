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

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * <p>
 * 店铺公司信息表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "店铺公司信息表参数")
public class StoreCompanyAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "所属用户")
    private Integer userId;

    @Schema(description = "店铺编号")
    private Integer storeId;

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

    @Schema(description = "法人身份证电子版")
    private String legalPersonElectronic;

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
