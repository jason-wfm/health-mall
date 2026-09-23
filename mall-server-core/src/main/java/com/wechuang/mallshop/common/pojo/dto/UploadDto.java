package com.wechuang.mallshop.common.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "上传文件DTO", description = "上传文件DTO")
public class UploadDto {

    @Schema(description = "上传类型")
    private Integer uploadType;

    @Schema(description = "上传文件")
    private File file;

    @Schema(description = "二进制文件")
    private InputStream inputStream;

    private String materialType;

    private String fileName;
    private Long fileSize;

}
