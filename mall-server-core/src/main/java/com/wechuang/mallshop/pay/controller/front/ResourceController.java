package com.wechuang.mallshop.pay.controller.front;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.UserExpHistory;
import com.wechuang.mallshop.pay.model.req.UserExpHistoryListReq;
import com.wechuang.mallshop.pay.model.res.DistributionCommissionRes;
import com.wechuang.mallshop.pay.model.res.SignInfoRes;
import com.wechuang.mallshop.pay.model.res.UserResourceRes;
import com.wechuang.mallshop.pay.service.UserResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Tag(name = "用户资源表-资金账户表 前端控制器")
@RestController
@RequestMapping("/front/pay/userResource")
public class ResourceController extends BaseController {

    @Autowired
    private UserResourceService userResourceService;

    @Operation(summary = "获取签到基本信息", description = "获取签到基本信息")
    @RequestMapping(value = "/getSignInfo", method = RequestMethod.GET)
    public CommonRes<SignInfoRes> getSignInfo(@RequestParam(name = "user_id") Integer userId) {
        userId = ContextUtil.checkLoginUserId();

        return success(userResourceService.getSignInfo(userId));
    }

    @Operation(summary = "签到", description = "签到")
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public CommonRes signIn() {
        Integer userId = ContextUtil.checkLoginUserId();

        userResourceService.sign(userId);

        return success(__("签到成功"));
    }

    @Operation(summary = "签到", description = "签到")
    @RequestMapping(value = "/signState", method = RequestMethod.GET)
    public CommonRes signState() {
        Integer userId = ContextUtil.checkLoginUserId();

        if (userResourceService.getSignState(userId)) {
            return success();
        } else {
            return fail();
        }
    }

    @Operation(summary = "账户余额信息", description = "账户余额信息")
    @RequestMapping(value = "/getCommissionInfo", method = RequestMethod.GET)
    public CommonRes<DistributionCommissionRes> getCommissionInfo() {
        Integer userId = ContextUtil.checkLoginUserId();

        return success(userResourceService.getCommissionInfo(userId));
    }

    @Operation(summary = "用户经验列表", description = "账户余额信息")
    @RequestMapping(value = "/listsExp", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserExpHistory>> listsExp(UserExpHistoryListReq userExpHistoryListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        userExpHistoryListReq.setUserId(userId);
        IPage<UserExpHistory> pageList = userResourceService.listsExp(userExpHistoryListReq);

        return success(pageList);
    }

    @Operation(summary = "获取用户资源信息", description = "获取用户资源信息")
    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public CommonRes<UserResourceRes> resource() {
        Integer userId = ContextUtil.checkLoginUserId();

        return success(userResourceService.resource(userId));
    }
}
