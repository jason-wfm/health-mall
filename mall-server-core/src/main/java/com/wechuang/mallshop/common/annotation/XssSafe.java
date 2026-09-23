package com.wechuang.mallshop.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XssSafe {

    /**
     * 处理模式：ESCAPE-转义, CLEAN-清理, RICH_TEXT-富文本
     */
    Mode mode() default Mode.ESCAPE;

    enum Mode {
        ESCAPE,      // 普通文本转义
        CLEAN,       // 严格清理（移除HTML）
        RICH_TEXT    // 富文本清理（允许安全标签）
    }
}