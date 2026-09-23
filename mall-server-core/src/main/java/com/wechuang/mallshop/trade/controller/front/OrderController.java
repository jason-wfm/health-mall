package com.wechuang.mallshop.trade.controller.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pt.service.ProductCommentService;
import com.wechuang.mallshop.trade.model.entity.*;
import com.wechuang.mallshop.trade.model.input.CartAddInput;
import com.wechuang.mallshop.trade.model.input.CheckoutInput;
import com.wechuang.mallshop.trade.model.output.CheckoutOutput;
import com.wechuang.mallshop.trade.model.output.OrderAddOutput;
import com.wechuang.mallshop.trade.model.req.*;
import com.wechuang.mallshop.trade.model.res.*;
import com.wechuang.mallshop.trade.model.vo.CheckoutItemVo;
import com.wechuang.mallshop.trade.model.vo.OrderVo;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import com.wechuang.mallshop.trade.repository.OrderItemRepository;
import com.wechuang.mallshop.trade.repository.OrderReturnItemRepository;
import com.wechuang.mallshop.trade.service.OrderBaseService;
import com.wechuang.mallshop.trade.service.OrderInvoiceService;
import com.wechuang.mallshop.trade.service.OrderService;
import com.wechuang.mallshop.trade.service.UserCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-07-26
 */
