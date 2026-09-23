package com.wechuang.mallshop.shop.controller.front;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.model.req.UserVoucherListReq;
import com.wechuang.mallshop.shop.model.res.UserVoucherRes;
import com.wechuang.mallshop.shop.model.res.VoucherCountRes;
import com.wechuang.mallshop.shop.service.UserVoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "会员优惠券")
@RestController
@RequestMapping("/front/shop/userVoucher")
public class VoucherController extends BaseController {

    @Autowired
    private UserVoucherService voucherService;

    @Operation(summary = "列举出不同优惠券的数量", description = "列举出不同优惠券的数量")
    @RequestMapping(value = "/getEachVoucherNum", method = RequestMethod.GET)
    public CommonRes<VoucherCountRes> getEachVoucherNum(@RequestParam(value = "voucher_state_id", required = false) Integer voucherStateId) {
        Integer userId = ContextUtil.checkLoginUserId();
        VoucherCountRes voucherCountRes = voucherService.getEachVoucherNum(voucherStateId, userId);

        return success(voucherCountRes);
    }

    @Operation(summary = "会员优惠券列表", description = "会员优惠券列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserVoucherRes>> list(UserVoucherListReq voucherListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        voucherListReq.setUserId(userId);
        IPage<UserVoucherRes> voucherResIPage = voucherService.getList(voucherListReq);

        return success(voucherResIPage);
    }

    @Operation(summary = "领取代金券", description = "领取代金券")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(@RequestParam("activity_id") Integer activityId) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserVoucher success = voucherService.addVoucher(activityId, userId, null, StateCode.VOUCHER_SOURCE_DEFAULT);

        return success(success);
    }

    @Operation(summary = "读取优惠券信息", description = "读取优惠券信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<UserVoucherRes> get(@RequestParam(value = "activity_id") Integer activityId,
                                         @RequestParam(value = "user_voucher_id", required = false) Integer userVoucherId,
                                         @RequestParam(value = "currency_id", required = false) Integer currencyId) {
        UserVoucherRes userVoucherRes = voucherService.getVoucher(activityId, userVoucherId, currencyId);

        return success(userVoucherRes);
    }

    // 领取PLUS券
    @Operation(summary = "领取PLUS每月券", description = "领取PLUS每月券")
    @RequestMapping(value = "/takeMonthlyVouchers", method = RequestMethod.POST)
    public CommonRes<?> takeMonthlyVouchers() {
        Integer userId = ContextUtil.checkLoginUserId();
        String grantType = StateCode.VOUCHER_SOURCE_PLUS_MONTHLY;
        Boolean success = voucherService.takePlusVouchers(userId, grantType, "");

        return success(success);
    }


}
