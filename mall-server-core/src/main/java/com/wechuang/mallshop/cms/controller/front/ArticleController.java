package com.wechuang.mallshop.cms.controller.front;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.cms.model.entity.ArticleCategory;
import com.wechuang.mallshop.cms.model.req.ArticleBaseListReq;
import com.wechuang.mallshop.cms.model.req.ArticleCategoryListReq;
import com.wechuang.mallshop.cms.model.res.ArticleBaseRes;
import com.wechuang.mallshop.cms.service.ArticleBaseService;
import com.wechuang.mallshop.cms.service.ArticleCategoryService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "文章内容-移动端")
@RestController
@RequestMapping("/front/cms/articleBase")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleBaseService articleBaseService;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Operation(summary = "文章列表", description = "文章列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ArticleBaseRes>> list(ArticleBaseListReq articleBaseListReq) {
        IPage<ArticleBaseRes> pageList = articleBaseService.getArticleBaseList(articleBaseListReq);

        return success(pageList);
    }

    @Operation(summary = "文章详情", description = "文章列表")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<ArticleBaseRes> get(@RequestParam("article_id") Integer articleId) {
        ArticleBaseRes articleBaseRes = articleBaseService.getArticleBase(articleId);

        return success(articleBaseRes);
    }

    @Operation(summary = "文章分类-分页列表查询", description = "文章分类-分页列表查询")
    @RequestMapping(value = "/listCategory", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ArticleCategory>> list(ArticleCategoryListReq articleCategoryListReq) {
        IPage<ArticleCategory> pageList = articleCategoryService.lists(articleCategoryListReq);

        return success(pageList);
    }
}
