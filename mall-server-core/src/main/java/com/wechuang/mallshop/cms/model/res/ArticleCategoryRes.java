package com.wechuang.mallshop.cms.model.res;


import com.wechuang.mallshop.cms.model.entity.ArticleCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "文章前端对象")
public class ArticleCategoryRes extends ArticleCategory {
    @Schema(description = "文章子集")
    private List<ArticleCategoryRes> children;
}
