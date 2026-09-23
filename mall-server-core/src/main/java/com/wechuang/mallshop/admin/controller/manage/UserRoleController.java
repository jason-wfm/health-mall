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
import com.wechuang.mallshop.admin.model.entity.UserRole;
import com.wechuang.mallshop.admin.model.req.UserRoleAddReq;
import com.wechuang.mallshop.admin.model.req.UserRoleEditReq;
import com.wechuang.mallshop.admin.model.req.UserRoleListReq;
import com.wechuang.mallshop.admin.service.UserRoleService;
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
 * 权限组表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-22
 */
@Tag(name = "权限组表")
@RestController
@RequestMapping("/manage/admin/userRole")
public class UserRoleController extends BaseController {
    @Autowired
    private UserRoleService userRoleService;

    @PreAuthorize("hasAuthority('/manage/admin/userRole/list')")
    @Operation(summary = "权限组表-分页列表查询", description = "权限组表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserRole>> list(UserRoleListReq userRoleListReq) {
        IPage<UserRole> pageList = userRoleService.getList(userRoleListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/admin/userRole/add')")
    @Operation(summary = "权限组表-添加", description = "权限组表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserRoleAddReq userRoleAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
            userRoleAddReq.setStoreId(loginUser.getStoreId());
        }

        UserRole userRole = BeanUtil.copyProperties(userRoleAddReq, UserRole.class);
        boolean success = userRoleService.addRole(userRole);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/admin/userRole/edit')")
    @Operation(summary = "权限组表-编辑", description = "权限组表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserRoleEditReq userRoleEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        UserRole role = userRoleService.get(userRoleEditReq.getUserRoleId());

        if (role == null) {
            throw new BusinessException(__("该权限组信息不存在！"));
        }

        if (CheckUtil.isEmpty(role.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), role, UserRole::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        UserRole userRole = BeanUtil.copyProperties(userRoleEditReq, UserRole.class);
        boolean success = userRoleService.editRole(userRole);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/admin/userRole/remove')")
    @Operation(summary = "权限组表-通过user_role_id删除", description = "权限组表-通过user_role_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("user_role_id") Integer userRoleId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        UserRole role = userRoleService.get(userRoleId);

        if (role == null) {
            throw new BusinessException(__("该权限组信息不存在！"));
        }

        if (CheckUtil.isEmpty(role.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), role, UserRole::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        boolean success = userRoleService.remove(userRoleId);

        if (success) {
            return success();
        }

        return fail();
    }

}

