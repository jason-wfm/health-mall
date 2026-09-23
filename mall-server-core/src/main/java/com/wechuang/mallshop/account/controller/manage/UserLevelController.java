package com.wechuang.mallshop.account.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.model.req.UserLevelAddReq;
import com.wechuang.mallshop.account.model.req.UserLevelEditReq;
import com.wechuang.mallshop.account.model.req.UserLevelListReq;
import com.wechuang.mallshop.account.service.UserLevelService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户等级表-平台 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-09
 */
@Tag(name = "用户等级表-平台")
@RestController
@RequestMapping("/manage/account/userLevel")
public class UserLevelController extends BaseController {
    @Autowired
    private UserLevelService userLevelService;

    @Operation(summary = "用户等级表-平台-分页列表查询", description = "用户等级表-平台-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserLevel>> list(UserLevelListReq userLevelListReq) {
        IPage<UserLevel> pageList = userLevelService.lists(userLevelListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/account/userLevel/add')")
    @Operation(summary = "用户等级表-平台-添加", description = "用户等级表-平台-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserLevelAddReq userLevelAddReq) {
        UserLevel userLevel = BeanUtil.copyProperties(userLevelAddReq, UserLevel.class);
        boolean success = userLevelService.add(userLevel);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userLevel/edit')")
    @Operation(summary = "用户等级表-平台-编辑", description = "用户等级表-平台-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserLevelEditReq userLevelEditReq) {
        UserLevel userLevel = BeanUtil.copyProperties(userLevelEditReq, UserLevel.class);
        boolean success = userLevelService.edit(userLevel);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userLevel/remove')")
    @Operation(summary = "用户等级表-平台-通过user_level_id删除", description = "用户等级表-平台-通过user_level_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_level_id") Integer userLevelId) {
        boolean success = userLevelService.remove(userLevelId);

        if (success) {
            return success();
        }

        return fail();
    }
}