@Tag(name = "订单表")
@RestController
@RequestMapping("/front/trade/order")
public class OrderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductCommentService productCommentService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private OrderReturnItemRepository orderReturnItemRepository;

    @Autowired
    private UserCartService userCartService;


    @Operation(summary = "订单列表信息", description = "订单列表信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderVo>> list(OrderInfoListReq orderInfoListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        orderInfoListReq.setUserId(userId);
        Page<OrderVo> pageList = orderService.lists(orderInfoListReq);

        return success(pageList);
    }

    @Operation(summary = "订单详细信息", description = "订单详细信息")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonRes<?> detail(@RequestParam("order_id") String orderId) {
        Integer userId = ContextUtil.checkLoginUserId();
        java.util.List<String> orderIds = Convert.toList(String.class, orderId);
        if (CollUtil.isEmpty(orderIds)) {
            return fail();
        }
        OrderVo orderBase = orderService.detail(orderIds.get(0));

        if (CheckUtil.checkDataRights(userId, orderBase, OrderVo::getUserId)) {
            return success(orderBase);
        }

        return fail();
    }

    @Operation(summary = "订单详细信息", description = "订单详细信息")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(OrderAddReq orderBaseAddReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        ContextUser user = ContextUtil.getLoginUser();
        CheckoutInput input = BeanUtil.copyProperties(orderBaseAddReq, CheckoutInput.class);

        //处理数据
        input.setUserId(user.getUserId());
        input.setUserNickname(user.getUserNickname());

        //消息
        Map<String, String> orderMessageMap = JSONUtil.parseObject(orderBaseAddReq.getOrderMessage(), Map.class);

        // Convert keys to integers and create a new Map<Integer, String>
        Map<Integer, String> orderMessage = new HashMap<>();
        for (Map.Entry<String, String> entry : orderMessageMap.entrySet()) {
            Integer key = Integer.parseInt(entry.getKey());
            String value = entry.getValue();
            orderMessage.put(key, value);
        }

        //优惠券
        List<Integer> ts = Convert.toList(Integer.class, orderBaseAddReq.getUserVoucherIds());

        //下单商品
        List<CheckoutItemVo> items = new ArrayList<>();

        input.setUserVoucherIds(ts);
        input.setMessage(orderMessage);
        input.setItems(items);
        if (StrUtil.isNotEmpty(orderBaseAddReq.getUserInvoiceIds())) {
            Map<String, String> invoiceIdMap = JSONUtil.parseObject(orderBaseAddReq.getUserInvoiceIds(), Map.class);

            Map<Integer, Integer> invoiceIds = new HashMap<>();

            if (CollUtil.isNotEmpty(invoiceIdMap)) {
                for (Map.Entry<String, String> entry : invoiceIdMap.entrySet()) {
                    Integer key = Integer.parseInt(entry.getKey());
                    Integer value = Convert.toInt(entry.getValue());
                    invoiceIds.put(key, value);
                }
            }
            input.setInvoice(invoiceIds);
        }


        List<String> itemInfoRow = StrUtil.split(orderBaseAddReq.getCartId(), ",");

        for (String item : itemInfoRow) {
            long[] item_row = StrUtil.splitToLong(item, "|");

            if (item_row[1] <= 0) {
                throw new BusinessException(__("购买数量最低为 1 哦~"));
            }

            CheckoutItemVo checkoutItemVo = new CheckoutItemVo();
            checkoutItemVo.setItemId(item_row[0]);
            checkoutItemVo.setCartQuantity(Convert.toInt(item_row[1]));
            checkoutItemVo.setCartId(item_row[2]);

            items.add(checkoutItemVo);
        }

        OrderAddOutput success = orderService.add(input);

        if (CollUtil.isNotEmpty(success.getOrderIds())) {
            return success(success);
        }

        return fail();
    }

    @Operation(summary = "取消订单", description = "取消订单")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public CommonRes<OrderCancelRes> cancel(OrderCancelReq req) {
        Integer userId = ContextUtil.checkLoginUserId();
        OrderCancelRes res = new OrderCancelRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");
        String orderCancelReason = req.getOrderCancelReason();

        List<OrderBase> orderBaseList = orderBaseService.gets(orderIdList);
        if (!CheckUtil.checkDataRights(userId, orderBaseList, OrderBase::getUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        for (String orderId : orderIdList) {
            try {
                Boolean flag = orderService.cancel(orderId, orderCancelReason);

                if (flag) {
                    orderIdRes.add(orderId);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);
                throw new BusinessException(e.getMessage());
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @Operation(summary = "确认收货", description = "确认收货")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public CommonRes<OrderReceiveRes> receive(OrderReceiveReq req) {
        Integer userId = ContextUtil.checkLoginUserId();
        OrderReceiveRes res = new OrderReceiveRes();
        List<String> orderIdRes = res.getOrderId();
        List<String> orderIdList = StrUtil.split(req.getOrderId(), ",");


        List<OrderBase> orderBaseList = orderBaseService.gets(orderIdList);
        if (!CheckUtil.checkDataRights(userId, orderBaseList, OrderBase::getUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        for (String orderId : orderIdList) {
            // RETURN_PROCESS_RECEIVED 为商家已同意退货、等待买家寄回，允许先确认收货。
            QueryWrapper<OrderReturnItem> itemQueryWrapper = new QueryWrapper<>();
            itemQueryWrapper.eq("order_id", orderId);
            List<OrderReturnItem> orderReturnItems = orderReturnItemRepository.find(itemQueryWrapper);
            if (CollUtil.isNotEmpty(orderReturnItems)) {
                Optional<OrderReturnItem> itemOptional = orderReturnItems.stream().filter(item -> !Objects.equals(item.getReturnStateId(), StateCode.RETURN_PROCESS_FINISH) && !Objects.equals(item.getReturnStateId(), StateCode.RETURN_PROCESS_CANCEL) && !Objects.equals(item.getReturnStateId(), StateCode.RETURN_PROCESS_REFUSED) && !Objects.equals(item.getReturnStateId(), StateCode.RETURN_PROCESS_RECEIVED)).findFirst();
                if (itemOptional.isPresent()) {
                    throw new BusinessException(__("有订单在售后处理中，不能确认收货"));
                }
            }

            try {
                Boolean flag = orderService.receive(orderId, "");

                if (flag) {
                    orderIdRes.add(orderId);
                }
            } catch (Exception e) {
                LogUtil.error(ConstantLog.TRADE, e);
                throw new BusinessException(e.getMessage());
            }
        }

        if (CollUtil.isEmpty(orderIdRes)) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return success(res);
    }

    @Operation(summary = "订单评价-读取订单商品", description = "订单评价-读取订单商品")
    @RequestMapping(value = "/storeEvaluationWithContent", method = RequestMethod.GET)
    public CommonRes<OrderCommentRes> storeEvaluationWithContent(@RequestParam(name = "order_id") String order_id) {
        return success(productCommentService.storeEvaluationWithContent(order_id));
    }

    @Operation(summary = "订单评价", description = "订单评价")
    @RequestMapping(value = "/addOrderComment", method = RequestMethod.POST)
    public CommonRes addOrderComment(OrderCommentReq orderCommentReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        OrderBase orderBase = orderBaseService.get(orderCommentReq.getOrderId());

        if (CheckUtil.checkDataRights(userId, orderBase, OrderBase::getUserId)) {
            productCommentService.addOrderComment(orderCommentReq);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        return success();
    }

    @Operation(summary = "用户中心订单数量", description = "用户中心订单数量")
    @RequestMapping(value = "/getOrderNum", method = RequestMethod.GET)
    public CommonRes<?> getOrderNum() {
        Integer userId = ContextUtil.checkLoginUserId();
        return success(orderService.getOrderStatisticsInfo(userId));
    }

    @Autowired
    private OrderInvoiceService orderInvoiceService;

    @Operation(summary = "订单发票管理表-分页列表查询", description = "订单发票管理表-分页列表查询")
    @RequestMapping(value = "/listInvoice", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderInvoice>> listInvoice(OrderInvoiceListReq orderInvoiceListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        orderInvoiceListReq.setUserId(userId);
        IPage<OrderInvoice> pageList = orderInvoiceService.lists(orderInvoiceListReq);

        return success(pageList);
    }

    @Operation(summary = "申请订单发票", description = "申请订单发票")
    @RequestMapping(value = "/addOrderInvoice", method = RequestMethod.POST)
    public CommonRes<?> addOrderInvoice(OrderInvoiceAddReq orderInvoiceAddReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        orderInvoiceAddReq.setUserId(userId);
        boolean success = orderService.addOrderInvoice(orderInvoiceAddReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "企业订单审核", description = "企业订单审核")
    @RequestMapping(value = "/companyOrderReview", method = RequestMethod.POST)
    public CommonRes<?> companyOrderReview(OrderReviewReq orderReviewReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        boolean success = orderService.companyOrderReview(orderReviewReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "再下一单", description = "再下一单")
    @RequestMapping(value = "/nextOrder", method = RequestMethod.POST)
    public CommonRes<?> nextOrder(@RequestParam(name = "order_id") String orderId) {
        Integer userId = ContextUtil.checkLoginUserId();
        QueryWrapper<OrderItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("order_id", orderId);

        List<OrderItem> orderItem = orderItemRepository.find(itemQueryWrapper);
        OrderInfo orderInfo = orderInfoRepository.get(orderId);

        for (OrderItem shopOrderItem : orderItem) {
            CartAddInput userCart = new CartAddInput();
            userCart.setItemId(shopOrderItem.getItemId());
            userCart.setCartQuantity(shopOrderItem.getOrderItemQuantity());
            userCart.setUserId(userId);
            userCart.setActivityId(shopOrderItem.getActivityId());
            userCart.setCartType(1);
            userCart.setChainId(orderInfo.getChainId());

            boolean flag = userCartService.addCart(userCart);

            if (!flag) {
                throw new BusinessException(__("操作失败"));
            }
        }

        return success();
    }

    @Operation(summary = "导出批量下单模板", description = "导出批量下单模板")
    @RequestMapping(value = "/batchOrderTemp", method = RequestMethod.GET)
    public void batchOrderTemp(HttpServletResponse response) {
        orderService.batchOrderTemp(response);
    }

    @Operation(summary = "导入批量下单模板", description = "导入批量下单模板")
    @RequestMapping(value = "/importBatchOrderTemp", method = RequestMethod.POST)
    public CommonRes<?> importBatchOrderTemp(@RequestParam MultipartFile file) throws Exception {
        Integer userId = ContextUtil.checkLoginUserId();
        orderService.importBatchOrderTemp(file);

        return success();
    }

    @Operation(summary = "批量下单列表", description = "批量下单列表")
    @RequestMapping(value = "/batchOrders", method = RequestMethod.GET)
    public CommonRes<BatchOrderDataRes> batchOrders(BatchOrderListReq batchOrderListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        batchOrderListReq.setUserId(userId);

        return success(orderService.batchOrders(batchOrderListReq));
    }

    @Operation(summary = "修改批量下单-商品/数量", description = "修改批量下单-商品/数量")
    @RequestMapping(value = "/editBatchOrder", method = RequestMethod.POST)
    public CommonRes<?> editBatchOrder(BatchOrderEditReq batchOrderEditReq) {
        BatchOrder batchOrder = BeanUtil.copyProperties(batchOrderEditReq, BatchOrder.class);
        boolean success = orderService.editBatchOrder(batchOrder);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "添加批量下单-商品", description = "添加批量下单-商品")
    @RequestMapping(value = "/addBatchOrder", method = RequestMethod.POST)
    public CommonRes<?> addBatchOrder(@RequestParam("item_id") Long itemId) {
        boolean success = orderService.addBatchOrder(itemId);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "删除批量下单", description = "删除批量下单")
    @RequestMapping(value = "/removeBatchOrders", method = RequestMethod.POST)
    public CommonRes<?> removeBatchOrders(@RequestParam("batch_ids") String batchIds) {
        boolean success = orderService.removeBatchOrders(Convert.toList(Integer.class, batchIds));

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "代客下单-商品", description = "代客下单-商品")
    @RequestMapping(value = "/checkoutCustomerOrder", method = RequestMethod.GET)
    public CommonRes<?> checkoutCustomerOrder(OrderAddReq orderBaseAddReq) {
        Integer saleUserId = ContextUtil.checkLoginUserId();
        CheckoutInput input = BeanUtil.copyProperties(orderBaseAddReq, CheckoutInput.class);
        input.setOrderSaleId(saleUserId);

        List<CheckoutItemVo> items = new ArrayList<>();
        List<String> itemInfoRow = StrUtil.split(orderBaseAddReq.getCartId(), ",");

        for (String item : itemInfoRow) {
            long[] item_row = StrUtil.splitToLong(item, "|");

            if (item_row[1] <= 0) {
                throw new BusinessException(__("购买数量最低为 1 哦~"));
            }

            CheckoutItemVo checkoutItemVo = new CheckoutItemVo();
            checkoutItemVo.setItemId(item_row[0]);
            checkoutItemVo.setCartQuantity(Convert.toInt(item_row[1]));
            checkoutItemVo.setCartId(item_row[2]);

            items.add(checkoutItemVo);
        }
        input.setItems(items);

        CheckoutOutput success = userCartService.checkout(input);

        return success(success);
    }

    @Operation(summary = "销售员-代客下单", description = "销售员-代客下单")
    @RequestMapping(value = "/addCustomerOrder", method = RequestMethod.POST)
    public CommonRes<?> addCustomerOrder(OrderAddReq orderBaseAddReq) {
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
        OrderAddOutput output = orderService.addCustomerOrder(orderBase);

        if (CollUtil.isNotEmpty(output.getOrderIds())) {
            return success(output);
        }

        return fail();
    }

    @Operation(summary = "订单列表信息", description = "订单列表信息")
    @RequestMapping(value = "/customerOrders", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderVo>> customerOrders(OrderInfoListReq orderInfoListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        orderInfoListReq.setOrderSaleId(userId);
        Page<OrderVo> pageList = orderService.lists(orderInfoListReq);

        return success(pageList);
    }

    @Operation(summary = "PC订单支付列表", description = "PC订单支付列表")
    @RequestMapping(value = "/getPayList", method = RequestMethod.GET)
    public CommonRes<?> getPayList(@RequestParam("order_ids") String orderIds) {
        OrderPayRes orderPayRes = orderService.getPayList(Convert.toList(String.class, orderIds));

        return success(orderPayRes);
    }

    @Operation(summary = "重新修改订单发票", description = "重新修改订单发票")
    @RequestMapping(value = "/reviseOrderInvoice", method = RequestMethod.POST)
    public CommonRes<?> reviseOrderInvoice(@RequestParam("order_invoice_id") Integer orderInvoiceId,
                                          @RequestParam("user_invoice_id") Integer userInvoiceId) {
        boolean success = orderService.reviseOrderInvoice(orderInvoiceId, userInvoiceId);

        if (success) {
            return success();
        }

        return fail();
    }
}
