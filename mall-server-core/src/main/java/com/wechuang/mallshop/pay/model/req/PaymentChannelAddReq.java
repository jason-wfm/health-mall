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
package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 支付渠道表-可以用config取代-收款账户
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "支付渠道表-可以用config取代-收款账户参数")
public class PaymentChannelAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "代码名称")
    private String paymentChannelCode;

    @Schema(description = "账户名称")
    private String paymentChannelName;

    @Schema(description = "图标")
    private String paymentChannelImg;

    @Schema(description = "支付接口配置信息(JSON)")
    private String paymentChannelConfig;

    @Schema(description = "接口状态")
    private Boolean paymentChannelStatus;

    @Schema(description = "类型(LIST):both-全终端使用;wap-移动端;pc-PC端")
    private String paymentChannelAllow;

    @Schema(description = "微信中是否可以使用(LIST):1-启用; 0-禁用")
    private Integer paymentChannelWechat;

    @Schema(description = "是否启用(ENUM):1-启用; 0-禁用")
    private Integer paymentChannelEnable;

    @Schema(description = "支付方式(LIST):1302-在线支付【站内支付余额充值卡白条】;1305-线下支付")
    private Integer paymentTypeId;

    @Schema(description = "排序:数字越小排序越靠前")
    private Boolean paymentChannelOrder;

    @Schema(description = "系统内置(ENUM):1-系统内置; 0-线下账户")
    private Boolean paymentChannelBuildin;

    @Schema(description = "所属店铺:0-为平台使用")
    private Integer storeId;

    @Schema(description = "所属门店")
    private Integer chainId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;


}
