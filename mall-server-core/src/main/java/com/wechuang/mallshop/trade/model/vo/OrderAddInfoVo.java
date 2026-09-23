package com.wechuang.mallshop.trade.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评价Vo
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
public class OrderAddInfoVo {

    @Schema(description = "消息")
    private Map<Integer, String> message;

    @Schema(description = "优惠券")
    private List<Integer> userVoucherIds;

    @Schema(description = "发票")
    private Integer userInvoiceId;

}
