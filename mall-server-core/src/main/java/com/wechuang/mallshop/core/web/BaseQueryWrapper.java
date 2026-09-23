package com.wechuang.mallshop.core.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.model.BaseOrder;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ListReq → QueryWrapper 适配器（自研重写，行为对齐 core-3.0.27908 黑盒探针结论）
 *
 * 规则（与原版一致，探针记录见 docs/core重写方案.md 实施记录）：
 * 1. 带 @QueryField 的字段按注解列名与 QueryType 拼条件（value 为空时字段名驼峰转下划线）
 * 2. 无注解字段（processDefaultFields=true 时）仅做 null 检查——空白串也生成 EQ（原版行为）
 * 3. createTimeStart/End → create_time 的 GE/LE
 * 4. 排序：sidx+sort 优先，其次 order 列表；列名反引号包裹（原版行为）
 * 5. page、size、sidx、sort、order、like、createTimeStart、createTimeEnd、sourceLang
 *    不参与条件；like 列表原版不处理（业务零使用），保持不处理
 * 6. 与原版的有意偏差（安全加固）：空集合值跳过条件生成；排序列剔除内容中的反引号
 *
 * @param <T> 实体类型
 * @param <U> 查询请求类型
 * @since 3.1.0-healthmall
 */
public class BaseQueryWrapper<T, U extends BaseListReq> {

    /**
     * 不参与查询条件拼装的基类字段
     */
    private static final List<String> SKIP_FIELDS = Arrays.asList(
            "page", "size", "sidx", "sort", "order", "like",
            "createTimeStart", "createTimeEnd", "sourceLang", "serialVersionUID");

    QueryWrapper<T> queryWrapper;

    public BaseQueryWrapper() {
        this.queryWrapper = new QueryWrapper<>();
    }

    public BaseQueryWrapper(U req) {
        this(req, true);
    }

    public BaseQueryWrapper(U req, boolean processDefaultFields) {
        this.queryWrapper = new QueryWrapper<>();
        if (req == null) {
            return;
        }
        buildConditions(req, processDefaultFields);
        buildOrder(req);
    }

    private void buildConditions(U req, boolean processDefaultFields) {
        List<Field> fields = getAllFields(req.getClass());
        for (Field field : fields) {
            if (SKIP_FIELDS.contains(field.getName())
                    || Modifier.isStatic(field.getModifiers())
                    || field.isSynthetic()) {
                continue;
            }

            Object value = getFieldValue(req, field);
            if (isEmptyValue(value)) {
                continue;
            }

            QueryField queryField = field.getAnnotation(QueryField.class);
            if (queryField != null) {
                String column = StrUtil.isNotBlank(queryField.value())
                        ? queryField.value()
                        : camelToSnake(field.getName());
                applyCondition(queryWrapper, column, queryField.type(), value);
            } else if (processDefaultFields && !(value instanceof Collection) && !(value instanceof java.util.Map)) {
                queryWrapper.eq(camelToSnake(field.getName()), value);
            }
        }

        if (StrUtil.isNotBlank(req.getCreateTimeStart())) {
            queryWrapper.ge("create_time", req.getCreateTimeStart());
        }
        if (StrUtil.isNotBlank(req.getCreateTimeEnd())) {
            queryWrapper.le("create_time", req.getCreateTimeEnd());
        }
    }

