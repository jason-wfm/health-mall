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
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.excel.ExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.entity.OrderData;
import com.wechuang.mallshop.trade.model.entity.OrderItem;
import com.wechuang.mallshop.trade.model.entity.OrderStateLog;
import com.wechuang.mallshop.trade.model.input.CheckoutInput;
import com.wechuang.mallshop.trade.model.input.OrderPickingInput;
import com.wechuang.mallshop.trade.model.input.OrderShippingInput;
import com.wechuang.mallshop.trade.model.output.OrderAddOutput;
import com.wechuang.mallshop.trade.model.req.*;
import com.wechuang.mallshop.trade.model.res.*;
import com.wechuang.mallshop.trade.model.vo.CheckoutItemVo;
import com.wechuang.mallshop.trade.model.vo.OrderVo;
import com.wechuang.mallshop.trade.model.vo.PickingItem;
import com.wechuang.mallshop.trade.repository.OrderBaseRepository;
import com.wechuang.mallshop.trade.service.OrderDataService;
import com.wechuang.mallshop.trade.service.OrderService;
import com.wechuang.mallshop.trade.service.OrderStateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 订单详细信息-检索不分表也行，cache 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-07-03
 */
@Tag(name = "订单详细信息-检索不分表也行，cache")
@RestController
@RequestMapping("/manage/trade/orderBase")
public class OrderBaseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderBaseController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    @Autowired
    private OrderStateLogService orderStateLogService;

    @Autowired
    private OrderDataService orderDataService;

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "订单详细信息", description = "订单详细信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderVo>> list(OrderInfoListReq orderInfoListReq) {
        ContextUser user = ContextUtil.checkLoginUser();

        if (user.getRoleId().intValue() == ConstantRole.ROLE_SELLER) {
            orderInfoListReq.setStoreId(user.getStoreId());
        }

        if (user.getRoleId().intValue() == ConstantRole.ROLE_CHAIN) {
            orderInfoListReq.setChainId(user.getChainId());
        }

        Page<OrderVo> pageList = orderService.lists(orderInfoListReq);

        return success(pageList);
    }

    @Operation(summary = "订单详细信息", description = "订单详细信息")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonRes<OrderVo> detail(@RequestParam("order_id") String orderId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderVo orderBase = orderService.detail(orderId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderVo::getStoreId)) {

            return success(orderBase);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/add')")
    @Operation(summary = "代客下单", description = "代客下单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(OrderAddReq orderBaseAddReq) {
        CheckoutInput orderBase = BeanUtil.copyProperties(orderBaseAddReq, CheckoutInput.class);

        if (StrUtil.isEmpty(orderBaseAddReq.getProductItems())) {
            throw new BusinessException(__("商品数据为空！"));
        }
        List<CheckoutItemVo> checkoutItemVos = JSONUtil.parseArray(orderBaseAddReq.getProductItems(), CheckoutItemVo.class);

        if (CollectionUtil.isEmpty(checkoutItemVos)) {
            throw new BusinessException(__("商品数据错误！"));
        }
        orderBase.setPaymentTypeId(StateCode.PAYMENT_TYPE_ONLINE);
        orderBase.setItems(checkoutItemVos);
        OrderAddOutput output = orderService.replaceAdd(orderBase);

        if (CollUtil.isNotEmpty(output.getOrderIds())) {
            return success(output);
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/cancel')")
    @Operation(summary = "取消订单", description = "取消订单")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public CommonRes<OrderCancelRes> cancel(OrderCancelReq req) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderCancelRes res = new OrderCancelRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");
        String orderCancelReason = req.getOrderCancelReason();

        for (String orderId : orderIdList) {
            try {
                OrderBase orderBase = orderBaseRepository.get(orderId);

                if (orderBase == null) {
                    throw new BusinessException("该订单信息不存在！");
                }

                if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
                    Boolean flag = orderService.cancel(orderId, orderCancelReason);

                    if (flag) {
                        orderIdRes.add(orderId);
                    }
                } else {
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);
//                throw new BusinessException(e.getMessage());
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/edit')")
    @Operation(summary = "推送订单", description = "推送订单")
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public CommonRes<OrderCancelRes> push(OrderCancelReq req) {
        OrderCancelRes res = new OrderCancelRes();
        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/review')")
    @Operation(summary = "审核订单", description = "审核订单")
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public CommonRes<OrderReviewRes> review(OrderReviewReq req) {
        OrderReviewRes res = new OrderReviewRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");
        String orderReviewReason = req.getOrderReviewReason();

        for (String orderId : orderIdList) {
            try {
                Boolean flag = orderService.review(orderId, orderReviewReason);

                if (flag) {
                    orderIdRes.add(orderId);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);

                if (orderIdList.size() == 1) {
                    throw new BusinessException(e.getMessage());
                }
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/finance')")
    @Operation(summary = "财务审核", description = "财务审核")
    @RequestMapping(value = "/finance", method = RequestMethod.POST)
    public CommonRes<OrderFinanceRes> finance(OrderFinanceReq req) {
        OrderFinanceRes res = new OrderFinanceRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");
        String orderFinanceReason = req.getOrderFinanceReason();

        for (String orderId : orderIdList) {
            try {
                Boolean flag = orderService.finance(orderId, orderFinanceReason);

                if (flag) {
                    orderIdRes.add(orderId);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);

                if (orderIdList.size() == 1) {
                    throw new BusinessException(e.getMessage());
                }
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/picking')")
    @Operation(summary = "出库审核", description = "出库审核")
    @RequestMapping(value = "/picking", method = RequestMethod.POST)
    public CommonRes<OrderPickingRes> picking(OrderPickingReq req) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderPickingRes res = new OrderPickingRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");
        List<PickingItem> pickingItems = new ArrayList<>();

        if (StrUtil.isNotEmpty(req.getItems())) {
            pickingItems = JSONUtil.parseArray(req.getItems(), PickingItem.class);
        }

        for (String orderId : orderIdList) {
            try {
                OrderBase orderBase = orderBaseRepository.get(orderId);

                if (orderBase == null) {
                    throw new BusinessException("该订单信息不存在！");
                }

                if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
                    OrderPickingInput input = new OrderPickingInput();
                    BeanUtils.copyProperties(req, input);
                    input.setOrderId(orderId);
                    input.setItems(pickingItems);

                    Boolean flag = orderService.picking(input);

                    if (flag) {
                        orderIdRes.add(orderId);
                    }
                } else {
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);

                if (orderIdList.size() == 1) {
                    throw new BusinessException(e.getMessage());
                }
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/shipping')")
    @Operation(summary = "发货审核", description = "发货审核")
    @RequestMapping(value = "/shipping", method = RequestMethod.POST)
    public CommonRes<OrderShippingRes> shipping(OrderShippingReq req) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderShippingRes res = new OrderShippingRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");

        for (String orderId : orderIdList) {
            try {
                OrderBase orderBase = orderBaseRepository.get(orderId);

                if (orderBase == null) {
                    throw new BusinessException("该订单信息不存在！");
                }

                if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
                    OrderShippingInput input = new OrderShippingInput();
                    BeanUtils.copyProperties(req, input);
                    input.setOrderId(orderId);

                    Boolean flag = orderService.shipping(input);

                    if (flag) {
                        orderIdRes.add(orderId);
                    }
                } else {
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);

                if (orderIdList.size() == 1) {
                    throw new BusinessException(e.getMessage());
                }
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/receive')")
    @Operation(summary = "收货审核", description = "收货审核")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public CommonRes<OrderReceiveRes> receive(OrderReceiveReq req) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderReceiveRes res = new OrderReceiveRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");

        for (String orderId : orderIdList) {
            try {
                OrderBase orderBase = orderBaseRepository.get(orderId);

                if (orderBase == null) {
                    throw new BusinessException("该订单信息不存在！");
                }

                if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
                    Boolean flag = orderService.receive(orderId, "");

                    if (flag) {
                        orderIdRes.add(orderId);
                    }
                } else {
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);

                if (orderIdList.size() == 1) {
                    throw new BusinessException(e.getMessage());
                }
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "订单日志", description = "订单日志")
    @RequestMapping(value = "/listStateLog", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderStateLog>> listStateLog(OrderStateLogListReq req) {
        IPage<OrderStateLog> pageList = orderStateLogService.listStateLog(req);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/finance')")
    @Operation(summary = "批量修改订单-提现审核", description = "批量修改订单-提现审核")
    @RequestMapping(value = "/doUpdateOrders", method = RequestMethod.POST)
    public CommonRes<?> doUpdateOrders(@RequestParam("order_ids") String orderIds) {
        boolean success = orderService.doUpdateOrders(Convert.toList(String.class, orderIds));

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "导出在线订单", description = "导出在线订单")
    @RequestMapping(value = "/exportFile", method = RequestMethod.GET)
    public void exportFile(OrderInfoListReq orderInfoListReq, HttpServletResponse response) {
        Page<OrderVo> pageList = orderService.lists(orderInfoListReq);
        List<OrderVo> orderVos = pageList.getRecords();
        ExcelUtil.exportReport(response, 1, __("在线订单"), orderVos);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/shipping')")
    @Operation(summary = "提货核销", description = "提货核销")
    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    public CommonRes<?> verification(@RequestParam("order_id") String orderId) {
        boolean success = orderService.verification(orderId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "商家提现列表", description = "商家提现列表")
    @RequestMapping(value = "/getSettlementList", method = RequestMethod.GET)
    public CommonRes<OrderSettlementRes> getSettlementList(OrderInfoListReq orderInfoListReq) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser != null) {
            Integer storeId = loginUser.getStoreId();

            if (CheckUtil.isNotEmpty(storeId) && loginUser.isStore()) {
                orderInfoListReq.setStoreId(storeId);
            }
        }
        OrderSettlementRes orderSettlementRes = orderService.getSettlementList(orderInfoListReq);

        return success(orderSettlementRes);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/settlementApply')")
    @Operation(summary = "订单结算申请", description = "订单结算申请")
    @RequestMapping(value = "/settlementApply", method = RequestMethod.POST)
    public CommonRes<?> settlementApply(@RequestParam("user_bank_id") Integer user_bank_id) {
        boolean success = orderService.settlementApply(user_bank_id);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "商家待办事项", description = "商家待办事项")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public CommonRes<SellerTodoRes> dashboard() {
        SellerTodoRes dashboardRes = orderService.getSellerTodo();

        return success(dashboardRes);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "平台待办事项", description = "平台待办事项")
    @RequestMapping(value = "/adminDashboard", method = RequestMethod.GET)
    public CommonRes<AdminTodoRes> adminDashboard() {
        AdminTodoRes dashboardRes = orderService.getAdminTodo();

        return success(dashboardRes);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/edit')")
    @Operation(summary = "增加订单备注", description = "增加订单备注")
    @RequestMapping(value = "/addDesc", method = RequestMethod.POST)
    public CommonRes<?> addDesc(OrderDescEditReq orderDescEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        OrderData orderData = BeanUtil.copyProperties(orderDescEditReq, OrderData.class);
        boolean success = orderDataService.edit(orderData);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "已购课程列表", description = "已购课程列表")
    @RequestMapping(value = "/purchasedCourseList", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderCourseRes>> purchasedCourseList(OrderInfoListReq orderInfoListReq) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser == null || !loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        IPage<OrderCourseRes> iPage = orderService.purchasedCourseList(orderInfoListReq);

        return success(iPage);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/edit')")
    @Operation(summary = "订单改价", description = "订单改价")
    @RequestMapping(value = "/orderAdjustFee", method = RequestMethod.POST)
    public CommonRes<?> orderAdjustFee(@RequestParam("order_items") String order_items) {
        List<OrderItem> orderItems = JSONUtil.parseArray(order_items, OrderItem.class);

        if (CollectionUtil.isEmpty(orderItems)) {
            throw new BusinessException(__("订单数据为空！"));
        }
        boolean success = orderService.orderAdjustFee(orderItems);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/list')")
    @Operation(summary = "统计信息", description = "统计信息")
    @RequestMapping(value = "/sellerDashboard", method = RequestMethod.GET)
    public CommonRes<SellerDashboardRes> sellerDashboard() {

        return success(orderService.sellerDashboard());
    }

}

