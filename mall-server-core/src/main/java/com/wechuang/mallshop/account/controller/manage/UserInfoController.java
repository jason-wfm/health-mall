package com.wechuang.mallshop.account.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.input.RegInput;
import com.wechuang.mallshop.account.model.output.UserInfoOutput;
import com.wechuang.mallshop.account.model.req.*;
import com.wechuang.mallshop.account.service.LoginService;
import com.wechuang.mallshop.account.service.UserInfoService;
import com.wechuang.mallshop.common.consts.BindConnectCode;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * <p>
 * 用户详细信息表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-09
 */
@Tag(name = "用户详细信息表")
@RestController
@RequestMapping("/manage/account/userInfo")
public class UserInfoController extends BaseController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LoginService loginService;

    @PreAuthorize("hasAuthority('/manage/account/userInfo/list')")
    @Operation(summary = "用户详细信息表-分页列表查询", description = "用户详细信息表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes
            <BaseListRes<UserInfo>> list(UserInfoListReq userInfoListReq) {
        IPage<UserInfo> pageList = userInfoService.getList(new BaseQueryWrapper<UserInfo, UserInfoListReq>(userInfoListReq).getWrapper(), userInfoListReq.getPage(), userInfoListReq.getSize());

        return success(pageList);
    }

    @Operation(summary = "用户详细信息表-通过user_id查询", description = "用户详细信息表-通过user_id查询")
    @RequestMapping(value = "/getUserData", method = RequestMethod.GET)
    public CommonRes<UserInfoOutput> getUserData(@RequestParam(value = "user_id", required = false) Integer userId) {

        if (CheckUtil.isEmpty(userId)) {
            userId = ContextUtil.checkLoginUserId();
        }
        UserInfoOutput userInfoOutput = userInfoService.getUserData(userId);

        return success(userInfoOutput);
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/add')")
    @Operation(summary = "用户详细信息表-添加", description = "用户详细信息表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserInfoAddReq userInfoAddReq) {
        UserInfo userInfo = BeanUtil.copyProperties(userInfoAddReq, UserInfo.class);

        //手机号注册
        RegInput in = BeanUtil.copyProperties(userInfoAddReq, RegInput.class);
        in.setBindType(BindConnectCode.ACCOUNT);

        if (CheckUtil.isEmpty(userInfoAddReq.getPassword())) {
            in.setPassword("Shopsuite@2018" + UUID.randomUUID());
        }

        //in.setBindType(BindConnectCode.MOBILE);
        //in.setUserAccount(String.format("%s%s", in.getUserIntl(), in.getUserMobile()));

        Integer userId = loginService.register(in);

        //手机绑定
        ContextUser user = new ContextUser();
        user.setUserId(userId);
        Integer uid = loginService.doBindMobile(user, in.getUserIntl(), in.getUserMobile());

        userInfo.setUserId(userId);
        boolean success = userInfoService.save(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "用户详细信息表-编辑", description = "用户详细信息表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserInfoEditReq userInfoEditReq) {
        UserInfo userInfo = BeanUtil.copyProperties(userInfoEditReq, UserInfo.class);
        boolean success = userInfoService.editUser(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/editState')")
    @Operation(summary = "用户详细信息表-编辑", description = "用户详细信息表-编辑")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(UserInfoStateEditReq userInfoStateEditReq) {
        UserInfo userInfo = BeanUtil.copyProperties(userInfoStateEditReq, UserInfo.class);
        boolean success = userInfoService.edit(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/remove')")
    @Operation(summary = "用户详细信息表-通过user_id删除", description = "用户详细信息表-通过user_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_id") Integer userId) {
        boolean success = userInfoService.removeUser(userId);

        if (success) {
            return success();
        }

        return fail();
    }


    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "修改密码", description = "修改密码")
    @RequestMapping(value = "/passWordEdit", method = RequestMethod.POST)
    public CommonRes<?> passWordEdit(@RequestParam("user_id") Integer userId,
                                     @RequestParam("user_password") String userPassword) {
        boolean success = userInfoService.passWordEdit(userId, userPassword);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "批量设置标签", description = "批量设置标签")
    @RequestMapping(value = "/addTags", method = RequestMethod.POST)
    public CommonRes<?> addTags(@RequestParam("user_ids") String userIds,
                                @RequestParam("tag_ids") String tagIds) {
        boolean success = userInfoService.addTags(userIds, tagIds);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/list')")
    @Operation(summary = "导出模版", description = "导出模版")
    @RequestMapping(value = "/exportTemp", method = {RequestMethod.POST, RequestMethod.GET})
    public void exportTemp(HttpServletResponse response) {
        userInfoService.exportTemp(response);
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/list')")
    @Operation(summary = "导出指定用户详细信息", description = "导出指定用户详细信息")
    @RequestMapping(value = "/exportFile", method = RequestMethod.POST)
    public void exportFile(HttpServletResponse response, @RequestParam(name = "user_ids") String userIds) {
        userInfoService.exportFile(response, Convert.toList(Integer.class, userIds));
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/add')")
    @Operation(summary = "导入用户信息", description = "导入用户信息")
    @RequestMapping(value = "/importTemp", method = RequestMethod.POST)
    public CommonRes<?> importTemp(@RequestParam MultipartFile file) throws Exception {
        userInfoService.importTemp(file);

        return success();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "批量发放优惠券", description = "批量发放优惠券")
    @RequestMapping(value = "/addVouchers", method = RequestMethod.POST)
    public CommonRes<?> addVouchers(@RequestParam(name = "user_ids") String userIds,
                                    @RequestParam(name = "activity_id") Integer activityId) {
        userInfoService.addVouchers(Convert.toList(Integer.class, userIds), activityId);

        return success();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "删除客户", description = "删除客户")
    @RequestMapping(value = "/removeSale", method = RequestMethod.POST)
    public CommonRes<?> removeSale(@RequestParam("user_id") Integer user_id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user_id);
        userInfo.setUserIsSale(false);
        boolean success = userInfoService.edit(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "删除客户", description = "删除客户")
    @RequestMapping(value = "/removeCustomer", method = RequestMethod.POST)
    public CommonRes<?> removeCustomer(@RequestParam("user_id") Integer user_id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user_id);
        userInfo.setUserSaleId(0);
        boolean success = userInfoService.edit(userInfo);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userInfo/edit')")
    @Operation(summary = "销售员绑定客户", description = "销售员绑定客户")
    @RequestMapping(value = "/bindCustomer", method = RequestMethod.POST)
    public CommonRes<?> bindCustomer(UserInfoBindReq userInfoBindReq) {
        boolean success = userInfoService.addCustomer(userInfoBindReq.getUserSaleId(), userInfoBindReq.getUserAccount());

        if (success) {
            return success();
        }

        return fail();
    }
}

