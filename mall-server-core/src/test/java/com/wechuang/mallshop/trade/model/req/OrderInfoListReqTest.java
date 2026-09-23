package com.wechuang.mallshop.trade.model.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [healthmall-ext] OrderInfoListReq 查询条件回归测试
 * 锚定 is_review 列错名修复：修复前该字段默认 false（非空），
 * BaseQueryWrapper 恒生成 `is_review = ?` 条件，而 trade_order_info 实际列为
 * order_is_review，导致 /manage/trade/orderBase/list 存量 BadSqlGrammar(1054)。
 */
class OrderInfoListReqTest {

    private QueryWrapper<Object> wrapperOf(OrderInfoListReq req) {
        return new BaseQueryWrapper<Object, OrderInfoListReq>(req).getWrapper();
    }

    @Test
    @DisplayName("默认请求不生成任何 is_review 条件（修复前恒生成 is_review = ? 致 1054）")
    void defaultReqGeneratesNoIsReviewCondition() {
        QueryWrapper<Object> wrapper = wrapperOf(new OrderInfoListReq());
        String sql = wrapper.getSqlSegment();
        assertFalse(sql.contains("is_review"), "默认请求不应生成 is_review 条件，实际: " + sql);
    }

    @Test
    @DisplayName("显式传 isReview 时映射到真实列 order_is_review")
    void explicitIsReviewMapsToRealColumn() {
        OrderInfoListReq req = new OrderInfoListReq();
        req.setIsReview(Boolean.TRUE);
        QueryWrapper<Object> wrapper = wrapperOf(req);
        String sql = wrapper.getSqlSegment();
        assertTrue(sql.contains("order_is_review ="), "应映射到 order_is_review 列，实际: " + sql);
        assertFalse(sql.contains(" is_review ="), "不应再出现裸 is_review 列名，实际: " + sql);
    }

    @Test
    @DisplayName("排序默认 create_time DESC 保持不变")
    void defaultOrderingPreserved() {
        QueryWrapper<Object> wrapper = wrapperOf(new OrderInfoListReq());
        assertTrue(wrapper.getSqlSegment().contains("`create_time` DESC"));
    }
}
