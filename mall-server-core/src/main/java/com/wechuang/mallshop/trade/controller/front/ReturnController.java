package com.wechuang.mallshop.trade.controller.front;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.trade.model.entity.OrderReturn;
import com.wechuang.mallshop.trade.model.input.OrderReturnInput;
import com.wechuang.mallshop.trade.model.req.OrderReturnAddReq;
import com.wechuang.mallshop.trade.model.req.OrderReturnEditReq;
import com.wechuang.mallshop.trade.model.req.OrderReturnListReq;
import com.wechuang.mallshop.trade.model.res.OrderReturnRes;
import com.wechuang.mallshop.trade.model.vo.OrderReturnItemInputVo;
import com.wechuang.mallshop.trade.model.vo.OrderReturnItemVo;
import com.wechuang.mallshop.trade.model.vo.OrderReturnVo;
import com.wechuang.mallshop.trade.service.OrderReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "退款退货")
@RestController
@RequestMapping("/front/trade/orderReturn")
public class ReturnController extends BaseController {

    @Autowired
    private OrderReturnService orderReturnService;

    @Operation(summary = "退款退货列表", description = "退款退货列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderReturnRes>> list(OrderReturnListReq orderReturnListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        orderReturnListReq.setBuyerUserId(userId);
        IPage<OrderReturnRes> page = orderReturnService.getList(orderReturnListReq);

        return success(page);
    }

    @Operation(summary = "读取退款退货", description = "退款退货列表")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<OrderReturnRes> get(@RequestParam("return_id") String returnId) {
        OrderReturnRes orderReturnRes = orderReturnService.getReturn(returnId);

        return success(orderReturnRes);
    }

    @Operation(summary = "确认退货物流单号", description = "确认退货物流单号")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(OrderReturnEditReq orderReturnEditReq) {
        boolean success = false;
        Integer userId = ContextUtil.checkLoginUserId();
        OrderReturn orderReturn = BeanUtil.copyProperties(orderReturnEditReq, OrderReturn.class);
        OrderReturn returnOrder = orderReturnService.get(orderReturnEditReq.getReturnId());

        if (CheckUtil.checkDataRights(userId, returnOrder, OrderReturn::getBuyerUserId)) {
            success = orderReturnService.editReturn(orderReturn);
        }

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "取消退款订单", description = "取消退款订单")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public CommonRes<?> cancel(@RequestParam(name = "return_id") String returnId) {
        Integer userId = ContextUtil.checkLoginUserId();

        OrderReturn orderReturn = orderReturnService.get(returnId);

        if (!CheckUtil.checkDataRights(userId, orderReturn, OrderReturn::getBuyerUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        boolean success = orderReturnService.cancel(returnId, null);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "订单item详情,列出订单的item，及退款详情", description = "订单item详情,列出订单的item，及退款详情")
    @RequestMapping(value = "/returnItem", method = RequestMethod.GET)
    public CommonRes<OrderReturnItemVo> returnItem(@RequestParam(name = "order_id") String orderId,
                                                   @RequestParam(name = "order_item_id") String orderItemId) {
        Integer userId = ContextUtil.checkLoginUserId();
        OrderReturnItemVo orderReturnItemVo = orderReturnService.returnItem(orderId, orderItemId, userId);

        return success(orderReturnItemVo);
    }

    @Operation(summary = "添加退款退货", description = "添加退款退货")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(OrderReturnAddReq req) {
        Integer userId = ContextUtil.checkLoginUserId();

        OrderReturnInput orderReturnInput = BeanUtil.copyProperties(req, OrderReturnInput.class);
        orderReturnInput.setUserId(userId);
        orderReturnInput.setReturnAllFlag(false);

        OrderReturnItemInputVo returnItemInputVo = new OrderReturnItemInputVo();
        returnItemInputVo.setOrderItemId(req.getOrderItemId());
        returnItemInputVo.setReturnRefundAmount(req.getReturnRefundAmount());
        returnItemInputVo.setReturnItemNum(req.getReturnItemNum());
        orderReturnInput.getReturnItems().add(returnItemInputVo);

        String returnId = orderReturnService.addItem(orderReturnInput);

        orderReturnInput.setReturnId(returnId);

        return success(orderReturnInput);
    }

    @Operation(summary = "添加整单退款退货", description = "添加整单退款退货")
    @RequestMapping(value = "/addWhole", method = RequestMethod.POST)
    public CommonRes<?> addWhole(OrderReturnAddReq req) {
        Integer userId = ContextUtil.checkLoginUserId();

        OrderReturnInput orderReturnInput = BeanUtil.copyProperties(req, OrderReturnInput.class);
        orderReturnInput.setUserId(userId);
        orderReturnInput.setReturnAllFlag(true);
        String returnId = orderReturnService.addWhole(orderReturnInput);

        orderReturnInput.setReturnId(returnId);

        return success(orderReturnInput);
    }

    @Operation(summary = "整单退款退款详情", description = "整单退款退款详情")
    @RequestMapping(value = "/returnWhole", method = RequestMethod.GET)
    public CommonRes<OrderReturnVo> returnWhole(@RequestParam(name = "order_id") String orderId) {
        Integer userId = ContextUtil.checkLoginUserId();
        OrderReturnVo orderReturnVo = orderReturnService.returnWhole(orderId, userId);

        return success(orderReturnVo);
    }
}
