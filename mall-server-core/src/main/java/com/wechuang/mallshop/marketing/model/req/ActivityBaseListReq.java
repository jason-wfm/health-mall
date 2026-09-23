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
package com.wechuang.mallshop.marketing.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 活动表-通过插件实现	当为拼团是activity_rule中的group_remain_quantity用于标识拼团剩余需要人数，如果用户登录了，需要查询出activity_groupbooking中参与该团的剩余情况
 * </p>
 *
 * @author Xinze
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "活动表-通过插件实现	当为拼团是activity_rule中的group_remain_quantity用于标识拼团剩余需要人数，如果用户登录了，需要查询出activity_groupbooking中参与该团的剩余情况分页查询")
public class ActivityBaseListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "活动名称")
    @QueryField(type = QueryType.LIKE)
    private String activityName;

    @Schema(description = "活动标题")
    private String activityTitle;

    @Schema(description = "活动说明")
    private String activityRemark;

    @Schema(description = "活动类型")
    private Integer activityTypeId;

    @Schema(description = "活动开始时间")
    private Long activityStarttime;

    @Schema(description = "活动结束时间")
    private Long activityEndtime;

    @Schema(description = "活动状态(ENUM):0-未开启;1-正常;2-已结束;3-管理员关闭;4-商家关闭")
    @QueryField(type = QueryType.IN_STR)
    private String activityState;

    @Schema(description = "参与类型(ENUM):1-免费参与;2-积分参与;3-购买参与;4-分享参与")
    private Integer activityType;

    @Schema(description = "活动排序")
    private Integer activitySort;

    @Schema(description = "活动是否完成(ENUM):0-未完成;1-已完成;2-已解散(目前用于团购)")
    private Integer activityIsFinish;

    @Schema(description = "分站编号")
    private Integer subsiteId;

    @Schema(description = "角色编号(ENUM):0-用户;2-商家;3-门店;8-租户;9-平台;")
    @TableField(exist = false)
    private Integer roleId;

    @Schema(description = "使用等级(DOT)")
    private String activityUseLevel;

    @Schema(description = "活动SKU(DOT):activity_rule中数据冗余")
    @QueryField(type = QueryType.FIND_IN_SET_STR)
    private String activityItemIds;

    @Schema(description = "是否PLUS专享")
    private Boolean activityIsPlus;

    private String met;

}
