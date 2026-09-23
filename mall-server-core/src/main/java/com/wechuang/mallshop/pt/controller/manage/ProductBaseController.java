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
package com.wechuang.mallshop.pt.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.pt.model.entity.*;
import com.wechuang.mallshop.pt.model.input.ProductIndexInput;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.input.ProductSaveInput;
import com.wechuang.mallshop.pt.model.output.ProductDataOutput;
import com.wechuang.mallshop.pt.model.req.*;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.model.res.ProductDataRes;
import com.wechuang.mallshop.pt.model.res.ProductListRes;
import com.wechuang.mallshop.pt.model.res.ProductSaveRes;
import com.wechuang.mallshop.pt.service.ProductBaseService;
import com.wechuang.mallshop.pt.service.ProductCategoryService;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商品基础表-SPU表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-03-20
 */
@Tag(name = "商品基础表-SPU表")
@RestController
@RequestMapping("/manage/pt/productBase")
public class ProductBaseController extends BaseController {
    @Autowired
    private ProductBaseService productBaseService;

    @Resource
    private ProductIndexService productIndexService;

    @Autowired
    private ProductCategoryService productCategoryService;


    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "分页列表查询", description = "分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<ProductListRes> list(ProductIndexListReq productIndexListReq) {
        ProductIndexInput input = new ProductIndexInput();
        BeanUtils.copyProperties(productIndexListReq, input);

        if (CheckUtil.isNotEmpty(productIndexListReq.getCategoryId())) {
            List<Integer> categoryLeafs = productCategoryService.getCategoryLeafs(productIndexListReq.getCategoryId(), productIndexListReq.getSourceLang(), ContextUtil.getSiteId());
            if (CollUtil.isNotEmpty(categoryLeafs)) {
                input.setCategoryId(CollUtil.join(categoryLeafs, ","));
            } else {
                input.setCategoryId(productIndexListReq.getCategoryId().toString());
            }
        }

        ProductListRes pageList = productIndexService.listItem(input);