    private void applyCondition(QueryWrapper<T> wrapper, String column, QueryType type, Object value) {
        switch (type) {
            case EQ -> wrapper.eq(column, value);
            case NE -> wrapper.ne(column, value);
            case GT -> wrapper.gt(column, value);
            case GE -> wrapper.ge(column, value);
            case LT -> wrapper.lt(column, value);
            case LE -> wrapper.le(column, value);
            case LIKE -> wrapper.like(column, value);
            case NOT_LIKE -> wrapper.notLike(column, value);
            case LIKE_LEFT -> wrapper.likeLeft(column, value);
            case LIKE_RIGHT -> wrapper.likeRight(column, value);
            case IS_NULL -> wrapper.isNull(column);
            case IS_NOT_NULL -> wrapper.isNotNull(column);
            case IN -> {
                if (value instanceof Collection<?> collection) {
                    wrapper.in(column, collection);
                } else {
                    wrapper.in(column, Collections.singletonList(value));
                }
            }
            case NOT_IN -> {
                if (value instanceof Collection<?> collection) {
                    wrapper.notIn(column, collection);
                } else {
                    wrapper.notIn(column, Collections.singletonList(value));
                }
            }
            case IN_STR -> wrapper.in(column, Arrays.asList(value.toString().split(",")));
            case NOT_IN_STR -> wrapper.notIn(column, Arrays.asList(value.toString().split(",")));
            case FIND_IN_SET -> wrapper.apply("FIND_IN_SET({0}, " + column + ")", value);
            case FIND_IN_SET_STR -> wrapper.apply("FIND_IN_SET({0}, " + column + ")", value.toString());
        }
    }

    private void buildOrder(U req) {
        if (StrUtil.isNotBlank(req.getSidx())) {
            appendOrder(req.getSidx(), req.getSort());
        }

        if (CollUtil.isNotEmpty(req.getOrder())) {
            for (BaseOrder order : req.getOrder()) {
                if (order == null || StrUtil.isBlank(order.getSidx())) {
                    continue;
                }
                appendOrder(order.getSidx(), order.getSort());
            }
        }
    }

    private void appendOrder(String sidx, String sort) {
        String column = "`" + sidx.replace("`", "") + "`";
        if (Constants.ORDER_BY_ASC.equalsIgnoreCase(sort)) {
            queryWrapper.orderByAsc(column);
        } else {
            queryWrapper.orderByDesc(column);
        }
    }

    /**
     * 获取查询条件（可选指定 select 列）
     */
    public QueryWrapper<T> getWrapper(String... columns) {
        if (columns != null && columns.length > 0) {
            queryWrapper.select(columns);
        }
        return queryWrapper;
    }

    /**
     * 获取查询条件（带 select 列），与 getWrapper 等价，保留双入口兼容
     */
    public QueryWrapper<T> getWrapperWith(String... columns) {
        return getWrapper(columns);
    }

    /**
     * 多值 FIND_IN_SET 逐项 AND 拼接
     */
    public static <T, M> void handleFindInSet(List<M> values, String column, QueryWrapper<T> wrapper) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        for (M value : values) {
            if (value != null) {
                wrapper.apply("FIND_IN_SET({0}, " + column + ")", value);
            }
        }
    }

    /**
     * 转义特殊字符（原版行为：单引号 SQL 标准双写，反斜杠/双引号加反斜杠）
     */
    public static String addslashes(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("\\", "\\\\")
                .replace("'", "''")
                .replace("\"", "\\\"");
    }

    /**
     * null / 空集合 / 空白串 均视为未传，跳过条件生成。
     * [healthmall-ext] 空白串跳过：管理端前端列表页固定携带空参数（如 product_tags=），
     * 若生成 `column = ''` 条件会导致查空（库里该列多为 NULL）；原版经请求绑定层把空串转 null，
     * Spring 绑定不转换，故在此等价处理。如需按"空串"过滤请使用显式 @QueryField 条件。
     */
    private boolean isEmptyValue(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Collection<?> collection) {
            return collection.isEmpty();
        }
        if (value instanceof String str) {
            return StrUtil.isBlank(str);
        }
        return false;
    }

    private String camelToSnake(String name) {
        StringBuilder builder = new StringBuilder(name.length() + 4);
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append('_').append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 沿类继承链收集全部声明字段（子类在前）
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new java.util.ArrayList<>();
        for (Class<?> current = clazz; current != null && current != Object.class; current = current.getSuperclass()) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
        }
        return fields;
    }

    private Object getFieldValue(Object target, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("读取查询字段失败: " + field.getName(), e);
        }
    }
}
