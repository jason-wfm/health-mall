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
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.shop.model.entity.UserContrastItem;
import com.wechuang.mallshop.shop.model.req.UserContrastItemAddReq;
import com.wechuang.mallshop.shop.model.req.UserContrastItemEditReq;
import com.wechuang.mallshop.shop.model.req.UserContrastItemListReq;
import com.wechuang.mallshop.shop.service.UserContrastItemService;
import com.wechuang.mallshop.common.utils.I18nUtil;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品对比表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2025-09-11
 */
@Tag(name = "商品对比表")
@RestController
@RequestMapping("/manage/shop/userContrastItem")
public class UserContrastItemController extends BaseController {
    @Autowired
    private UserContrastItemService userContrastItemService;

    @PreAuthorize("hasAuthority('/manage/shop/userContrastItem/list')")
    @Operation(summary = "商品对比表-分页列表查询", description = "商品对比表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes <BaseListRes<UserContrastItem>> list(UserContrastItemListReq userContrastItemListReq) {
        IPage<UserContrastItem> pageList = userContrastItemService.lists(userContrastItemListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/shop/userContrastItem/detail')")
    @Operation(summary = "商品对比表-通过user_id查询", description = "商品对比表-通过user_id查询")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public CommonRes<UserContrastItem> get(@PathVariable Integer userId) {
        UserContrastItem userContrastItem = userContrastItemService.get(userId);

        return success(userContrastItem);
    }

    @PreAuthorize("hasAuthority('/manage/shop/userContrastItem/add')")
    @Operation(summary = "商品对比表-添加", description = "商品对比表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserContrastItemAddReq userContrastItemAddReq) {
        UserContrastItem userContrastItem = BeanUtil.copyProperties(userContrastItemAddReq, UserContrastItem.class);
        boolean success = userContrastItemService.add(userContrastItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/userContrastItem/edit')")
    @Operation(summary = "商品对比表-编辑", description = "商品对比表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserContrastItemEditReq userContrastItemEditReq) {
        UserContrastItem userContrastItem = BeanUtil.copyProperties(userContrastItemEditReq, UserContrastItem.class);
        boolean success = userContrastItemService.edit(userContrastItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/userContrastItem/remove')")
    @Operation(summary = "商品对比表-通过user_id删除", description = "商品对比表-通过user_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_id") Integer userId) {
        boolean success = userContrastItemService.remove(userId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/userContrastItem/removeBatch')")
    @Operation(summary = "商品对比表-批量删除", description = "商品对比表-批量删除")
    @RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
    public CommonRes<?> removeBatch(@RequestParam("user_id") String userIds) {
        boolean success = userContrastItemService.remove(Convert.toList(Integer.class, userIds));

        if (success) {
            return success();
        }

        return fail();
    }
}

