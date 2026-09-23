// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.admin.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.model.req.UserAdminEditReq;
import com.wechuang.mallshop.admin.model.req.UserAdminEnableEditReq;
import com.wechuang.mallshop.admin.model.req.UserAdminListReq;
import com.wechuang.mallshop.admin.model.res.UserAdminRes;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-22
 */
@Tag(name = "管理员表")
@RestController
@RequestMapping("/manage/admin/userAdmin")
public class UserAdminController extends BaseController {
    @Autowired
    private UserAdminService userAdminService;

    @PreAuthorize("hasAuthority('/manage/admin/userAdmin/list')")
    @Operation(summary = "管理员表-分页列表查询", description = "管理员表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserAdminRes>> list(UserAdminListReq userAdminListReq) {
        IPage<UserAdminRes> pageList = userAdminService.getList(userAdminListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/admin/userAdmin/add')")
    @Operation(summary = "管理员表-添加", description = "管理员表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserAdminEditReq userAdminEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        UserAdmin admin = userAdminService.get(userAdminEditReq.getUserId());

        if (admin != null) {
            throw new BusinessException(__("该用户管理员信息已经存在！"));
        }

        if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
            userAdminEditReq.setStoreId(loginUser.getStoreId());
            userAdminEditReq.setRoleId(2);
        }

        UserAdmin userAdmin = BeanUtil.copyProperties(userAdminEditReq, UserAdmin.class);
        boolean success = userAdminService.updateAdmin(userAdmin);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/admin/userAdmin/edit')")
    @Operation(summary = "管理员表-编辑", description = "管理员表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserAdminEditReq userAdminEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        UserAdmin admin = userAdminService.get(userAdminEditReq.getUserId());

        if (admin == null) {
            throw new BusinessException(__("该管理员信息不存在！"));
        }

        if (CheckUtil.isEmpty(admin.getStoreId()) || admin.getUserRoleId().equals(1002)) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), admin, UserAdmin::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        UserAdmin userAdmin = BeanUtil.copyProperties(userAdminEditReq, UserAdmin.class);
        boolean success = userAdminService.updateAdmin(userAdmin);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/admin/userAdmin/remove')")
    @Operation(summary = "管理员表-通过user_id删除", description = "管理员表-通过user_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_id") Integer userId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        UserAdmin admin = userAdminService.get(userId);

        if (admin == null) {
            throw new BusinessException(__("该管理员信息不存在！"));
        }

        if (CheckUtil.isEmpty(admin.getStoreId()) || admin.getUserRoleId().equals(1002)) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), admin, UserAdmin::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        boolean success = userAdminService.remove(userId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/admin/userAdmin/edit')")
    @Operation(summary = "管理员表-编辑", description = "管理员表-编辑")
    @RequestMapping(value = "/editEnable", method = RequestMethod.POST)
    public CommonRes<?> editEnable(UserAdminEnableEditReq userAdminEnableEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        UserAdmin admin = userAdminService.get(userAdminEnableEditReq.getUserId());

        if (admin == null) {
            throw new BusinessException(__("该管理员信息不存在！"));
        }

        if (CheckUtil.isEmpty(admin.getStoreId()) || admin.getUserRoleId().equals(1002)) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), admin, UserAdmin::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        UserAdmin userAdmin = BeanUtil.copyProperties(userAdminEnableEditReq, UserAdmin.class);
        boolean success = userAdminService.edit(userAdmin);

        if (success) {
            return success();
        }

        return fail();
    }
}

