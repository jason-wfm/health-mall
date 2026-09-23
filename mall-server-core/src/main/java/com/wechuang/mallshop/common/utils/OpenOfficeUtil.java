package com.wechuang.mallshop.common.utils;

import cn.hutool.core.util.StrUtil;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;

/**
 * OpenOfficeUtil - Spring Boot 3 兼容版本
 * 使用 jodconverter-spring-boot-starter
 *
 * @author Xinze
 * @since 2018-12-14 08:38:19
 */
@Component
public class OpenOfficeUtil {
    private static final Logger logger = LoggerFactory.getLogger(OpenOfficeUtil.class);

    // 支持转换pdf的文件后缀列表
    private static final String[] CAN_CONVERTER_FILES = new String[]{
            "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    };

    @Autowired(required = false)  // 添加 required = false
    private static DocumentConverter documentConverter;

    /**
     * 文件转pdf
     *
     * @param filePath 源文件路径
     * @param outDir   输出目录
     * @return File
     */
    public static File converterToPDF(String filePath, String outDir) {
        return converterToPDF(filePath, outDir, true);
    }

    /**
     * 文件转pdf
     *
     * @param filePath 源文件路径
     * @param outDir   输出目录
     * @param cache    是否使用上次转换过的文件
     * @return File
     */
    public static File converterToPDF(String filePath, String outDir, boolean cache) {
        // 检查 DocumentConverter 是否可用
        if (documentConverter == null) {
            logger.error("DocumentConverter 不可用，请检查Office配置");
            return null;
        }

        if (StrUtil.isBlank(filePath)) {
            logger.warn("文件路径为空");
            return null;
        }

        File srcFile = new File(filePath);
        if (!srcFile.exists()) {
            logger.warn("源文件不存在: {}", filePath);
            return null;
        }

        // 检查文件格式是否支持转换
        if (!canConverter(filePath)) {
            logger.warn("不支持转换的文件格式: {}", filePath);
            return null;
        }

        // 生成输出文件名（保持原有逻辑）
        String outPath = Base64.getEncoder().encodeToString(filePath.getBytes())
                .replace("/", "-").replace("+", "-");
        File outFile = new File(outDir, outPath + ".pdf");

        // 缓存检查
        if (cache && outFile.exists()) {
            logger.info("使用缓存文件: {}", outFile.getAbsolutePath());
            return outFile;
        }

        // 转换文件
        return convertFile(srcFile, outFile);
    }

    /**
     * 转换文件
     *
     * @param srcFile 源文件
     * @param outFile 输出文件
     * @return File
     */
    private static File convertFile(File srcFile, File outFile) {
        try {
            // 创建输出目录
            if (!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs()) {
                logger.error("创建输出目录失败: {}", outFile.getParent());
                return null;
            }

            // 使用 jodconverter 进行转换
            documentConverter.convert(srcFile).to(outFile).execute();

            logger.info("文件转换成功: {} -> {}", srcFile.getName(), outFile.getName());
            return outFile;

        } catch (OfficeException e) {
            logger.error("文件转换失败: {}", srcFile.getAbsolutePath(), e);
            return null;
        } catch (Exception e) {
            logger.error("文件转换发生异常: {}", srcFile.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * 判断文件后缀是否可以转换pdf
     *
     * @param path 文件路径
     * @return boolean
     */
    public static boolean canConverter(String path) {
        if (StrUtil.isBlank(path)) {
            return false;
        }

        try {
            String suffix = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
            return Arrays.asList(CAN_CONVERTER_FILES).contains(suffix);
        } catch (Exception e) {
            logger.debug("解析文件后缀失败: {}", path, e);
            return false;
        }
    }

    /**
     * 获取支持的文件格式列表
     *
     * @return 支持的文件格式数组
     */
    public String[] getSupportedFormats() {
        return CAN_CONVERTER_FILES.clone();
    }
}