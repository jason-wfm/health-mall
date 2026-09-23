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
package com.wechuang.mallshop.cms.model.entity;

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
 * 文章标签表
 * </p>
 *
 * @author Xinze
 * @since 2021-05-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cms_article_tag")
@Schema(name = "ArticleTag对象", description = "文章标签表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ArticleTag implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "标签编号")
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Integer tagId;

    @Schema(description = "标签名称")
    @TableField("tag_name")
    private String tagName;

    @Schema(description = "标签内容数量")
    @TableField("tag_count")
    private Integer tagCount;

    @Schema(description = "用户编号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "版本")
    @TableField("version")
    private Integer version;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
