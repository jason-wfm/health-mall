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
import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.StoreTransportType;
import com.wechuang.mallshop.shop.model.req.StoreTransportTypeAddReq;
import com.wechuang.mallshop.shop.model.req.StoreTransportTypeEditReq;
import com.wechuang.mallshop.shop.model.req.StoreTransportTypeListReq;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.service.StoreTransportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义物流运费及售卖区域类型表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-11
 */
@Tag(name = "自定义物流运费及售卖区域类型表")
@RestController
@RequestMapping("/manage/shop/storeTransportType")
public class StoreTransportTypeController extends BaseController {
    @Autowired
    private StoreTransportTypeService storeTransportTypeService;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @PreAuthorize("hasAuthority('/manage/shop/storeTransportType/list')")
    @Operation(summary = "自定义物流运费及售卖区域类型表-分页列表查询", description = "自定义物流运费及售卖区域类型表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StoreTransportType>> list(StoreTransportTypeListReq storeTransportTypeListReq) {
        ContextUser user = ContextUtil.checkLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        Integer chainId = user.getChainId();

        if (CheckUtil.isNotEmpty(chainId)) {
            storeTransportTypeListReq.setChainId(chainId);
        }

        if (user.isPlatform()) {
            storeTransportTypeListReq.setStoreId(0);
        }

        IPage<StoreTransportType> pageList = storeTransportTypeService.lists(storeTransportTypeListReq);
        // 补充店铺名称
        if (pageList != null && CollUtil.isNotEmpty(pageList.getRecords())) {
            List<Integer> storeIds = CommonUtil.column(pageList.getRecords(), StoreTransportType::getStoreId);
            if (CollUtil.isNotEmpty(storeIds)) {
                List<StoreBase> storeList = storeBaseRepository.find(new QueryWrapper<StoreBase>().in("store_id", storeIds));
                Map<Integer, String> storeNameMap = new HashMap<>();
                if (CollUtil.isNotEmpty(storeList)) {
                    for (StoreBase storeBase : storeList) {
                        storeNameMap.put(storeBase.getStoreId(), storeBase.getStoreName());
                    }
                }
                for (StoreTransportType item : pageList.getRecords()) {
                    item.setStoreName(storeNameMap.get(item.getStoreId()));
                }
            }
        }

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeTransportType/add')")
    @Operation(summary = "自定义物流运费及售卖区域类型表-添加", description = "自定义物流运费及售卖区域类型表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(StoreTransportTypeAddReq storeTransportTypeAddReq) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (user.getRoleId().intValue() == ConstantRole.ROLE_SELLER) {
            storeTransportTypeAddReq.setStoreId(user.getStoreId());
        }

        Integer chainId = user.getChainId();

        if (CheckUtil.isNotEmpty(chainId)) {
            storeTransportTypeAddReq.setChainId(chainId);
        }

        StoreTransportType storeTransportType = BeanUtil.copyProperties(storeTransportTypeAddReq, StoreTransportType.class);
        boolean success = storeTransportTypeService.saveOrUpdateTransport(storeTransportType);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeTransportType/edit')")
    @Operation(summary = "自定义物流运费及售卖区域类型表-编辑", description = "自定义物流运费及售卖区域类型表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(StoreTransportTypeEditReq storeTransportTypeEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreTransportType transportType = storeTransportTypeService.get(storeTransportTypeEditReq.getTransportTypeId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), transportType, StoreTransportType::getStoreId)) {
            StoreTransportType storeTransportType = BeanUtil.copyProperties(storeTransportTypeEditReq, StoreTransportType.class);
            boolean success = storeTransportTypeService.saveOrUpdateTransport(storeTransportType);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeTransportType/remove')")
    @Operation(summary = "自定义物流运费及售卖区域类型表-通过transport_type_id删除", description = "自定义物流运费及售卖区域类型表-通过transport_type_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("transport_type_id") Integer transportTypeId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreTransportType transportType = storeTransportTypeService.get(transportTypeId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), transportType, StoreTransportType::getStoreId)) {
            boolean success = storeTransportTypeService.removeStoreTransportType(transportTypeId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeTransportType/editState')")
    @Operation(summary = "自定义物流运费及售卖区域类型表-通过transport_type_buildin", description = "自定义物流运费及售卖区域类型表-更改状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(@RequestParam("transport_type_id") Integer transportTypeId,
                                  @RequestParam("transport_type_buildin") Boolean transportTypeBuildin) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreTransportType transportType = storeTransportTypeService.get(transportTypeId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), transportType, StoreTransportType::getStoreId)) {
            boolean success = storeTransportTypeService.editState(transportTypeId, transportTypeBuildin);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

