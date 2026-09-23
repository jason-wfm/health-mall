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

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 店铺基础信息表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "店铺基础信息表分页查询")
public class StoreBaseListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "店铺分类编号")
    private Integer storeCategoryId;

    @Schema(description = "距离(仅getNearShop定位查询使用;非null且小于20000000000才过滤)")
    @TableField(exist = false)
    private Long distance;

    @Schema(description = "店铺名称")
    @QueryField(type = QueryType.LIKE)
    private String storeName;

    @Schema(description = "店铺状态(BOOL):0-关闭;  1-运营中")
    private Boolean storeIsOpen;

    @Schema(description = "搜索关键词")
    @QueryField(value = "store_name", type = QueryType.LIKE)
    private String keywords;

    @Schema(description = "店铺资料信息状态(ENUM):3210-待完善资料;   3220-等待审核 ;  3230-资料审核没有通过;     3240-资料审核通过,待付款")
    private Integer storeStateId;

    @Schema(description = "店铺类型(ENUM): 1-卖家店铺; 2-供应商店铺")
    private Integer storeType;

    @Schema(description = "商品数量")
    @TableField(exist = false)
    private Integer keyType;

    @TableField(exist = false)
    private String sidx = "store_id";

    @TableField(exist = false)
    private String sort = Constants.ORDER_BY_DESC;

}
