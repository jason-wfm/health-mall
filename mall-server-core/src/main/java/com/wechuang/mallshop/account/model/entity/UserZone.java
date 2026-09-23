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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 群组
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_user_zone")
@Schema(name = "UserZone对象", description = "群组")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserZone implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "好友组编号")
    @TableId(value = "zone_id", type = IdType.AUTO)
    private Integer zoneId;

    @Schema(description = "群组名称")
    @TableField("zone_name")
    private String zoneName;

    @Schema(description = "群组类型(ENUM):0-临时组上限100人;  1-普通组上限300人; 2-VIP组 上限500人")
    @TableField("zone_type")
    private Boolean zoneType;

    @Schema(description = "申请加入模式(ENUM): 0-默认直接加入; 1-需要身份验证; 2-私有群组")
    @TableField("zone_permission")
    private Integer zonePermission;

    @Schema(description = "群组公告")
    @TableField("zone_declared")
    private String zoneDeclared;

    @Schema(description = "管理员")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "第三方群组编号")
    @TableField("zone_bind_id")
    private String zoneBindId;

    @Schema(description = "人数")
    @TableField("zone_user_num")
    private Integer zoneUserNum;

    @TableField(exist = false)
    private String avatar = "//tva3.sinaimg.cn/crop.64.106.361.361.50/7181dbb3jw8evfbtem8edj20ci0dpq3a.jpg";


    @TableField(exist = false)
    private String groupname;

    @TableField(exist = false)
    Integer id = 0;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
