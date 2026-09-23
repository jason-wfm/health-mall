package com.wechuang.mallshop.trade.model.res;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.vo.OrderVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算列表内部分页（与 Go OrderListOutput 对齐：列表字段为 items，避免 MyBatis Page 默认 JSON key records）。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "OrderSettlementPageRes")
public class OrderSettlementPageRes {

    @Schema(description = "分页数据内容")
    private List<OrderVo> items = new ArrayList<>();

    @Schema(description = "分页号码")
    private long page = 1;

    @Schema(description = "总页数")
    private long total;

    @Schema(description = "数据总数")
    private long records;

    @Schema(description = "单页数量")
    private long size = 10;

    public static OrderSettlementPageRes fromMyBatisPage(IPage<OrderVo> page, List<OrderVo> list) {
        OrderSettlementPageRes res = new OrderSettlementPageRes();
        res.setItems(list != null ? list : new ArrayList<>());
        if (page != null) {
            res.setPage(page.getCurrent());
            res.setTotal(page.getPages());
            res.setRecords(page.getTotal());
            res.setSize(page.getSize());
        }
        return res;
    }

    public static OrderSettlementPageRes empty(long page, long size) {
        OrderSettlementPageRes res = new OrderSettlementPageRes();
        res.setPage(page > 0 ? page : 1);
        res.setSize(size > 0 ? size : 10);
        res.setTotal(0);
        res.setRecords(0);
        res.setItems(new ArrayList<>());
        return res;
    }
}
