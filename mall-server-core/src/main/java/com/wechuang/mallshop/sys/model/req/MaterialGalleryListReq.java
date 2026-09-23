package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "素材分类表分页查询")
public class MaterialGalleryListReq extends BaseListReq {
    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺编号")
    private Integer storeId;

    public MaterialGalleryListReq() {
        setSidx("gallery_sort");
    }
}
