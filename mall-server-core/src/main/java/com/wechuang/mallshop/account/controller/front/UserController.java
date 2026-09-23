package com.wechuang.mallshop.account.controller.front;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.*;
import com.wechuang.mallshop.account.model.output.UserInfoOutput;
import com.wechuang.mallshop.account.model.req.*;
import com.wechuang.mallshop.account.model.res.*;
import com.wechuang.mallshop.account.service.*;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantMsg;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.phone.PhoneModel;
import com.wechuang.mallshop.common.utils.phone.PhoneNumberUtils;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.common.web.service.VerifyCodeService;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.BaseOrder;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.UserResource;
import com.wechuang.mallshop.pay.repository.UserResourceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 用户基本信息表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Tag(name = "用户基本信息表")
@RestController
@RequestMapping("/front/account/user")
public class UserController extends BaseController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private UserMessageService messageService;

    @Autowired
    private UserIndustryService userIndustryService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserResourceRepository userResourceRepository;

    @Operation(summary = "用户基本信息", description = "用户基本信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonRes<?> getUserInfo() {
        Integer userId = ContextUtil.getLoginUserId();
        if (CheckUtil.isNotEmpty(userId)) {
            UserInfoOutput userBase = loginService.getInfoByUserId(ContextUtil.getLoginUserId());
            userBase.setClientId(ContextUtil.getLoginUser().getClientId());
            userBase.setIm(messageService.getImConfig(userId, null, null, null));
            return success(userBase);
        } else {
            return fail(__("尚未登录"));
        }
    }

    @Operation(summary = "修改用户端用户信息", description = "修改用户端用户信息")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserInfoEditReq userInfoEditReq) {
        UserInfo userInfo = BeanUtil.copyProperties(userInfoEditReq, UserInfo.class);
        Integer userId = ContextUtil.checkLoginUserId();
        userInfo.setUserId(userId);
        boolean success = loginService.edit(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "绑定手机号", description = "绑定手机号")
    @RequestMapping(value = "/bindMobile", method = {RequestMethod.POST, RequestMethod.GET})
    public CommonRes<?> bindMobile(RegReq req) {
        //判断手机号
        PhoneModel phoneModelWithCountry = PhoneNumberUtils.getPhoneModelWithCountry(req.getVerifyKey());

        if (phoneModelWithCountry == null) {
            return fail(__("手机号码不正确"), ConstantMsg.CODE_BUSINESS_VALIDATION_FAILED);
        }

        //验证码
        if (!verifyCodeService.checkVerifyCode(req.getVerifyKey(), req.getVerifyCode())) {
            return fail(__("验证码有误"), ConstantMsg.CODE_BUSINESS_VALIDATION_FAILED);
        }

        ContextUser loginUser = ContextUtil.getLoginUser();
        LoginRes loginRes = loginService.bindMobile(loginUser, phoneModelWithCountry.getCountryCodeStr(), phoneModelWithCountry.getNationalNumber(), "");

        return success(loginRes);
    }


    @Operation(summary = "重新绑定手机号", description = "重新绑定手机号")
    @RequestMapping(value = "/unBindMobile", method = {RequestMethod.POST, RequestMethod.GET})
    public CommonRes<?> unBindMobile(RegReq req) {
        //判断手机号
        PhoneModel phoneModelWithCountry = PhoneNumberUtils.getPhoneModelWithCountry(req.getVerifyKey());

        if (phoneModelWithCountry == null) {
            return fail(__("手机号码不正确"), ConstantMsg.CODE_BUSINESS_VALIDATION_FAILED);
        }

        //验证码
        if (!verifyCodeService.checkVerifyCode(req.getVerifyKey(), req.getVerifyCode())) {
            return fail(__("验证码有误"), ConstantMsg.CODE_BUSINESS_VALIDATION_FAILED);
        }

        ContextUser loginUser = ContextUtil.getLoginUser();

        //绑定新手机
        boolean flag = loginService.unBindMobile(loginUser, phoneModelWithCountry.getCountryCodeStr(), phoneModelWithCountry.getNationalNumber());

        if (flag) {
            return success();
        } else {
            return fail();
        }
    }

    @Operation(summary = "实名认证保存", description = "实名认证保存")
    @RequestMapping(value = "/saveCertificate", method = RequestMethod.POST)
    public CommonRes<?> saveCertificate(UserInfoEditReq userInfoEditReq) {
        UserInfo userInfo = BeanUtil.copyProperties(userInfoEditReq, UserInfo.class);
        Integer userId = ContextUtil.checkLoginUserId();
        userInfo.setUserId(userId);
        userInfo.setUserIsAuthentication(StateCode.USER_CERTIFICATION_VERIFY);
        boolean success = loginService.saveCertificate(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "读取平台用户等级", description = "读取平台用户等级")
    @RequestMapping(value = "/listBaseUserLevel", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserLevel>> listBaseUserLevel() {
        IPage<UserLevel> pageList = loginService.listBaseUserLevel();

        return success(pageList);
    }

    @Operation(summary = "用户等级规则", description = "用户等级规则")
    @RequestMapping(value = "/listsExpRule", method = RequestMethod.GET)
    public CommonRes<ExpRuleRes> listsExpRule() {
        ExpRuleRes expRuleRes = loginService.listsExpRule();

        return success(expRuleRes);
    }

    @Operation(summary = "用户行业树形列表", description = "用户行业树形列表")
    @RequestMapping(value = "/getIndustryTree", method = RequestMethod.GET)
    public CommonRes<List<UserIndustryRes>> getIndustryTree(UserIndustryListReq userIndustryListReq) {
        //额外排序
        BaseOrder baseOrder = new BaseOrder();
        baseOrder.setSidx("industry_id");
        baseOrder.setSort(Constants.ORDER_BY_DESC);

        List<BaseOrder> order = new ArrayList<>();
        order.add(baseOrder);
        userIndustryListReq.setOrder(order);

        List<UserIndustryRes> pageList = userIndustryService.tree(userIndustryListReq);

        return success(pageList);
    }

    @Operation(summary = "客户列表", description = "客户列表")
    @RequestMapping(value = "/getCustomerList", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserInfoRes>> getCustomerList(UserInfoListReq userInfoListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        userInfoListReq.setUserSaleId(userId);
        IPage<UserInfoRes> lists = userInfoService.getList(userInfoListReq);

        return success(lists);
    }

    @Operation(summary = "客户信息", description = "客户信息")
    @RequestMapping(value = "/getCustomer", method = RequestMethod.GET)
    public CommonRes<UserInfo> getCustomer(@RequestParam("user_id") String userId) {
        Integer saleId = ContextUtil.checkLoginUserId();
        UserInfo customerInfo = userInfoService.getCustomer(saleId, userId);

        return success(customerInfo);
    }

    @Operation(summary = "添加客户", description = "添加客户")
    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public CommonRes<?> addCustomer(@RequestParam("user_account") String user_account) {
        Integer saleId = ContextUtil.checkLoginUserId();
        boolean success = userInfoService.addCustomer(saleId, user_account);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "删除客户", description = "删除客户")
    @RequestMapping(value = "/removeCustomer", method = RequestMethod.POST)
    public CommonRes<?> removeCustomer(@RequestParam("user_id") Integer user_id) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user_id);
        userInfo.setUserSaleId(0);
        boolean success = userInfoService.edit(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "销售数据统计", description = "销售数据统计")
    @RequestMapping(value = "/saleDataStatistics", method = RequestMethod.GET)
    public CommonRes<UserSaleRes> saleDataStatistics() {
        Integer userId = ContextUtil.checkLoginUserId();
        UserSaleRes userSaleRes = userInfoService.saleDataStatistics(userId);

        return success(userSaleRes);
    }

}

