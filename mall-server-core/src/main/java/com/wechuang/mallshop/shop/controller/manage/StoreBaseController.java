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
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.req.*;
import com.wechuang.mallshop.shop.model.res.StoreBaseRes;
import com.wechuang.mallshop.shop.service.StoreBaseService;
import com.wechuang.mallshop.trade.model.vo.StoreInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;


/**
 * <p>
 * 店铺基础信息表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Tag(name = "店铺基础信息表")
@RestController
@RequestMapping("/manage/shop/storeBase")
public class StoreBaseController extends BaseController {
    @Autowired
    private StoreBaseService storeBaseService;

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/list')")
    @Operation(summary = "店铺基础信息表-分页列表查询", description = "店铺基础信息表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StoreBaseRes>> list(StoreBaseListReq storeBaseListReq) {
        IPage<StoreBaseRes> pageList = storeBaseService.getList(storeBaseListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/add')")
    @Operation(summary = "店铺基础信息表-添加", description = "店铺基础信息表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(StoreBaseAddReq storeBaseAddReq) {
        boolean success = storeBaseService.addStore(storeBaseAddReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺基础信息表-编辑", description = "店铺基础信息表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(StoreBaseEditReq storeBaseEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreBase storeBase = storeBaseService.get(storeBaseEditReq.getStoreId());
        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeBase, StoreBase::getStoreId)) {
            boolean success = storeBaseService.editStore(storeBaseEditReq);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/remove')")
    @Operation(summary = "店铺基础信息表-通过store_id删除", description = "店铺基础信息表-通过store_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("store_id") Integer storeId) {

        ContextUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        StoreBase storeBase = storeBaseService.get(storeId);
        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeBase, StoreBase::getStoreId)) {

            /*boolean success = storeBaseService.removeStore(storeId);
            if (success) {
                return success();
            }

            return fail();*/
            return success();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "修改状态", description = "修改状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(StoreBaseStateEditReq storeBaseStateEditReq) {
        ContextUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {

            StoreBase storeBase = BeanUtil.copyProperties(storeBaseStateEditReq, StoreBase.class);
            boolean success = storeBaseService.editState(storeBase);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/getStore')")
    @Operation(summary = "获取店铺信息", description = "获取店铺信息")
    @RequestMapping(value = "/getStore", method = RequestMethod.GET)
    public CommonRes<?> getStore() {
        Integer storeId = 0;

        ContextUser loginUser = getLoginUser();
        if (loginUser != null) {
            storeId = loginUser.getStoreId();
        }

        List<Integer> storeIds = Collections.singletonList(storeId);

        List<StoreInfoVo> storeInfoVoList = storeBaseService.getStore(storeIds);
        if (storeInfoVoList == null || storeInfoVoList.size() == 0) {
            return fail();
        }

        return success(storeInfoVoList.get(0));
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺审核", description = "店铺审核")
    @RequestMapping(value = "/editStateId", method = RequestMethod.POST)
    public CommonRes<?> editStateId(StoreBaseStateIdEditReq storeBaseStateIdEditReq) {
        ContextUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {

            StoreBase storeBase = BeanUtil.copyProperties(storeBaseStateIdEditReq, StoreBase.class);
            boolean success = storeBaseService.editStateId(storeBase);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeBase/edit')")
    @Operation(summary = "店铺开通", description = "店铺开通(资料审核通过待付款→已开通运营, 3240→3250), 同步开启店铺与商品运营状态")
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    public CommonRes<?> open(@RequestParam("store_id") Integer storeId) {
        ContextUser loginUser = getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            boolean success = storeBaseService.openStore(storeId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

}

