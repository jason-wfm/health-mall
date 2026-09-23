package com.wechuang.mallshop.pay.controller.front;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.ConsumeWithdraw;
import com.wechuang.mallshop.pay.model.entity.RechargeLevel;
import com.wechuang.mallshop.pay.model.entity.UserPay;
import com.wechuang.mallshop.pay.model.req.ConsumeWithdrawListReq;
import com.wechuang.mallshop.pay.model.req.RechargeLevelListReq;
import com.wechuang.mallshop.pay.service.ConsumeWithdrawService;
import com.wechuang.mallshop.pay.service.RechargeLevelService;
import com.wechuang.mallshop.pay.service.UserPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户资金")
@RestController
@RequestMapping("/front/pay/index")
public class IndexController extends BaseController {

    @Autowired
    private UserPayService userPayService;

    @Autowired
    private ConsumeWithdrawService consumeWithdrawService;

    @Autowired
    private RechargeLevelService rechargeLevelService;

    @Operation(summary = "获取支付密码", description = "获取支付密码")
    @RequestMapping(value = "/getPayPasswd", method = RequestMethod.GET)
    public CommonRes<UserPay> getPayPasswd() {
        Integer userId = ContextUtil.checkLoginUserId();
        UserPay userPay = userPayService.getPayPasswd(userId);

        return success(userPay);
    }

    @Operation(summary = "修改支付密码", description = "修改支付密码")
    @RequestMapping(value = "/changePayPassword", method = RequestMethod.POST)
    public CommonRes<?> changePayPassword(@RequestParam(value = "old_pay_password", required = false) String oldPayPassword,
                                          @RequestParam("new_pay_password") String newPayPassword,
                                          @RequestParam("pay_password") String payPassword) {
        Integer userId = ContextUtil.checkLoginUserId();
        boolean success = userPayService.changePayPassword(oldPayPassword, newPayPassword, payPassword, userId);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "提现记录", description = "提现记录")
    @RequestMapping(value = "/consumeWithdrawList", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ConsumeWithdraw>> consumeWithdrawList(ConsumeWithdrawListReq consumeWithdrawListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        consumeWithdrawListReq.setUserId(userId);
        IPage<ConsumeWithdraw> pageList = consumeWithdrawService.lists(consumeWithdrawListReq);

        return success(pageList);
    }

    @Operation(summary = "定额充值表-分页列表查询", description = "定额充值表-分页列表查询")
    @RequestMapping(value = "/listRechargeLevel", method = RequestMethod.GET)
    public CommonRes<BaseListRes<RechargeLevel>> list(RechargeLevelListReq rechargeLevelListReq) {
        IPage<RechargeLevel> pageList = rechargeLevelService.lists(rechargeLevelListReq);

        return success(pageList);
    }
}
