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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.BaseOrder;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pt.model.entity.ProductSpecItem;
import com.wechuang.mallshop.pt.model.req.ProductSpecItemAddReq;
import com.wechuang.mallshop.pt.model.req.ProductSpecItemEditReq;
import com.wechuang.mallshop.pt.model.req.ProductSpecItemListReq;
import com.wechuang.mallshop.pt.service.ProductSpecItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 商品规格值表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-08
 */
@Tag(name = "商品规格值表")
@RestController
@RequestMapping("/manage/pt/productSpecItem")
public class ProductSpecItemController extends BaseController {
    @Autowired
    private ProductSpecItemService productSpecItemService;

    @PreAuthorize("hasAuthority('/manage/pt/productSpec/list')")
    @Operation(summary = "商品规格值表-分页列表查询", description = "商品规格值表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ProductSpecItem>> list(ProductSpecItemListReq productSpecItemListReq) {
        //额外排序
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setSidx("spec_item_id");
        baseOrder.setSort(Constants.ORDER_BY_ASC);

        List<BaseOrder> order = new ArrayList<>();
        order.add(baseOrder);

        productSpecItemListReq.setOrder(order);

        IPage<ProductSpecItem> pageList = productSpecItemService.lists(productSpecItemListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productSpec/detail')")
    @Operation(summary = "商品规格值表-通过spec_item_id查询", description = "商品规格值表-通过spec_item_id查询")
    @RequestMapping(value = "/{specItemId}", method = RequestMethod.GET)
    public CommonRes<ProductSpecItem> get(@PathVariable Integer specItemId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductSpecItem specItem = productSpecItemService.get(specItemId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), specItem, ProductSpecItem::getStoreId)) {

            return success(specItem);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productSpec/add')")
    @Operation(summary = "商品规格值表-添加", description = "商品规格值表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(ProductSpecItemAddReq productSpecItemAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer storeId = loginUser.getStoreId();

        if (CheckUtil.isNotEmpty(storeId)) {
            productSpecItemAddReq.setStoreId(storeId);
        }
        ProductSpecItem productSpecItem = BeanUtil.copyProperties(productSpecItemAddReq, ProductSpecItem.class);
        boolean success = productSpecItemService.add(productSpecItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productSpec/edit')")
    @Operation(summary = "商品规格值表-编辑", description = "商品规格值表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(ProductSpecItemEditReq productSpecItemEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductSpecItem specItem = productSpecItemService.get(productSpecItemEditReq.getSpecItemId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), specItem, ProductSpecItem::getStoreId)) {
            ProductSpecItem productSpecItem = BeanUtil.copyProperties(productSpecItemEditReq, ProductSpecItem.class);
            boolean success = productSpecItemService.edit(productSpecItem);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productSpec/remove')")
    @Operation(summary = "商品规格值表-通过spec_item_id删除", description = "商品规格值表-通过spec_item_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("spec_item_id") Integer specItemId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductSpecItem specItem = productSpecItemService.get(specItemId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), specItem, ProductSpecItem::getStoreId)) {
            boolean success = productSpecItemService.removeItem(specItemId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productSpec/editState')")
    @Operation(summary = "商品规格值表-通过spec_item_enable更改状态", description = "商品规格值表-通过spec_item_enable更改状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(@RequestParam("spec_item_id") Integer specItemId,
                                  @RequestParam("spec_item_enable") Boolean specItemEnable) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            boolean success = productSpecItemService.editState(specItemId, specItemEnable);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

