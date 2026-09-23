package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 素材表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "素材表分页查询")
public class MaterialBaseListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "素材编号")
    private Long materialId;

    @Schema(description = "附件md5")
    private String materialNumber;

    @Schema(description = "分类编号")
    private Long galleryId;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "文件URL")
    private String materialUrl;

    @Schema(description = "素材来源")
    private String materialSource;

    @Schema(description = "素材排序")
    private Integer materialSort;

    @Schema(description = "素材域名")
    private String materialDomain;

    @Schema(description = "素材path:本地存储")
    private String materialPath;

    @Schema(description = "素材类型")
    private String materialType;

    @Schema(description = "素材高度")
    private Integer materialImageH;

    @Schema(description = "素材宽度")
    private Integer materialImageW;

    @Schema(description = "素材大小")
    private Integer materialSize;

    @Schema(description = "素材类型")
    private String materialMimeType;

    @Schema(description = "metadata")
    private String materialMetadata;

    @Schema(description = "素材alt")
    private String materialAlt;

    @Schema(description = "素材标题")
    @QueryField(type = QueryType.LIKE)
    private String materialName;

    @Schema(description = "素材描述")
    private String materialDesc;

    @Schema(description = "浏览密码")
    private String materialPassword;

    @Schema(description = "素材日期")
    private Date materialTime;

    @Schema(description = "素材时长:（音频/视频）")
    private String materialDuration;

    public MaterialBaseListReq() {
        setSidx("material_id");
        setSort("DESC");
    }
}
