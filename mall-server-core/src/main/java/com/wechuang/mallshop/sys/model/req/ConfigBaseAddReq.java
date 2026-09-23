package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统参数设置表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "系统参数设置表参数")
public class ConfigBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置编码")
    private String configKey;

    @Schema(description = "配置标题")
    private String configTitle;

    @Schema(description = "配置类型(ENUM):readonly-只读文本;number-数字;text-单行文本;textarea-多行文本;array-数组;password-密码;radio-单选框;checkbox-复选框;select-下拉框;icon-字体图标;date-日期;datetime-时间;image-单张图片;images-多张图片;file-单个文件;files-多个文件;ueditor-富文本编辑器")
    private String configDatatype;

    @Schema(description = "配置项")
    private String configOptions;

    @Schema(description = "配置值")
    private String configValue = "";

    @Schema(description = "所属分类")
    private Integer configTypeId;

    @Schema(description = "配置注释")
    private String configNote;

    @Schema(description = "配置排序:从小到大")
    private Integer configSort;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    private Boolean configEnable;


}