        return success(pageList);
    }

    @Operation(summary = "通过product_id查询", description = "通过product_id查询")
    @RequestMapping(value = "/getProductData", method = RequestMethod.GET)
    public CommonRes<ProductDataRes> getProduct(@RequestParam("product_id") Long productId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductBase productBase = productBaseService.get(productId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), productBase, ProductBase::getStoreId)) {
            ProductDataOutput productDataOutput = productBaseService.getProduct(productId);

            ProductDataRes productDateRes = new ProductDataRes();
            BeanUtils.copyProperties(productDataOutput, productDateRes);

            return success(productDateRes);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "保存商品", description = "保存商品")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonRes<ProductSaveRes> save(ProductSaveReq productSaveReq) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser != null) {
            Integer storeId = loginUser.getStoreId();

            if (CheckUtil.isNotEmpty(storeId) && loginUser.isStore()) {
                productSaveReq.setStoreId(storeId);
            }
        }

        ProductSaveInput productSaveInput = new ProductSaveInput();
        productSaveInput.setProductBase(BeanUtil.copyProperties(productSaveReq, ProductBase.class));
        productSaveInput.setProductIndex(BeanUtil.copyProperties(productSaveReq, ProductIndex.class));
        productSaveInput.setProductInfo(BeanUtil.copyProperties(productSaveReq, ProductInfo.class));
        productSaveInput.setProductValidPeriod(BeanUtil.copyProperties(productSaveReq, ProductValidPeriod.class));

        List<ProductItem> productItems = BeanUtil.copyToList(JSONUtil.parseArray(productSaveReq.getProductItems(), Map.class), ProductItem.class);
        productSaveInput.setProductItems(productItems);

        List<ProductImage> productImages = BeanUtil.copyToList(JSONUtil.parseArray(productSaveReq.getProductImages(), Map.class), ProductImage.class);
        productSaveInput.setProductImages(productImages);

        boolean success = productBaseService.saveProduct(productSaveInput);

        ProductSaveRes res = new ProductSaveRes();
        res.setProductId(productSaveInput.getProductBase().getProductId());

        if (success) {
            return success(res);
        }

        return fail(res);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/remove')")
    @Operation(summary = "通过product_id删除", description = "通过product_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("product_id") Long productId) {
        boolean success = productBaseService.removeProduct(productId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/remove')")
    @Operation(summary = "批量删除", description = "批量删除")
    @RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
    public CommonRes<?> removeBatch(@RequestParam("product_id") String productIds) {
        for (Long productId : Convert.toList(Long.class, productIds)) {
            boolean success = productBaseService.removeProduct(productId);
        }

        return success();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "商品状态", description = "商品状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(ProductBaseStateEditReq productBaseStateEditReq) {
        boolean success = productBaseService.batchEditState(Collections.singletonList(productBaseStateEditReq.getProductId()), productBaseStateEditReq.getProductStateId(), null);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "三级分销状态", description = "三级分销状态")
    @RequestMapping(value = "/editEnable", method = RequestMethod.POST)
    public CommonRes<?> editEnable(ProductIndexEnableEditReq productIndexEnableEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            ProductIndex productIndex = BeanUtil.copyProperties(productIndexEnableEditReq, ProductIndex.class);

            if (Boolean.TRUE.equals(productIndexEnableEditReq.getProductPlusEnable())) {
                throw new BusinessException(__("开源版不支持PLUS商品"));
            }

            boolean success = productIndexService.edit(productIndex);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "平台佣金比率", description = "平台佣金比率")
    @RequestMapping(value = "/editCommissionRate", method = RequestMethod.POST)
    public CommonRes<?> editCommissionRate(ProductBaseRateEditReq productBaseRateEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            ProductBase productBase = BeanUtil.copyProperties(productBaseRateEditReq, ProductBase.class);
            boolean success = productBaseService.edit(productBase);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @Operation(summary = "商品SKU列表查询", description = "商品SKU列表查询")
    @RequestMapping(value = "/listItem", method = RequestMethod.GET)
    public CommonRes<ItemListRes> listItem(ProductItemListReq req) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser != null) {
            Integer storeId = loginUser.getStoreId();

            if (CheckUtil.isNotEmpty(storeId)) {
                req.setStoreId(storeId);
            }
        }
        ProductItemInput input = new ProductItemInput();
        BeanUtils.copyProperties(req, input);

        input.setItemId(Convert.toList(Long.class, req.getItemId()));

        if (CheckUtil.isNotEmpty(req.getCategoryId())) {
            List<Integer> categoryLeafs = productCategoryService.getCategoryLeafs(req.getCategoryId(), req.getSourceLang(), ContextUtil.getSiteId());
            if (CollUtil.isNotEmpty(categoryLeafs)) {
                input.setCategoryIds(categoryLeafs);
            } else {
                input.setCategoryIds(Collections.singletonList(req.getCategoryId()));
            }
        }

        ItemListRes pageList = productIndexService.listItem(input);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "批量修改商品状态", description = "批量修改商品状态")
    @RequestMapping(value = "/batchEditState", method = RequestMethod.POST)
    public CommonRes<?> batchEditState(@RequestParam("product_ids") String productIds,
                                       @RequestParam("product_state_id") Integer productStateId) {
        boolean success = productBaseService.batchEditState(Convert.toList(Long.class, productIds), productStateId, null);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "修改产品索引表-商品排序", description = "修改产品索引表-商品排序")
    @RequestMapping(value = "/editSort", method = RequestMethod.POST)
    public CommonRes<?> editSort(ProductIndexOrderEditReq productIndexOrderEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            ProductIndex productIndex = BeanUtil.copyProperties(productIndexOrderEditReq, ProductIndex.class);
            boolean success = productIndexService.edit(productIndex);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "导出模版", description = "导出模版")
    @RequestMapping(value = "/exportTemp", method = {RequestMethod.POST, RequestMethod.GET})
    public void exportTemp(HttpServletResponse response) {
        productBaseService.exportTemp(response);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "导入单规格商品", description = "导入单规格商品")
    @RequestMapping(value = "/importTemp", method = RequestMethod.POST)
    public CommonRes<?> importTemp(@RequestParam MultipartFile file) throws Exception {
        productBaseService.importTemp(file);

        return success();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "商品审核", description = "商品审核")
    @RequestMapping(value = "/editVerify", method = RequestMethod.POST)
    public CommonRes<?> editVerify(ProductIndexVerifyEditReq productIndexVerifyEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            ProductIndex productIndex = BeanUtil.copyProperties(productIndexVerifyEditReq, ProductIndex.class);
            boolean success = productIndexService.editVerify(productIndex);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "商品违规禁售", description = "商品违规禁售")
    @RequestMapping(value = "/violation", method = RequestMethod.POST)
    public CommonRes<?> violation(ProductIndexStateEditReq productIndexStateEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            boolean success = productBaseService.batchEditState(Convert.toList(Long.class, productIndexStateEditReq.getProductId()), StateCode.PRODUCT_STATE_ILLEGAL, productIndexStateEditReq.getProductStateRemark());

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

}

