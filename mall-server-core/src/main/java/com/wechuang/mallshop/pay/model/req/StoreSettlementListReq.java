package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商户结算单分页查询
 * </p>
 *
 * @author jason
 * @since 2026-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商户结算单分页查询")
public class StoreSettlementListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺编号(商家身份自动按本店过滤)")
    private Integer storeId;

    @Schema(description = "结算状态:0-待商家确认;1-已确认待出金;2-出金中;3-已完成;4-已驳回")
    private Integer settlementState;

    public StoreSettlementListReq() {
        setSidx("settlement_id");
        setSort(Constants.ORDER_BY_DESC);
    }
}
