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
package com.wechuang.mallshop.shop.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.shop.model.entity.StoreCategory;
import com.wechuang.mallshop.shop.model.req.StoreCategoryAddReq;
import com.wechuang.mallshop.shop.model.req.StoreCategoryEditReq;
import com.wechuang.mallshop.shop.model.req.StoreCategoryListReq;
import com.wechuang.mallshop.shop.model.req.StoreCategoryStateEditReq;
import com.wechuang.mallshop.shop.model.res.StoreCategoryRes;
import com.wechuang.mallshop.shop.service.StoreCategoryService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 市场分类表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2026-03-30
 */
@Tag(name = "市场分类表")
@RestController
@RequestMapping("/manage/shop/storeCategory")
public class StoreCategoryController extends BaseController {
    @Autowired
    private StoreCategoryService storeCategoryService;

    @PreAuthorize("hasAuthority('/manage/shop/storeCategory/list')")
    @Operation(summary = "市场分类表-分页列表查询", description = "市场分类表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes <BaseListRes<StoreCategory>> list(StoreCategoryListReq storeCategoryListReq) {
        IPage<StoreCategory> pageList = storeCategoryService.lists(storeCategoryListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeCategory/list')")
    @Operation(summary = "市场分类表-树形", description = "市场分类表-树形")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public CommonRes <List<StoreCategoryRes>> tree(StoreCategoryListReq storeCategoryListReq) {
        List<StoreCategoryRes> list = storeCategoryService.getTree(storeCategoryListReq);

        return success(list);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeCategory/add')")
    @Operation(summary = "市场分类表-添加", description = "市场分类表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(StoreCategoryAddReq storeCategoryAddReq) {
        StoreCategory storeCategory = BeanUtil.copyProperties(storeCategoryAddReq, StoreCategory.class);
        boolean success = storeCategoryService.add(storeCategory);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeCategory/edit')")
    @Operation(summary = "市场分类表-编辑", description = "市场分类表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(StoreCategoryEditReq storeCategoryEditReq) {
        StoreCategory storeCategory = BeanUtil.copyProperties(storeCategoryEditReq, StoreCategory.class);
        boolean success = storeCategoryService.edit(storeCategory);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeCategory/remove')")
    @Operation(summary = "市场分类表-通过store_category_id删除", description = "市场分类表-通过store_category_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("store_category_id") Integer storeCategoryId) {
        boolean success = storeCategoryService.removeById(storeCategoryId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeCategory/edit')")
    @Operation(summary = "市场分类表-编辑", description = "市场分类表-编辑")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(StoreCategoryStateEditReq storeCategoryStateEditReq) {
        StoreCategory storeCategory = BeanUtil.copyProperties(storeCategoryStateEditReq, StoreCategory.class);
        boolean success = storeCategoryService.edit(storeCategory);

        if (success) {
            return success();
        }

        return fail();
    }
}

