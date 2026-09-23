// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.pt.controller.front;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.model.entity.ProductUnit;
import com.wechuang.mallshop.pt.model.input.ProductDetailInput;
import com.wechuang.mallshop.pt.model.input.ProductIndexInput;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.req.*;
import com.wechuang.mallshop.pt.model.res.*;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.service.*;
import com.wechuang.mallshop.shop.model.entity.UserContrastItem;
import com.wechuang.mallshop.shop.model.entity.UserFavoritesItem;
import com.wechuang.mallshop.shop.service.UserContrastItemService;
import com.wechuang.mallshop.shop.service.UserFavoritesItemService;
import com.wechuang.mallshop.shop.service.UserProductBrowseService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.model.entity.UserCart;
import com.wechuang.mallshop.trade.service.UserCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Tag(name = "商品信息")
@RestController
@RequestMapping("/front/pt/product")
public class ProductController extends BaseController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Resource
    private ProductIndexService productIndexService;

    @Autowired
    private UserProductBrowseService productProductBrowseService;

    @Autowired
    private UserFavoritesItemService userFavoritesItemService;

    @Autowired
    private UserContrastItemService userContrastItemService;

    @Autowired
    private ProductCommentService productCommentService;

    @Autowired
    private ProductBrandService productBrandService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private ProductUnitService productUnitService;

    @Autowired
    private UserCartService userCartService;

    @Operation(summary = "商品列表查询", description = "商品列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<ProductListRes> list(ProductIndexListReq productIndexListReq) {
        ProductIndexInput input = new ProductIndexInput();
        BeanUtils.copyProperties(productIndexListReq, input);
        input.setStoreIsOpen(true);

        if (configBaseService.ifIndustry()) {
            ContextUser loginUser = ContextUtil.getLoginUser();

            if (loginUser != null) {
                Integer industryId = loginUser.getIndustryId();

                if (CheckUtil.isNotEmpty(industryId)) {
                    input.setIndustryIds(industryId.toString());
                }
            }
        }

        if (CheckUtil.isNotEmpty(productIndexListReq.getCategoryId())) {
            List<Integer> categoryLeafs = productCategoryService.getCategoryLeafs(productIndexListReq.getCategoryId(), productIndexListReq.getSourceLang(), productIndexListReq.getSiteId());
            if (CollUtil.isNotEmpty(categoryLeafs)) {
                input.setCategoryId(CollUtil.join(categoryLeafs, ","));
            } else {
                input.setCategoryId(productIndexListReq.getCategoryId().toString());
            }
        }

        //上架商品
        input.setProductStateId(StateCode.PRODUCT_STATE_NORMAL);
        input.setProductVerifyId(StateCode.PRODUCT_VERIFY_PASSED);

        ProductListRes pageList = productIndexService.listItem(input);

        return success(pageList);
    }

    @Operation(summary = "商品分类表-分页列表查询", description = "商品分类表-分页列表查询")
    @RequestMapping(value = "/listCategory", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ProductCategory>> listCategory(ProductCategoryListReq productCategoryListReq) {
        productCategoryListReq.setCategoryIsEnable(true);
        IPage<ProductCategory> pageList = productCategoryService.lists(productCategoryListReq);

        return success(pageList);
    }

    @Operation(summary = "商品分类表-分页列表查询", description = "商品分类表-分页列表查询")
    @RequestMapping(value = "/listAllCategory", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ProductCategory>> listAllCategory(ProductCategoryListReq productCategoryListReq) {
        productCategoryListReq.setCategoryParentId(null);
        productCategoryListReq.setCategoryIsEnable(true);
        productCategoryListReq.setSize(99999);
        IPage<ProductCategory> pageList = productCategoryService.lists(productCategoryListReq);

        for (ProductCategory item : pageList.getRecords()) {
            item.setId(item.getCategoryId());
            item.setParentId(item.getCategoryParentId());
            item.setName(item.getCategoryName());
        }

        return success(pageList);
    }

    @Operation(summary = "商品树形分类", description = "商品树形分类")
    @RequestMapping(value = "/treeCategory", method = RequestMethod.GET)
    public CommonRes<List<ProductCategoryRes>> treeCategory(ProductCategoryListReq productCategoryListReq) {
        productCategoryListReq.setCategoryIsEnable(true);
        List<ProductCategoryRes> categoryRes = productCategoryService.getTree(productCategoryListReq.getCategoryParentId(), productCategoryListReq.getCategoryIsEnable(), "", productCategoryListReq.getSourceLang(), productCategoryListReq.getSiteId());

        return success(categoryRes);
    }

    @Operation(summary = "商品分类过滤属性", description = "商品分类过滤属性")
    @RequestMapping(value = "/getSearchFilter", method = RequestMethod.GET)
    public CommonRes<ProductCategoryFilterRes> getSearchFilter(@RequestParam("category_id") Integer categoryId) {
        ProductCategoryFilterRes categoryRes = productCategoryService.getCategoryFilter(categoryId);

        return success(categoryRes);
    }

    @Operation(summary = "商品详情", description = "商品详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonRes<ProductDetailRes> detail(ProductDetailReq req) {
        Integer userId = ContextUtil.getLoginUserId();
        ProductDetailInput input = new ProductDetailInput();
        BeanUtils.copyProperties(req, input);
        input.setUserId(userId);

        ProductDetailRes detailRes = productIndexService.detail(input);

        //添加浏览记录
        if (CheckUtil.isNotEmpty(userId)) {
            productProductBrowseService.addBrowser(req.getItemId(), userId);

            //是否收藏
            long count = userFavoritesItemService.count(new QueryWrapper<UserFavoritesItem>().eq("user_id", userId).eq("item_id", req.getItemId()));

            if (count > 0) {
                detailRes.setIsFavorite(true);
            }

            //是否对比
            detailRes.setIsCompare(false);
            UserContrastItem userContrastItem = userContrastItemService.get(userId);
            if(userContrastItem != null){
               List<Long> itemIds = Convert.toList(Long.class, userContrastItem.getItemIds());
               if (itemIds.contains(req.getItemId())){
                   detailRes.setIsCompare(true);
               }
            }
        }


        return success(detailRes);
    }

    @Operation(summary = "商品活动信息", description = "商品活动信息")
    @RequestMapping(value = "/getActivityInfo", method = RequestMethod.GET)
    public CommonRes<ActivityInfoRes> getActivityInfo(@RequestParam("item_id") Long itemId) {
        ActivityInfoRes res = productIndexService.getActivityInfo(itemId);

        return success(res);
    }

    @Operation(summary = "商品SKU列表查询", description = "商品SKU列表查询")
    @RequestMapping(value = "/listItem", method = RequestMethod.GET)
    public CommonRes<ItemListRes> listItem(ProductItemListReq req) {
        ProductItemInput input = new ProductItemInput();
        BeanUtils.copyProperties(req, input);

        if (configBaseService.ifIndustry()) {
            ContextUser loginUser = ContextUtil.getLoginUser();

            if (loginUser != null) {
                Integer industryId = loginUser.getIndustryId();

                if (CheckUtil.isNotEmpty(industryId)) {
                    input.setIndustryIds(industryId.toString());
                }
            }
        }

        input.setItemId(Convert.toList(Long.class, req.getItemId()));

        if (CheckUtil.isNotEmpty(req.getCategoryId())) {
            List<Integer> categoryLeafs = productCategoryService.getCategoryLeafs(req.getCategoryId(), req.getSourceLang(), req.getSiteId());
            if (CollUtil.isNotEmpty(categoryLeafs)) {
                input.setCategoryId(CollUtil.join(categoryLeafs, ","));
                input.setCategoryIds(categoryLeafs);
            } else {
                input.setCategoryId(req.getCategoryId().toString());
                input.setCategoryIds(Convert.toList(Integer.class, req.getCategoryId()));
            }
        }

        ItemListRes pageList = productIndexService.listItem(input);

        return success(pageList);
    }

    @Operation(summary = "商品评价表查询", description = "商品评价表查询")
    @RequestMapping(value = "/getComment", method = RequestMethod.GET)
    public CommonRes<ProductCommentRes> getComment(ProductCommentListReq req) {
        ProductCommentRes commentRes = productCommentService.getComment(req);
        return success(commentRes);
    }

    @Operation(summary = "商品品牌列表", description = "商品品牌列表")
    @RequestMapping(value = "/brand", method = RequestMethod.GET)
    public CommonRes<List<ProductBrandRes>> getBrand() {
        List<ProductBrandRes> commentRes = productBrandService.getTree();
        return success(commentRes);
    }

    @Operation(summary = "商品计量单位表-分页列表查询", description = "商品计量单位表-分页列表查询")
    @RequestMapping(value = "/unitList", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ProductUnit>> unitList(ProductUnitListReq productUnitListReq) {
        IPage<ProductUnit> pageList = productUnitService.lists(productUnitListReq);

        return success(pageList);
    }


    @Operation(summary = "plus商品列表", description = "plus商品列表")
    @RequestMapping(value = "/listProduct", method = RequestMethod.GET)
    public CommonRes<ProductListRes> listProduct(ProductIndexInput input) {

        // 商品状态
        input.setProductStateId(StateCode.PRODUCT_STATE_NORMAL);
        input.setProductVerifyId(StateCode.PRODUCT_VERIFY_PASSED);

        Integer categoryId = Convert.toInt(input.getCategoryId());
        if (CheckUtil.isNotEmpty(input.getCategoryId())) {
            List<Integer> categoryLeafs = productCategoryService.getCategoryLeafs(categoryId, input.getSourceLang(), ContextUtil.getSiteId());
            if (CollUtil.isNotEmpty(categoryLeafs)) {
                input.setCategoryId(CollUtil.join(categoryLeafs, ","));
            } else {
                input.setCategoryId(input.getCategoryId().toString());
            }
        }

        ProductListRes pageList = productIndexService.listProduct(input);

        return success(pageList);
    }

    @Operation(summary = "商品", description = "商品")
    @RequestMapping(value = "/skuItem", method = RequestMethod.GET)
    public CommonRes<ProductItemVo> skuItem(@RequestParam("item_id") Long itemId) {
        Integer userId = ContextUtil.getLoginUserId();

        ProductItemVo itemVo = productIndexService.skuItem(itemId, userId);

        if (userId != null) {
            UserCart userCart = userCartService.findOne(
                    new QueryWrapper<UserCart>().
                            eq("item_id", itemId).
                            eq("user_id", userId)
            );
            if (userCart != null) {
                itemVo.setCartId(userCart.getCartId());
                itemVo.setCartQuantity(userCart.getCartQuantity());
            }
        }

        return success(itemVo);
    }

}

