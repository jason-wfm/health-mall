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
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.invoicing.model.entity.StockBillItem;
import com.wechuang.mallshop.invoicing.model.req.StockBillItemListReq;
import com.wechuang.mallshop.invoicing.service.StockBillItemService;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.input.ProductEditStockInput;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.output.ItemOutput;
import com.wechuang.mallshop.pt.model.req.ProductEditStockReq;
import com.wechuang.mallshop.pt.model.req.ProductItemLevelDiscountReq;
import com.wechuang.mallshop.pt.model.req.ProductItemListReq;
import com.wechuang.mallshop.pt.model.req.ProductItemStateEditReq;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.model.res.ProductItemLevelDiscountRes;
import com.wechuang.mallshop.pt.service.ProductCategoryService;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import com.wechuang.mallshop.pt.service.ProductItemService;
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
import java.util.Collections;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 商品SKU表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-03-13
 */
@Tag(name = "商品SKU表")
@RestController
@RequestMapping("/manage/pt/productItem")
public class ProductItemController extends BaseController {
    @Autowired
    private ProductItemService productItemService;
    @Resource
    private ProductIndexService productIndexService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private StockBillItemService stockBillItemService;

    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "商品SKU表-分页列表查询", description = "商品SKU表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<ItemListRes> list(ProductItemListReq req) {
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
                input.setCategoryId(CollUtil.join(categoryLeafs, ","));
            } else {
                input.setCategoryId(req.getCategoryId().toString());
            }
        }

        ItemListRes pageList = productIndexService.listItem(input);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "更改库存", description = "更改库存")
    @RequestMapping(value = "/editStock", method = RequestMethod.POST)
    public CommonRes<?> editStock(ProductEditStockReq req) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductItem productItem = productItemService.get(req.getItemId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), productItem, ProductItem::getStoreId)) {
            ProductEditStockInput input = BeanUtil.copyProperties(req, ProductEditStockInput.class);
            boolean success = productItemService.batchEditStock(Collections.singletonList(input));

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "修改状态-是否启用", description = "修改状态-是否启用")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(ProductItemStateEditReq req) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductItem item = productItemService.get(req.getItemId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), item, ProductItem::getStoreId)) {
            ProductItem productItem = BeanUtil.copyProperties(req, ProductItem.class);
            boolean success = productItemService.editState(productItem);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "出入库单据item表-分页列表查询", description = "出入库单据item表-分页列表查询")
    @RequestMapping(value = "/getStockBillItems", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StockBillItem>> getStockBillItems(StockBillItemListReq stockBillItemListReq) {
        IPage<StockBillItem> pageList = stockBillItemService.lists(stockBillItemListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "库存警告商品item-分页列表查询", description = "库存警告商品item-分页列表查询")
    @RequestMapping(value = "/getStockWarningItems", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ItemOutput>> getStockWarningItems(ProductItemListReq productItemListReq) {
        ProductItemInput input = new ProductItemInput();
        BeanUtils.copyProperties(productItemListReq, input);

        if (CheckUtil.isNotEmpty(productItemListReq.getItemId())) {
            input.setItemId(Convert.toList(Long.class, productItemListReq.getItemId()));
        }

        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser != null) {
            Integer storeId = loginUser.getStoreId();

            if (CheckUtil.isNotEmpty(storeId) && loginUser.isStore()) {
                input.setStoreId(storeId);
            }
        }

        IPage<ItemOutput> pageList = productItemService.getStockWarningItems(input);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "导出模版-批量修改价格、库存", description = "导出模版-批量修改价格、库存功能")
    @RequestMapping(value = "/exportTemp", method = RequestMethod.GET)
    public void exportTemp(HttpServletResponse response) {
        productItemService.exportTemp(response);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "导入-批量修改价格、库存", description = "导入-批量修改价格、库存")
    @RequestMapping(value = "/importTemp", method = RequestMethod.POST)
    public CommonRes<?> importTemp(@RequestParam MultipartFile file) throws Exception {
        productItemService.importTemp(file);

        return success();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "商品SKU折扣列表", description = "商品SKU折扣列表")
    @RequestMapping(value = "/levelDiscountList", method = RequestMethod.GET)
    public CommonRes<ProductItemLevelDiscountRes> levelDiscountList(@RequestParam(value = "product_id") Integer productId) {
        ProductItemLevelDiscountRes levelDiscountRes = productItemService.levelDiscountList(productId);

        return success(levelDiscountRes);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBase/edit')")
    @Operation(summary = "修改商品SKU折扣", description = "修改商品SKU折扣")
    @RequestMapping(value = "/editLevelDiscounts", method = RequestMethod.POST)
    public CommonRes<?> editLevelDiscounts(ProductItemLevelDiscountReq req) {

        return success(productItemService.editLevelDiscounts(req));
    }
}

