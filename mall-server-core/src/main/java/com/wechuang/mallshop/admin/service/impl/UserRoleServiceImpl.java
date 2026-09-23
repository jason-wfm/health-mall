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
package com.wechuang.mallshop.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.model.entity.UserRole;
import com.wechuang.mallshop.admin.model.req.UserRoleListReq;
import com.wechuang.mallshop.admin.repository.UserAdminRepository;
import com.wechuang.mallshop.admin.repository.UserRoleRepository;
import com.wechuang.mallshop.admin.service.UserRoleService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 权限组表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-22
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleRepository, UserRole, UserRoleListReq> implements UserRoleService {

    @Autowired
    private UserAdminRepository userAdminRepository;


    @Override
    public boolean remove(Serializable userRoleId) {
        UserRole userRole = get(userRoleId);

        if (userRole.getUserRoleBuildin()) {
            throw new BusinessException(__("系统内置，不可删除"));
        }

        QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
        userAdminQueryWrapper.eq("user_role_id", userRoleId);
        List<Serializable> userIds = userAdminRepository.findKey(userAdminQueryWrapper);

        if (CollectionUtil.isNotEmpty(userIds)) {
            throw new BusinessException(String.format(__("该权限组存在系统用户 %s ，不可删除"), StrUtil.join(",", userIds)));
        }

        return super.remove(userRoleId);
    }

    @Override
    public IPage<UserRole> getList(UserRoleListReq userRoleListReq) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isEmpty(loginUser.getStoreId()) && !loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        QueryWrapper<UserRole> wrapper = new BaseQueryWrapper<UserRole, UserRoleListReq>(userRoleListReq).getWrapper();

        if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
            wrapper.eq("store_id", loginUser.getStoreId());
        }

        if (loginUser.isPlatform() && CheckUtil.isNotEmpty(userRoleListReq.getRoleId())) {
            if (userRoleListReq.getRoleId().equals(2)) {
                wrapper.ne("store_id", 0);
            } else if (userRoleListReq.getRoleId().equals(9)) {
                wrapper.eq("store_id", 0);
            }
        }

        return lists(wrapper, userRoleListReq.getPage(), userRoleListReq.getSize());
    }

    @Override
    public boolean addRole(UserRole userRole) {
        checkMenuIds(userRole);

        if (!add(userRole)) {
            throw new BusinessException(__("添加权限组信息失败！"));
        }

        return true;
    }

    private void checkMenuIds(UserRole userRole) {
        String menuIds = userRole.getMenuIds();

        if (StrUtil.isEmpty(menuIds)) {
            throw new BusinessException(__("菜单权限不能为空！"));
        }
        List<Integer> menuIdList = Convert.toList(Integer.class, menuIds);

        if (!menuIdList.contains(101)) {
            menuIdList.add(101);

            userRole.setMenuIds(StrUtil.join(",", menuIdList));
        }
    }

    @Override
    public boolean editRole(UserRole userRole) {
        checkMenuIds(userRole);

        if (!edit(userRole)) {
            throw new BusinessException(__("修改权限组信息失败！"));
        }

        return true;
    }
}
