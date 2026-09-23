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
 * 用户SNS信息表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_user_sns")
@Schema(name = "UserSns对象", description = "用户SNS信息表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSns implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @Schema(description = "微博数量")
    @TableField("user_blog")
    private Integer userBlog;

    @Schema(description = "好友数量")
    @TableField("user_friend")
    private Integer userFriend;

    @Schema(description = "粉丝数量")
    @TableField("user_fans")
    private Integer userFans;

    @Schema(description = "成长值")
    @TableField("user_growth")
    private Integer userGrowth;

    @Schema(description = "是否可以举报(BOOL):0-不可以;1-可以")
    @TableField("user_report")
    private Boolean userReport;

    @Schema(description = "是否可以购买商品(BOOL):0-不可以;1-可以")
    @TableField("user_buy")
    private Boolean userBuy;

    @Schema(description = "是否允许发表言论(BOOL):0-不可以;1-可以")
    @TableField("user_comment")
    private Boolean userComment;

    @Schema(description = "推广店铺数量")
    @TableField("user_fans_store")
    private Integer userFansStore;

    @Schema(description = "帖子数量")
    @TableField("user_story")
    private Integer userStory;

    @Schema(description = "评论数量")
    @TableField("user_story_comment")
    private Integer userStoryComment;

    @Schema(description = "收藏店铺")
    @TableField("user_favorites_store")
    private Integer userFavoritesStore;

    @Schema(description = "收藏商品")
    @TableField("user_favorites_item")
    private Integer userFavoritesItem;

    @Schema(description = "收藏品牌")
    @TableField("user_favorites_brand")
    private Integer userFavoritesBrand;

    @Schema(description = "收藏帖子")
    @TableField("user_story_collection")
    private Integer userStoryCollection;

    @Schema(description = "帖子点赞")
    @TableField("user_story_like")
    private Integer userStoryLike;

    @Schema(description = "帖子转发")
    @TableField("user_story_forward")
    private Integer userStoryForward;

    @Schema(description = "背景图片")
    @TableField("user_poster")
    private String userPoster;

    @Schema(description = "背景图片")
    @TableField("user_sign")
    private String userSign;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
