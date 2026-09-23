package com.wechuang.mallshop.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.model.BaseOrder;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [healthmall-ext] BaseQueryWrapper 行为规格测试（core 自研重写，阶段C 对照验证用）
 * 断言生成的 SQL 片段与参数映射，不依赖数据库；
 * 同一测试曾在原 core-3.0.27908 jar 下执行过对照（见 docs/core重写方案.md 实施记录）
 */
class BaseQueryWrapperSpecTest {

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class SpecReq extends BaseListReq {
        @QueryField(value = "user_name", type = QueryType.LIKE)
        private String userName;

        @QueryField(value = "order_state_id", type = QueryType.EQ)
        private Integer orderStateId;

        @QueryField(value = "create_time", type = QueryType.GE)
        private Long orderStime;

        @QueryField(value = "store_id", type = QueryType.IN_STR)
        private String storeIds;

        @QueryField(value = "user_tag", type = QueryType.FIND_IN_SET_STR)
        private String userTag;

        private Integer plainField;

        private String blankField;

        private Collection<Integer> collectionField;
    }

    private QueryWrapper<Object> wrapperOf(SpecReq req) {
        return new BaseQueryWrapper<Object, SpecReq>(req).getWrapper();
    }

    @Test
    @DisplayName("LIKE 注解生成 like 条件")
    void likeAnnotation() {
        SpecReq req = new SpecReq();
        req.setUserName("张三");
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("user_name LIKE"));
        assertEquals("%张三%", wrapper.getParamNameValuePairs().values().iterator().next().toString());
    }

    @Test
    @DisplayName("EQ 注解按注解列名等值")
    void eqAnnotation() {
        SpecReq req = new SpecReq();
        req.setOrderStateId(20);
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("order_state_id ="));
        assertTrue(wrapper.getParamNameValuePairs().containsValue(20));
    }

    @Test
    @DisplayName("GE 注解生成 >= 条件")
    void geAnnotation() {
        SpecReq req = new SpecReq();
        req.setOrderStime(1700000000000L);
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("create_time >="));
    }

    @Test
    @DisplayName("IN_STR 注解按逗号拆分 in 条件")
    void inStrAnnotation() {
        SpecReq req = new SpecReq();
        req.setStoreIds("1,2,3");
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("store_id IN"));
        Map<String, Object> params = wrapper.getParamNameValuePairs();
        assertTrue(params.containsValue("1"));
        assertTrue(params.containsValue("2"));
        assertTrue(params.containsValue("3"));
    }

    @Test
    @DisplayName("FIND_IN_SET_STR 注解生成 FIND_IN_SET 原生条件")
    void findInSetAnnotation() {
        SpecReq req = new SpecReq();
        req.setUserTag("vip");
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("FIND_IN_SET"));
        assertTrue(wrapper.getSqlSegment().contains("user_tag"));
    }

    @Test
    @DisplayName("无注解字段默认按驼峰转下划线 EQ")
    void defaultEqForPlainField() {
        SpecReq req = new SpecReq();
        req.setPlainField(9);
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("plain_field ="));
        assertTrue(wrapper.getParamNameValuePairs().containsValue(9));
    }

    @Test
    @DisplayName("空白串跳过条件生成（e7f1d49 语义：原版空白串生成条件致管理端空参数查空）；空集合跳过（本实现加固：原版会生成 eq(集合) 的异常条件）")
    void skipEmptyValues() {
        SpecReq req = new SpecReq();
        req.setBlankField("  ");
        req.setCollectionField(Arrays.asList(1, 2));
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertFalse(wrapper.getSqlSegment().contains("blank_field"));
        assertFalse(wrapper.getSqlSegment().contains("collection_field"));

        SpecReq nonBlankReq = new SpecReq();
        nonBlankReq.setBlankField("abc");
        QueryWrapper<Object> nonBlankWrapper = wrapperOf(nonBlankReq);
        assertTrue(nonBlankWrapper.getSqlSegment().contains("blank_field ="));
        assertTrue(nonBlankWrapper.getParamNameValuePairs().containsValue("abc"));
    }

    @Test
    @DisplayName("createTimeStart/End 生成 create_time 区间")
    void createTimeRange() {
        SpecReq req = new SpecReq();
        req.setCreateTimeStart("2026-01-01 00:00:00");
        req.setCreateTimeEnd("2026-12-31 23:59:59");
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("create_time >="));
        assertTrue(wrapper.getSqlSegment().contains("create_time <="));
    }

    @Test
    @DisplayName("排序：sidx/sort 优先，order 列表其次，列名反引号包裹")
    void ordering() {
        SpecReq req = new SpecReq();
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setSidx("sort_order");
        baseOrder.setSort("asc");
        req.setOrder(List.of(baseOrder));
        req.setSidx("create_time");
        req.setSort("desc");
        QueryWrapper<Object> wrapper = wrapperOf(req);
        String sql = wrapper.getSqlSegment();
        assertTrue(sql.contains("`create_time` DESC"));
        assertTrue(sql.contains("`sort_order` ASC"));
        assertTrue(sql.indexOf("`create_time`") < sql.indexOf("`sort_order`"), "sidx 优先于 order 列表（原版行为）");
    }

    @Test
    @DisplayName("非法 sidx 反引号包裹后透传（原版行为，运行期 SQL 报错即失败暴露）")
    void unsafeSidxWrapped() {
        SpecReq req = new SpecReq();
        req.setSidx("create_time; DROP TABLE x");
        QueryWrapper<Object> wrapper = wrapperOf(req);
        assertTrue(wrapper.getSqlSegment().contains("ORDER BY"));
        assertTrue(wrapper.getSqlSegment().contains("`create_time; DROP TABLE x`"));
    }

    @Test
    @DisplayName("无注解处理关闭时不生成默认 EQ")
    void processDefaultFieldsOff() {
        SpecReq req = new SpecReq();
        req.setPlainField(9);
        QueryWrapper<Object> wrapper = new BaseQueryWrapper<Object, SpecReq>(req, false).getWrapper();
        assertFalse(wrapper.getSqlSegment().contains("plain_field"));
    }

    @Test
    @DisplayName("addslashes：单引号双写、反斜杠/双引号加反斜杠（原版行为）")
    void addslashes() {
        assertNull(BaseQueryWrapper.addslashes(null));
        assertEquals("a''b", BaseQueryWrapper.addslashes("a'b"));
        assertEquals("a\\\\b", BaseQueryWrapper.addslashes("a\\b"));
        assertEquals("a\\\"b", BaseQueryWrapper.addslashes("a\"b"));
    }

    @Test
    @DisplayName("null 请求返回空条件（本实现加固：原版直接 NPE）")
    void nullReq() {
        QueryWrapper<Object> wrapper = wrapperOf(null);
        assertEquals("", wrapper.getSqlSegment());
    }
}
