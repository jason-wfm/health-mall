package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 素材分类表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "素材分类表参数")
public class MaterialGalleryAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分类名称")
    private String galleryName;

    @Schema(description = "分类描述")
    private String galleryDesc;

    @Schema(description = "素材数量")
    private Integer galleryNum;

    @Schema(description = "是否默认(BOOL):0-否;1-是")
    private Integer galleryIsDefault;

    @Schema(description = "分类排序")
    private Integer gallerySort;

    @Schema(description = "分类类型")
    private String galleryType;

    @Schema(description = "分类封面")
    private String galleryCover;

    @Schema(description = "所属店铺")
    private Integer storeId;


}
