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
package com.wechuang.mallshop.trade.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.trade.model.entity.OrderReturn;
import com.wechuang.mallshop.trade.model.req.OrderReturnEditReq;
import com.wechuang.mallshop.trade.model.req.OrderReturnListReq;
import com.wechuang.mallshop.trade.model.req.OrderReturnRefundEditReq;
import com.wechuang.mallshop.trade.model.res.OrderReturnRes;
import com.wechuang.mallshop.trade.model.vo.OrderReturnVo;
import com.wechuang.mallshop.trade.service.OrderReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 退款退货表-发货退货,卖家也可以决定不退货退款，买家申请退款不支持。卖家可以主动退款。 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Tag(name = "退款退货表-发货退货,卖家也可以决定不退货退款，买家申请退款不支持。卖家可以主动退款。")
@RestController
@RequestMapping("/manage/trade/orderReturn")
public class OrderReturnController extends BaseController {
    @Autowired
    private OrderReturnService orderReturnService;

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/list')")
    @Operation(summary = "退款退货表-发货退货,卖家也可以决定不退货退款，买家申请退款不支持。卖家可以主动退款。-分页列表查询", description = "退款退货表-发货退货,卖家也可以决定不退货退款，买家申请退款不支持。卖家可以主动退款。-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderReturnRes>> list(OrderReturnListReq orderReturnListReq) {
        ContextUser user = ContextUtil.checkLoginUser();

        if (user.getRoleId().intValue() == ConstantRole.ROLE_SELLER) {
            orderReturnListReq.setStoreId(user.getStoreId());
        }

        if (user.getRoleId().intValue() == ConstantRole.ROLE_CHAIN) {
            orderReturnListReq.setChainId(user.getChainId());
        }
        IPage<OrderReturnRes> pageList = orderReturnService.pageList(orderReturnListReq);

        return success(pageList);
    }

    @Operation(summary = "退款退货详情", description = "退款退货详情")
    @RequestMapping(value = "/getByReturnId", method = RequestMethod.GET)
    public CommonRes<OrderReturnVo> getByReturnId(@RequestParam("return_id") String return_id) {
        OrderReturnVo orderReturnVo = orderReturnService.getByReturnId(return_id);

        return success(orderReturnVo);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/edit')")
    @Operation(summary = "退货单审核-卖家拒绝退款/退货", description = "退货单审核-卖家拒绝退款/退货")
    @RequestMapping(value = "/refused", method = RequestMethod.POST)
    public CommonRes<?> refused(OrderReturnEditReq orderReturnEditReq) {
        orderReturnEditReq.setReturnReasonId(null);
        OrderReturn orderReturn = BeanUtil.copyProperties(orderReturnEditReq, OrderReturn.class);
        boolean success = orderReturnService.refused(orderReturn);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/edit')")
    @Operation(summary = "退货单审核-通过审核", description = "退货单审核-通过审核")
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public CommonRes<?> review(OrderReturnEditReq orderReturnEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderReturn orderReturnData = orderReturnService.get(orderReturnEditReq.getReturnId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderReturnData, OrderReturn::getStoreId)) {
            orderReturnEditReq.setReturnReasonId(null);
            OrderReturn orderReturn = BeanUtil.copyProperties(orderReturnEditReq, OrderReturn.class);
            orderReturn.setStoreId(loginUser.getStoreId());
            boolean success = orderReturnService.review(orderReturn, orderReturnEditReq.getReceivingAddress());

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/edit')")
    @Operation(summary = "退货单审核-确认收货", description = "退货单审核-确认收货")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public CommonRes<?> receive(@RequestParam("return_id") String return_id) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderReturn orderReturn = orderReturnService.get(return_id);
        Integer storeId = user.getStoreId();

        if (user.isPlatform() || CheckUtil.checkDataRights(storeId, orderReturn, OrderReturn::getStoreId)) {
            orderReturnService.dealWithReturn(Arrays.asList(return_id), storeId, StateCode.RETURN_PROCESS_RECEIVED, Arrays.asList(orderReturn), null);

            return success();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/edit')")
    @Operation(summary = "退货单审核-确认收款", description = "退货单审核-确认收款")
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public CommonRes<?> refund(@RequestParam("return_id") String return_id) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderReturn orderReturn = orderReturnService.get(return_id);
        Integer storeId = user.getStoreId();

        if (user.isPlatform() || CheckUtil.checkDataRights(storeId, orderReturn, OrderReturn::getStoreId)) {
            // 对齐多商户流程：商家确认退款后直接完结退单，避免停留在“收款确认”。
            orderReturnService.dealWithReturn(Arrays.asList(return_id), storeId, StateCode.RETURN_PROCESS_REFUND, Arrays.asList(orderReturn), StateCode.RETURN_PROCESS_FINISH);

            return success();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/edit')")
    @Operation(summary = "退货单-修改退款总额", description = "退货单审核-修改退款总额")
    @RequestMapping(value = "/editRefund", method = RequestMethod.POST)
    public CommonRes<?> editRefund(OrderReturnRefundEditReq refundEditReq) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderReturn orderReturnData = orderReturnService.get(refundEditReq.getReturnId());

        if (user.isPlatform() || CheckUtil.checkDataRights(user.getStoreId(), orderReturnData, OrderReturn::getStoreId)) {
            OrderReturn orderReturn = BeanUtil.copyProperties(refundEditReq, OrderReturn.class);

            boolean success = orderReturnService.edit(orderReturn);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderReturn/edit')")
    @Operation(summary = "退货单审核-取消退货", description = "退货单审核-取消退货")
    @RequestMapping(value = "/cancelReturn", method = RequestMethod.POST)
    public CommonRes<?> cancelReturn(@RequestParam("return_id") String return_id,
                                     @RequestParam("return_purchase_remark") String return_store_message) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderReturn orderReturn = orderReturnService.get(return_id);
        Integer storeId = user.getStoreId();

        if (user.isPlatform() || CheckUtil.checkDataRights(storeId, orderReturn, OrderReturn::getStoreId)) {
            boolean success = orderReturnService.cancel(return_id, return_store_message);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

