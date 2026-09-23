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
package com.wechuang.mallshop.account.model.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户SNS信息表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户SNS信息表分页查询")
public class UserSnsListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "微博数量")
    private Integer userBlog;

    @Schema(description = "好友数量")
    private Integer userFriend;

    @Schema(description = "粉丝数量")
    private Integer userFans;

    @Schema(description = "成长值")
    private Integer userGrowth;

    @Schema(description = "是否可以举报(BOOL):0-不可以;1-可以")
    private Boolean userReport;

    @Schema(description = "是否可以购买商品(BOOL):0-不可以;1-可以")
    private Boolean userBuy;

    @Schema(description = "是否允许发表言论(BOOL):0-不可以;1-可以")
    private Boolean userComment;

    @Schema(description = "推广店铺数量")
    private Integer userFansStore;

    @Schema(description = "帖子数量")
    private Integer userStory;

    @Schema(description = "评论数量")
    private Integer userStoryComment;

    @Schema(description = "收藏店铺")
    private Integer userFavoritesStore;

    @Schema(description = "收藏商品")
    private Integer userFavoritesItem;

    @Schema(description = "收藏品牌")
    private Integer userFavoritesBrand;

    @Schema(description = "收藏帖子")
    private Integer userStoryCollection;

    @Schema(description = "帖子点赞")
    private Integer userStoryLike;

    @Schema(description = "帖子转发")
    private Integer userStoryForward;

    @TableField(exist = false)
    private String sidx = "user_id";


}
