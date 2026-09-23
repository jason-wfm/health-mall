package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 素材表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "素材表参数")
public class MaterialBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description = "素材标题")
    private String materialName;

    @Schema(description = "素材描述")
    private String materialDesc;

    @Schema(description = "分类编号")
    private String galleryId;

    @Schema(description = "分类编号")
    private String materialType;

    @Schema(description = "文件URL")
    private String materialUrl;


}
