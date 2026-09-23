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
import java.math.BigDecimal;

/**
 * <p>
 * 消费者保障服务表-店铺加入
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_contract_type")
@Schema(name = "ContractType对象", description = "消费者保障服务表-店铺加入")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContractType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "保障编号")
    @TableId(value = "contract_type_id", type = IdType.AUTO)
    private Integer contractTypeId;

    @Schema(description = "保障名称")
    @TableField("contract_type_name")
    private String contractTypeName;

    @Schema(description = "保障简写")
    @TableField("contract_type_desc")
    private String contractTypeDesc;

    @Schema(description = "保障描述")
    @TableField("contract_type_text")
    private String contractTypeText;

    @Schema(description = "保证金")
    @TableField("contract_type_deposit")
    private BigDecimal contractTypeDeposit;

    @Schema(description = "项目图标")
    @TableField("contract_type_icon")
    private String contractTypeIcon;

    @Schema(description = "说明网址")
    @TableField("contract_type_url")
    private String contractTypeUrl;

    @Schema(description = "保障排序")
    @TableField("contract_type_order")
    private Integer contractTypeOrder;

    @Schema(description = "是否开启(BOOL):0-关闭;1-开启")
    @TableField("contract_type_enable")
    private Boolean contractTypeEnable;

    @Schema(description = "系统内置(BOOL): 0-非内置;1-系统内置")
    @TableField("contract_type_buildin")
    private Boolean contractTypeBuildin;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
