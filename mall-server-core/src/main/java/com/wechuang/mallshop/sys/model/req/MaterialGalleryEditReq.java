package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 素材分类表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "素材分类表参数")
public class MaterialGalleryEditReq extends MaterialGalleryAddReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分类编号")
    private Long galleryId;


}
