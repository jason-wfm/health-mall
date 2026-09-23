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
 * 商家列表分页查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商家列表分页查询")
public class MerchantBaseListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商家编号")
    private Integer merchantId;

    @Schema(description = "商家名称")
    @QueryField(type = QueryType.LIKE)
    private String merchantName;

    @Schema(description = "状态:10待提交 20待审核 30驳回 40营业 50冻结 60退驻")
    private Integer status;

    @Schema(description = "搜索关键词")
    @QueryField(value = "merchant_name", type = QueryType.LIKE)
    private String keywords;

    @TableField(exist = false)
    private String sidx = "merchant_id";

    @TableField(exist = false)
    private String sort = Constants.ORDER_BY_DESC;
}
