package com.wechuang.mallshop.account.controller.front;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserInvoice;
import com.wechuang.mallshop.account.model.req.UserInvoiceAddReq;
import com.wechuang.mallshop.account.model.req.UserInvoiceEditReq;
import com.wechuang.mallshop.account.model.req.UserInvoiceListReq;
import com.wechuang.mallshop.account.service.UserInvoiceService;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户发票管理")
@RestController
@RequestMapping("/front/account/userInvoice")
public class InvoiceController extends BaseController {

    @Autowired
    private UserInvoiceService userInvoiceService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Operation(summary = "发票列表", description = "发票列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserInvoice>> list(UserInvoiceListReq userInvoiceListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        userInvoiceListReq.setUserId(userId);

        IPage<UserInvoice> pageList = userInvoiceService.lists(userInvoiceListReq);

        return success(pageList);
    }

    @Operation(summary = "用户发票管理表-通过user_invoice_id查询", description = "用户发票管理表-通过user_invoice_id查询")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<?> get(@RequestParam("user_invoice_id") Integer userInvoiceId) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserInvoice userInvoice = userInvoiceService.get(userInvoiceId);

        if (CheckUtil.checkDataRights(userId, userInvoice, UserInvoice::getUserId)) {

            return success(userInvoice);
        }

        return fail();
    }

    @Operation(summary = "添加用户发票管理", description = "添加用户发票管理")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserInvoiceAddReq userInvoiceAddReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserInvoice userInvoice = BeanUtil.copyProperties(userInvoiceAddReq, UserInvoice.class);
        userInvoice.setUserId(userId);
        boolean success = userInvoiceService.save(userInvoice);

        if (success) {
            return success(userInvoice);
        }

        return fail();
    }

    @Operation(summary = "用户发票管理表-通过user_invoice_id删除", description = "用户发票管理表-通过user_invoice_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_invoice_id") Integer userInvoiceId) {
        boolean success = false;
        Integer userId = ContextUtil.checkLoginUserId();

        UserInvoice userInvoice = userInvoiceService.get(userInvoiceId);

        if (CheckUtil.checkDataRights(userId, userInvoice, UserInvoice::getUserId)) {
            success = userInvoiceService.remove(userInvoiceId);
        }

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "用户发票管理表-编辑", description = "用户发票管理表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserInvoiceEditReq userInvoiceEditReq) {
        boolean success = false;
        Integer userId = ContextUtil.checkLoginUserId();
        UserInvoice userInvoice = BeanUtil.copyProperties(userInvoiceEditReq, UserInvoice.class);
        userInvoice.setUserId(userId);
        UserInvoice invoice = userInvoiceService.get(userInvoiceEditReq.getUserInvoiceId());

        if (CheckUtil.checkDataRights(userId, invoice, UserInvoice::getUserId)) {
            success = userInvoiceService.edit(userInvoice);
        }

        if (success) {
            return success(userInvoice);
        }

        return fail();
    }

    @Operation(summary = "获取订单页发票提示", description = "获取订单页发票提示")
    @RequestMapping(value = "/getInvoiceTips", method = RequestMethod.GET)
    public CommonRes<?> getInvoiceTips() {
        String invoiceTips = configBaseService.getConfig("invoice_tips", "");
        return success(invoiceTips);
    }


}
