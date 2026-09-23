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
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.shop.model.entity.StoreCompany;
import com.wechuang.mallshop.shop.model.req.StoreCompanyEditReq;
import com.wechuang.mallshop.shop.service.StoreCompanyService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;


/**
 * <p>
 * 店铺公司信息表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2026-03-26
 */
@Tag(name = "店铺公司信息表")
@RestController
@RequestMapping("/manage/shop/storeCompany")
public class StoreCompanyController extends BaseController {
    @Autowired
    private StoreCompanyService storeCompanyService;

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺公司信息表-查询", description = "店铺公司信息表-查询")
    @RequestMapping(value = "/getCompany", method = RequestMethod.GET)
    public CommonRes<StoreCompany> getCompany(@RequestParam(value = "store_id") Integer storeId) {
        ContextUser user = getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!user.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        StoreCompany storeCompany = storeCompanyService.getByStoreId(storeId);

        return success(storeCompany);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺公司信息表-编辑", description = "店铺公司信息表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(StoreCompanyEditReq storeCompanyEditReq) {
        ContextUser user = getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!user.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        StoreCompany storeCompany = BeanUtil.copyProperties(storeCompanyEditReq, StoreCompany.class);
        boolean success = storeCompanyService.editCompany(storeCompany);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺公司信息", description = "店铺公司信息")
    @RequestMapping(value = "/getStoreCompany", method = RequestMethod.GET)
    public CommonRes<StoreCompany> getStoreCompany() {
        ContextUser user = getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!user.isStore()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        StoreCompany storeCompany = storeCompanyService.getByStoreId(user.getStoreId());

        return success(storeCompany);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺公司信息修改", description = "店铺公司信息修改")
    @RequestMapping(value = "/editStoreCompany", method = RequestMethod.POST)
    public CommonRes<?> editStoreCompany(StoreCompanyEditReq storeCompanyEditReq) {
        ContextUser user = getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!user.isStore()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        StoreCompany storeCompany = BeanUtil.copyProperties(storeCompanyEditReq, StoreCompany.class);
        storeCompany.setUserId(user.getUserId());
        storeCompany.setStoreId(user.getStoreId());
        boolean success = storeCompanyService.save(storeCompany);

        if (success) {
            return success();
        }

        return fail();
    }
}

