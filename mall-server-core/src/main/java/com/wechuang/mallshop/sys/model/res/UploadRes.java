package com.wechuang.mallshop.sys.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@Schema(name = "上传结果")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UploadRes {
    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件大小")
    private Long FileSize;

    @Schema(description = "文件网址")
    private String FileUrl;

    @Schema(description = "文件网址 等于 FileUrl")
    private String Url;

    @Schema(description = "文件类型:.jpg, 仅仅兼容PC装修")
    private String type = ".jpg";

    @Schema(description = "文件类型")
    private String FileType;

    @Schema(description = "文件路径")
    private String FilePath;

    @Schema(description = "素材类型")
    private String MimeType;

    @Schema(description = "用户编号")
    private Integer UserId;

    @Schema(description = "素材时长:（音频/视频）")
    private String materialDuration;
}
