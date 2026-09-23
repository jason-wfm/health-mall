package com.wechuang.mallshop.cms.model.res;


import com.wechuang.mallshop.cms.model.entity.ArticleBase;
import com.wechuang.mallshop.cms.model.entity.ArticleTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "文章内容前端对象")
public class ArticleBaseRes extends ArticleBase {

    @Schema(description = "文章标签集合")
    private List<ArticleTag> articleTagList;

    @Schema(description = "用户昵称")
    private String userNickname;
}
