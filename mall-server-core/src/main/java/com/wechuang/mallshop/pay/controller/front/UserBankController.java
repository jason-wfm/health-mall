package com.wechuang.mallshop.pay.controller.front;


import cn.hutool.core.bean.BeanUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.pay.model.entity.UserBankCard;
import com.wechuang.mallshop.pay.model.req.UserBankCardEditReq;
import com.wechuang.mallshop.pay.model.res.BankRes;
import com.wechuang.mallshop.pay.service.UserBankCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "提现账户")
@RestController
@RequestMapping("/front/pay/userBank")
public class UserBankController extends BaseController {

    @Autowired
    private UserBankCardService bankCardService;


    @Operation(summary = "银行-账号列表", description = "银行-账号列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BankRes> list() {
        Integer userId = ContextUtil.getLoginUserId();
        BankRes bankRes = bankCardService.getList(userId);

        return success(bankRes);
    }

    @Operation(summary = "提现账户-结算账户", description = "提现账户-结算账户")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<UserBankCard> get(@RequestParam(name = "user_bank_id") Integer userBankId) {
        Integer userId = ContextUtil.getLoginUserId();
        UserBankCard userBankCard = bankCardService.getUserBank(userBankId, userId);

        return success(userBankCard);
    }

    @Operation(summary = "收款账号-添加/编辑", description = "收款账号-添加/编辑")
    @RequestMapping(value = "/addOrEditUserBank", method = RequestMethod.POST)
    public CommonRes<?> addOrEditUserBank(UserBankCardEditReq userBankCardEditReq) {
        UserBankCard userBankCard = BeanUtil.copyProperties(userBankCardEditReq, UserBankCard.class);
        Integer userId = ContextUtil.getLoginUserId();
        userBankCard.setUserId(userId);
        boolean success = bankCardService.addOrEditUserBank(userBankCard);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "账号列表-通过user_bank_id删除", description = "结算账户表-通过user_bank_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_bank_id") Integer userBankId) {
        boolean success = bankCardService.remove(userBankId);

        if (success) {
            return success();
        }

        return fail();
    }

}
