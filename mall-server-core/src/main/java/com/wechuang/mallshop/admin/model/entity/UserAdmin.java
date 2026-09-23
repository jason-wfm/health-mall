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
package com.wechuang.mallshop.admin.model.entity;

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
 * 管理员表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("admin_user_admin")
@Schema(name = "UserAdmin对象", description = "管理员表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @Schema(description = "权限角色")
    @TableField("user_role_id")
    private Integer userRoleId;

    @Schema(description = "创建时间")
    @TableField("user_admin_ctime")
    private Date userAdminCtime;

    @Schema(description = "更新时间")
    @TableField("user_admin_utime")
    private Date userAdminUtime;

    @Schema(description = "是否超管(BOOL):0-否;1-是")
    @TableField("user_is_superadmin")
    private Boolean userIsSuperadmin;

    @Schema(description = "角色编号(ENUM):0-用户;2-商家;3-门店;8-租户;9-平台;")
    @TableField("role_id")
    private Integer roleId;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId = 0;

    @Schema(description = "门店编号")
    @TableField("chain_id")
    private Integer chainId = 0;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "[healthmall-ext] 商家编号:0-平台/无归属")
    @TableField("merchant_id")
    private Integer merchantId = 0;

    @Schema(description = "是否启用(BOOL):0-否;1-是")
    @TableField("user_enable")
    private Boolean userEnable;
}
