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
package com.wechuang.mallshop.invoicing.model.entity;

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
 * 仓库仓库表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("invoicing_warehouse_base")
@Schema(name = "WarehouseBase对象", description = "仓库仓库表 ")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WarehouseBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "仓库编号")
    @TableId(value = "warehouse_id", type = IdType.AUTO)
    private Integer warehouseId;

    @Schema(description = "仓库编号")
    @TableField("warehouse_number")
    private String warehouseNumber;

    @Schema(description = "仓库名称")
    @TableField("warehouse_name")
    private String warehouseName;

    @Schema(description = "仓库地址")
    @TableField("warehouse_address")
    private String warehouseAddress;

    @Schema(description = "仓库类型编号")
    @TableField("warehouse_type_id")
    private Integer warehouseTypeId;

    @Schema(description = "仓库是否启用(BOOL):0-禁用;1-启用")
    @TableField("warehouse_enable")
    private Boolean warehouseEnable;

    @Schema(description = "仓库是否默认(BOOL):0-非默认;1-默认仓库")
    @TableField("warehouse_is_default")
    private Boolean warehouseIsDefault;

    @Schema(description = "仓库联系人")
    @TableField("warehouse_contact")
    private String warehouseContact;

    @Schema(description = "仓库联系方式")
    @TableField("warehouse_phone")
    private Integer warehousePhone;

    @Schema(description = "发货属性(ENUM):1-发货仓库; 0-备货仓库")
    @TableField("warehouse_delivery_type")
    private Boolean warehouseDeliveryType;

    @Schema(description = "备货仓库编号(DOT)如果为发货仓库绑定的备货仓库编号")
    @TableField("warehouse_delivery_ids")
    private String warehouseDeliveryIds;

    @Schema(description = "所属店铺")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
