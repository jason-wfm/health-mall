package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 素材表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "添加上传参数")
public class MaterialBaseUploadReq {
    private static final long serialVersionUID = 1L;

    @Schema(description = "素材分类")
    private Long galleryId;

    @Schema(description = "素材类型")
    private String materialType;

    @Schema(description = "素材关键字")
    private String materialKey;
}
