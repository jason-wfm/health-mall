package com.wechuang.mallshop.common.config;


import com.wechuang.mallshop.common.annotation.XssSafe;
import com.wechuang.mallshop.common.utils.XssUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})
})
@Component
public class XssInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(XssInterceptor.class);

    // 需要跳过的Mapper方法（可选配置）
    private Set<String> skipMethods = new HashSet<>(Arrays.asList("selectById", "selectList", "selectPage", "selectCount",
            "selectByMap", "selectOne", "selectMaps", "selectObjs"));

    // 需要处理的SQL命令类型
    private Set<SqlCommandType> handleCommandTypes = new HashSet<>(Arrays.asList(
            SqlCommandType.INSERT, SqlCommandType.UPDATE
    ));

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        // 快速检查：如果参数为空，直接跳过
        if (parameter == null) {
            return invocation.proceed();
        }

        String methodName = mappedStatement.getId();
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (logger.isDebugEnabled()) {
            logger.debug("拦截方法: {}, 命令类型: {}", methodName, sqlCommandType);
        }

        // 跳过不需要处理的情况
        if (shouldSkip(methodName, sqlCommandType)) {
            return invocation.proceed();
        }

        // 处理参数中的XSS
        Object processedParameter = processParameter(parameter);

        // 只有在参数确实被修改时才替换
        if (processedParameter != parameter) {
            args[1] = processedParameter;
            if (logger.isDebugEnabled()) {
                logger.debug("参数已进行XSS处理");
            }
        }

        return invocation.proceed();
    }

    /**
     * 判断是否跳过处理
     */
    private boolean shouldSkip(String methodName, SqlCommandType sqlCommandType) {
        // 跳过查询操作
        if (sqlCommandType == SqlCommandType.SELECT) {
            return true;
        }

        // 跳过配置的特定方法
        String shortMethodName = methodName.substring(methodName.lastIndexOf(".") + 1);
        if (skipMethods.contains(shortMethodName)) {
            return true;
        }

        // 只处理配置的命令类型
        return !handleCommandTypes.contains(sqlCommandType);
    }

    /**
     * 处理参数
     */
    private Object processParameter(Object parameter) {
        if (parameter == null) {
            return null;
        }

        try {
            if (parameter instanceof Map) {
                // 处理Map参数
                return processMapParameter((Map<?, ?>) parameter);
            } else if (parameter instanceof Collection) {
                // 处理集合参数
                return processCollection((Collection<?>) parameter);
            } else if (parameter.getClass().isArray()) {
                // 处理数组参数
                return processArray((Object[]) parameter);
            } else {
                // 处理实体对象
                return processEntity(parameter);
            }
        } catch (Exception e) {
            logger.warn("XSS处理失败，保持原参数: {}", e.getMessage());
            return parameter; // 处理失败时返回原参数
        }
    }

    /**
     * 处理Map参数
     */
    private Map<?, ?> processMapParameter(Map<?, ?> parameterMap) {
        Map<Object, Object> resultMap = new HashMap<>(parameterMap);

        parameterMap.forEach((key, value) -> {
            if (value instanceof String) {
                // 对Map中的字符串值进行基本XSS防护
                resultMap.put(key, XssUtil.escape((String) value));
            } else if (value != null) {
                // 递归处理嵌套对象
                resultMap.put(key, processParameter(value));
            }
        });

        return resultMap;
    }

    /**
     * 处理集合参数
     */
    private Collection<?> processCollection(Collection<?> collection) {
        List<Object> result = new ArrayList<>();

        for (Object item : collection) {
            result.add(processParameter(item));
        }

        return result;
    }

    /**
     * 处理数组参数
     */
    private Object[] processArray(Object[] array) {
        Object[] result = new Object[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = processParameter(array[i]);
        }

        return result;
    }

    /**
     * 处理实体对象
     */
    private Object processEntity(Object entity) {
        Class<?> clazz = entity.getClass();
        boolean hasXssSafeField = false;

        // 遍历所有字段
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(XssSafe.class) && field.getType() == String.class) {
                hasXssSafeField = true;
                processEntityField(entity, field);
            }
        }

        if (hasXssSafeField) {
            logger.debug("已处理实体: {}", clazz.getSimpleName());
        }

        return entity;
    }

    /**
     * 处理实体字段
     */
    private void processEntityField(Object entity, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(entity);

            if (value instanceof String) {
                String stringValue = (String) value;
                XssSafe annotation = field.getAnnotation(XssSafe.class);

                // 只在值发生变化时处理（避免不必要的处理）
                if (XssUtil.hasXssRisk(stringValue)) {
                    String safeValue = XssUtil.processXss(stringValue, annotation.mode());
                    field.set(entity, safeValue);

                    logger.debug("XSS处理 - 字段: {}, 模式: {}, 原值: {}, 处理后: {}",
                            field.getName(), annotation.mode(), stringValue, safeValue);
                }
            }
        } catch (Exception e) {
            logger.error("处理字段XSS失败: {}.{}", entity.getClass().getSimpleName(), field.getName(), e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以从配置文件中读取属性
        if (properties != null) {
            String skipMethodsStr = properties.getProperty("skipMethods");
            if (skipMethodsStr != null) {
                skipMethods.addAll(Arrays.asList(skipMethodsStr.split(",")));
            }
        }
    }

    /**
     * 添加跳过的方法
     */
    public void addSkipMethod(String methodName) {
        skipMethods.add(methodName);
    }

    /**
     * 添加处理的命令类型
     */
    public void addHandleCommandType(SqlCommandType commandType) {
        handleCommandTypes.add(commandType);
    }
}