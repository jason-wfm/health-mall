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
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.StoreShippingAddress;
import com.wechuang.mallshop.shop.model.req.StoreShippingAddressAddReq;
import com.wechuang.mallshop.shop.model.req.StoreShippingAddressEditReq;
import com.wechuang.mallshop.shop.model.req.StoreShippingAddressListReq;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.service.StoreShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 发货地址表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-09
 */
@Tag(name = "发货地址表")
@RestController
@RequestMapping("/manage/shop/storeShippingAddress")
public class StoreShippingAddressController extends BaseController {
    @Autowired
    private StoreShippingAddressService storeShippingAddressService;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @PreAuthorize("hasAuthority('/manage/shop/storeShippingAddress/list')")
    @Operation(summary = "发货地址表-分页列表查询", description = "发货地址表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StoreShippingAddress>> list(StoreShippingAddressListReq storeShippingAddressListReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isNotEmpty(loginUser.getChainId())) {
            storeShippingAddressListReq.setChainId(loginUser.getChainId());
        }

        if (loginUser.isPlatform()) {
            storeShippingAddressListReq.setStoreId(0);
        }
        IPage<StoreShippingAddress> pageList = storeShippingAddressService.lists(storeShippingAddressListReq);
        // 补充店铺名称
        if (pageList != null && CollUtil.isNotEmpty(pageList.getRecords())) {
            List<Integer> storeIds = CommonUtil.column(pageList.getRecords(), StoreShippingAddress::getStoreId);
            if (CollUtil.isNotEmpty(storeIds)) {
                List<StoreBase> storeList = storeBaseRepository.find(new QueryWrapper<StoreBase>().in("store_id", storeIds));
                Map<Integer, String> storeNameMap = new HashMap<>();
                if (CollUtil.isNotEmpty(storeList)) {
                    for (StoreBase storeBase : storeList) {
                        storeNameMap.put(storeBase.getStoreId(), storeBase.getStoreName());
                    }
                }
                for (StoreShippingAddress item : pageList.getRecords()) {
                    item.setStoreName(storeNameMap.get(item.getStoreId()));
                }
            }
        }

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeShippingAddress/detail')")
    @Operation(summary = "发货地址表-通过ss_id查询", description = "发货地址表-通过ss_id查询")
    @RequestMapping(value = "/{ssId}", method = RequestMethod.GET)
    public CommonRes<StoreShippingAddress> get(@PathVariable Integer ssId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreShippingAddress storeShippingAddress = storeShippingAddressService.get(ssId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeShippingAddress, StoreShippingAddress::getStoreId)) {
            return success(storeShippingAddress);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeShippingAddress/add')")
    @Operation(summary = "发货地址表-添加", description = "发货地址表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(StoreShippingAddressAddReq storeShippingAddressAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser != null) {
            Integer chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(chainId)) {
                storeShippingAddressAddReq.setChainId(chainId);
            }

            Integer storeId = loginUser.getStoreId();

            if (CheckUtil.isNotEmpty(storeId) && loginUser.isStore()) {
                storeShippingAddressAddReq.setStoreId(storeId);
            }
        }
        StoreShippingAddress storeShippingAddress = BeanUtil.copyProperties(storeShippingAddressAddReq, StoreShippingAddress.class);
        boolean success = storeShippingAddressService.add(storeShippingAddress);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeShippingAddress/edit')")
    @Operation(summary = "发货地址表-编辑", description = "发货地址表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(StoreShippingAddressEditReq storeShippingAddressEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreShippingAddress shippingAddress = storeShippingAddressService.get(storeShippingAddressEditReq.getSsId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), shippingAddress, StoreShippingAddress::getStoreId)) {
            StoreShippingAddress storeShippingAddress = BeanUtil.copyProperties(storeShippingAddressEditReq, StoreShippingAddress.class);
            boolean success = storeShippingAddressService.edit(storeShippingAddress);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeShippingAddress/remove')")
    @Operation(summary = "发货地址表-通过ss_id删除", description = "发货地址表-通过ss_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("ss_id") Integer ssId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreShippingAddress shippingAddress = storeShippingAddressService.get(ssId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), shippingAddress, StoreShippingAddress::getStoreId)) {
            boolean success = storeShippingAddressService.remove(ssId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

