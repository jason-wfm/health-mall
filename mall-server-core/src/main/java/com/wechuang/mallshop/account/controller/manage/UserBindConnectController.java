package com.wechuang.mallshop.account.controller.manage;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserBindConnect;
import com.wechuang.mallshop.account.model.req.UserBindConnectListReq;
import com.wechuang.mallshop.account.service.UserBindConnectService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户等级表-平台")
@RestController
@RequestMapping("/manage/account/userBindConnect")
public class UserBindConnectController extends BaseController {

    @Autowired
    private UserBindConnectService bindConnectService;

    @PreAuthorize("hasAuthority('/manage/account/userInfo/list')")
    @Operation(summary = "用户绑定表-平台-分页列表查询", description = "用户绑定表-平台-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserBindConnect>> list(UserBindConnectListReq userBindConnectListReq) {
        IPage<UserBindConnect> pageList = bindConnectService.lists(userBindConnectListReq);

        return success(pageList);
    }
}
