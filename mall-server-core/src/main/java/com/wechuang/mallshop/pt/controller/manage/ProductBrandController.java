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
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pt.model.entity.ProductBrand;
import com.wechuang.mallshop.pt.model.req.ProductBrandAddReq;
import com.wechuang.mallshop.pt.model.req.ProductBrandEditReq;
import com.wechuang.mallshop.pt.model.req.ProductBrandListReq;
import com.wechuang.mallshop.pt.model.req.ProductBrandStateEditReq;
import com.wechuang.mallshop.pt.model.res.ProductBrandRes;
import com.wechuang.mallshop.pt.service.ProductBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Tag(name = "品牌表")
@RestController
@RequestMapping("/manage/pt/productBrand")
public class ProductBrandController extends BaseController {
    @Autowired
    private ProductBrandService productBrandService;

    @PreAuthorize("hasAuthority('/manage/pt/productBrand/list')")
    @Operation(summary = "品牌表-分页列表查询", description = "品牌表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ProductBrand>> list(ProductBrandListReq productBrandListReq) {
        IPage<ProductBrand> pageList = productBrandService.lists(productBrandListReq);

        return success(pageList);
    }

    @Operation(summary = "品牌表 - 递归查询", description = "品牌表 - 递归查询")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public CommonRes<List<ProductBrandRes>> tree() {
        List<ProductBrandRes> brandRes = productBrandService.getTree();

        return success(brandRes);
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBrand/detail')")
    @Operation(summary = "品牌表-通过brand_id查询", description = "品牌表-通过brand_id查询")
    @RequestMapping(value = "/{brandId}", method = RequestMethod.GET)
    public CommonRes<ProductBrand> get(@PathVariable Integer brandId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductBrand productBrand = productBrandService.get(brandId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), productBrand, ProductBrand::getStoreId)) {

            return success(productBrand);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBrand/add')")
    @Operation(summary = "品牌表-添加", description = "品牌表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(ProductBrandAddReq productBrandAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer storeId = loginUser.getStoreId();

        if (CheckUtil.isNotEmpty(storeId)) {
            productBrandAddReq.setStoreId(storeId);
        }
        ProductBrand productBrand = BeanUtil.copyProperties(productBrandAddReq, ProductBrand.class);
        boolean success = productBrandService.add(productBrand);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBrand/edit')")
    @Operation(summary = "品牌表-编辑", description = "品牌表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(ProductBrandEditReq productBrandEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductBrand brand = productBrandService.get(productBrandEditReq.getBrandId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), brand, ProductBrand::getStoreId)) {
            ProductBrand productBrand = BeanUtil.copyProperties(productBrandEditReq, ProductBrand.class);
            boolean success = productBrandService.edit(productBrand);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBrand/remove')")
    @Operation(summary = "品牌表-通过brand_id删除", description = "品牌表-通过brand_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("brand_id") Integer brandId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductBrand brand = productBrandService.get(brandId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), brand, ProductBrand::getStoreId)) {
            boolean success = productBrandService.remove(brandId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/pt/productBrand/edit')")
    @Operation(summary = "品牌表状态", description = "品牌表状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(ProductBrandStateEditReq productBrandStateEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductBrand brand = productBrandService.get(productBrandStateEditReq.getBrandId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), brand, ProductBrand::getStoreId)) {
            ProductBrand productBrand = BeanUtil.copyProperties(productBrandStateEditReq, ProductBrand.class);
            boolean success = productBrandService.edit(productBrand);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

