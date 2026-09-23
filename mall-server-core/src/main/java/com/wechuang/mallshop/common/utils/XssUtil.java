package com.wechuang.mallshop.common.utils;

import com.wechuang.mallshop.common.annotation.XssSafe;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XssUtil {
    /**
     * 根据模式处理XSS
     */
    public static String processXss(String value, XssSafe.Mode mode) {
        if (value == null) {
            return null;
        }

        switch (mode) {
            case ESCAPE:
                return escape(value);
            case CLEAN:
                return cleanStrict(value);
            case RICH_TEXT:
                return cleanRichText(value);
            default:
                return escape(value);
        }
    }

    /**
     * 转义普通文本（防止XSS）
     */
    public static String escape(String input) {
        if (input == null) {
            return null;
        }

        return StringEscapeUtils.escapeHtml4(input);
    }

    /**
     * 清理富文本内容（允许安全的HTML标签）
     */
    public static String cleanRichText(String html) {
        if (html == null) {
            return null;
        }

        Safelist safelist = Safelist.relaxed()
                .addTags("div", "span", "p", "br", "hr")
                .addAttributes(":all", "style", "class", "id")
                .addProtocols("a", "href", "http", "https", "mailto")
                .addProtocols("img", "src", "http", "https")
                .addEnforcedAttribute("a", "rel", "nofollow");

        return Jsoup.clean(html, safelist);
    }

    /**
     * 严格模式清理（移除所有HTML标签）
     */
    public static String cleanStrict(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none());
    }

    /**
     * 检查是否包含XSS风险
     */
    public static boolean hasXssRisk(String input) {
        if (input == null) {
            return false;
        }

        String cleaned = cleanStrict(input);
        return !input.equals(cleaned);
    }
}