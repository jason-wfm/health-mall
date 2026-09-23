package com.wechuang.mallshop.pt.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商品评价回复表参数")
public class ProductCommentReplyReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "评论编号")
    @NotNull(message = "评论编号不能为空")
    private Long commentId;

    @Schema(description = "评论回复内容")
    @NotNull(message = "商品编号不能为空")
    private String commentReplyContent;

}
