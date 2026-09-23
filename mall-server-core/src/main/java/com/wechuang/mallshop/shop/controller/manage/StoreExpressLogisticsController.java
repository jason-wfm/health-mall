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
import com.wechuang.mallshop.shop.model.entity.StoreExpressLogistics;
import com.wechuang.mallshop.shop.model.req.StoreExpressLogisticsAddReq;
import com.wechuang.mallshop.shop.model.req.StoreExpressLogisticsEditReq;
import com.wechuang.mallshop.shop.model.req.StoreExpressLogisticsListReq;
import com.wechuang.mallshop.shop.model.req.StoreExpressLogisticsStateEditReq;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.service.StoreExpressLogisticsService;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.trade.service.OrderLogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 物流 = shop_store_express 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-19
 */
@Tag(name = "物流 = shop_store_express")
@RestController
@RequestMapping("/manage/shop/storeExpressLogistics")
public class StoreExpressLogisticsController extends BaseController {
    @Autowired
    private StoreExpressLogisticsService storeExpressLogisticsService;

    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @PreAuthorize("hasAuthority('/manage/shop/storeExpressLogistics/list')")
    @Operation(summary = "物流 = shop_store_express-分页列表查询", description = "物流 = shop_store_express-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StoreExpressLogistics>> list(StoreExpressLogisticsListReq storeExpressLogisticsListReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isNotEmpty(loginUser.getChainId())) {
            storeExpressLogisticsListReq.setChainId(loginUser.getChainId());
        }

        if (loginUser.isPlatform()) {
            storeExpressLogisticsListReq.setStoreId(0);
        }

        IPage<StoreExpressLogistics> pageList = storeExpressLogisticsService.lists(storeExpressLogisticsListReq);
        // 补充店铺名称
        if (pageList != null && CollUtil.isNotEmpty(pageList.getRecords())) {
            List<Integer> storeIds = CommonUtil.column(pageList.getRecords(), StoreExpressLogistics::getStoreId);
            if (CollUtil.isNotEmpty(storeIds)) {
                List<StoreBase> storeList = storeBaseRepository.find(new QueryWrapper<StoreBase>().in("store_id", storeIds));
                Map<Integer, String> storeNameMap = new HashMap<>();
                if (CollUtil.isNotEmpty(storeList)) {
                    for (StoreBase storeBase : storeList) {
                        storeNameMap.put(storeBase.getStoreId(), storeBase.getStoreName());
                    }
                }
                for (StoreExpressLogistics item : pageList.getRecords()) {
                    item.setStoreName(storeNameMap.get(item.getStoreId()));
                }
            }
        }

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeExpressLogistics/detail')")
    @Operation(summary = "物流 = shop_store_express-通过logistics_id查询", description = "物流 = shop_store_express-通过logistics_id查询")
    @RequestMapping(value = "/{logisticsId}", method = RequestMethod.GET)
    public CommonRes<StoreExpressLogistics> get(@PathVariable Integer logisticsId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreExpressLogistics storeExpressLogistics = storeExpressLogisticsService.get(logisticsId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeExpressLogistics, StoreExpressLogistics::getStoreId)) {
            return success(storeExpressLogistics);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeExpressLogistics/add')")
    @Operation(summary = "物流 = shop_store_express-添加", description = "物流 = shop_store_express-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(StoreExpressLogisticsAddReq storeExpressLogisticsAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser != null) {
            Integer chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(chainId)) {
                storeExpressLogisticsAddReq.setChainId(chainId);
            }

            Integer storeId = loginUser.getStoreId();

            if (CheckUtil.isNotEmpty(storeId) && loginUser.isStore()) {
                storeExpressLogisticsAddReq.setStoreId(storeId);
            }
        }

        StoreExpressLogistics storeExpressLogistics = BeanUtil.copyProperties(storeExpressLogisticsAddReq, StoreExpressLogistics.class);
        boolean success = storeExpressLogisticsService.saveOrUpdate(storeExpressLogistics);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeExpressLogistics/edit')")
    @Operation(summary = "物流 = shop_store_express-编辑", description = "物流 = shop_store_express-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(StoreExpressLogisticsEditReq storeExpressLogisticsEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreExpressLogistics storeExpressLogistics = storeExpressLogisticsService.get(storeExpressLogisticsEditReq.getLogisticsId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeExpressLogistics, StoreExpressLogistics::getStoreId)) {
            StoreExpressLogistics expressLogistics = BeanUtil.copyProperties(storeExpressLogisticsEditReq, StoreExpressLogistics.class);
            boolean success = storeExpressLogisticsService.saveOrUpdate(expressLogistics);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeExpressLogistics/remove')")
    @Operation(summary = "物流 = shop_store_express-通过logistics_id删除", description = "物流 = shop_store_express-通过logistics_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("logistics_id") Integer logisticsId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreExpressLogistics storeExpressLogistics = storeExpressLogisticsService.get(logisticsId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeExpressLogistics, StoreExpressLogistics::getStoreId)) {
            boolean success = storeExpressLogisticsService.remove(logisticsId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @Operation(summary = "物流 = 查看物流", description = "物流 = 查看物流")
    @RequestMapping(value = "/returnLogistics", method = RequestMethod.GET)
    public CommonRes<Map> returnLogistics(@RequestParam(value = "order_id", required = false) String order_id,
                                          @RequestParam(value = "return_id", required = false) String return_id,
                                          @RequestParam("return_tracking_name") String returnTrackingName,
                                          @RequestParam("return_tracking_number") String returnTrackingNumber) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map map = orderLogisticsService.returnLogistics(order_id, return_id, returnTrackingName, returnTrackingNumber);

        return success(map);
    }

    @PreAuthorize("hasAuthority('/manage/shop/storeExpressLogistics/editState')")
    @Operation(summary = "修改状态", description = "修改状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(StoreExpressLogisticsStateEditReq logisticsStateEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        StoreExpressLogistics storeExpressLogistics = storeExpressLogisticsService.get(logisticsStateEditReq.getLogisticsId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), storeExpressLogistics, StoreExpressLogistics::getStoreId)) {
            StoreExpressLogistics expressLogistics = BeanUtil.copyProperties(logisticsStateEditReq, StoreExpressLogistics.class);
            boolean success = storeExpressLogisticsService.edit(expressLogistics);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

