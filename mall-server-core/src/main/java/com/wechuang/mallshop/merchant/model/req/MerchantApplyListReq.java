package com.wechuang.mallshop.merchant.model.req;

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
 * 商家申请单分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商家申请单分页查询")
public class MerchantApplyListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "申请编号")
    private Integer applyId;

    @Schema(description = "商家编号")
    private Integer merchantId;

    @Schema(description = "类型:10入驻 20变更 30退驻")
    private Integer applyType;

    @Schema(description = "状态:10待审核 20通过 30驳回")
    private Integer status;

    @Schema(description = "申请人姓名")
    @QueryField(type = QueryType.LIKE)
    private String applyName;

    @Schema(description = "商家名称(快照模糊搜索)")
    @QueryField(type = QueryType.LIKE)
    private String keywords;

    @TableField(exist = false)
    private String sidx = "apply_id";

    @TableField(exist = false)
    private String sort = Constants.ORDER_BY_DESC;
}
