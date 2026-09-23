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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.model.entity.UserRole;
import com.wechuang.mallshop.admin.model.req.UserAdminListReq;
import com.wechuang.mallshop.admin.model.res.UserAdminRes;
import com.wechuang.mallshop.admin.repository.UserAdminRepository;
import com.wechuang.mallshop.admin.repository.UserRoleRepository;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-22
 */
@Service
public class UserAdminServiceImpl extends BaseServiceImpl<UserAdminRepository, UserAdmin, UserAdminListReq> implements UserAdminService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ConfigBaseService configBaseService;


    @Override
    public boolean remove(Serializable userId) {
        UserAdmin userAdmin = get(userId);

        if (userAdmin.getUserIsSuperadmin()) {
            throw new BusinessException(__("系统内置，不可删除"));
        }

        return super.remove(userId);
    }

    @Override
    public boolean updateAdmin(UserAdmin userAdmin) {
        if (userAdmin.getRoleId().equals(8)) {
            QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
            userAdminQueryWrapper.eq("subsite_id", userAdmin.getSubsiteId());
            List<Serializable> subsite = findKey(userAdminQueryWrapper);

            if (CollectionUtil.isNotEmpty(subsite)) {
                throw new BusinessException(__("该租户已有用户，无法重复绑定！"));
            }
        }

        if (userAdmin.getRoleId().equals(3)) {
            QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
            userAdminQueryWrapper.eq("chain_id", userAdmin.getChainId());
            List<Serializable> chain = findKey(userAdminQueryWrapper);

            if (CollectionUtil.isNotEmpty(chain)) {
                throw new BusinessException(__("该门店已有用户，无法重复绑定！"));
            }
        }

        if (!repository.saveOrUpdate(userAdmin)) {
            throw new BusinessException(__("修改管理员信息失败！"));
        }

        return true;
    }

    @Override
    public Integer getUserIdByStoreId(Integer storeId) {
        QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
        userAdminQueryWrapper.eq("store_id", storeId);
        UserAdmin userAdmin = repository.findOne(userAdminQueryWrapper);
        if (userAdmin == null) {
            return 0;
        }

        return userAdmin.getUserId();
    }

    @Override
    public IPage<UserAdminRes> getList(UserAdminListReq userAdminListReq) {
        IPage<UserAdminRes> userAdminResPage = new Page<>();

        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isEmpty(loginUser.getStoreId()) && !loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        QueryWrapper<UserAdmin> wrapper = new BaseQueryWrapper<UserAdmin, UserAdminListReq>(userAdminListReq).getWrapper();

        if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
            wrapper.eq("store_id", loginUser.getStoreId());
            wrapper.ne("user_role_id", 1002);
        }

        if (loginUser.isPlatform() && CheckUtil.isNotEmpty(userAdminListReq.getRole())) {
            if (userAdminListReq.getRole().equals(2)) {
                // 店铺自建
                wrapper.ne("store_id", 0);
                wrapper.ne("user_role_id", 1002);
            } else if (userAdminListReq.getRole().equals(9)) {
                QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("store_id", 0);
                List<Serializable> userRoleIds = userRoleRepository.findKey(userRoleQueryWrapper);

                if (CollectionUtil.isEmpty(userRoleIds)) {
                    throw new BusinessException(__("权限组信息不存在！"));
                }

                wrapper.in("user_role_id", userRoleIds);
            }
        }
        IPage<UserAdmin> userAdminPage = lists(wrapper, userAdminListReq.getPage(), userAdminListReq.getSize());
        BeanUtil.copyProperties(userAdminPage, userAdminResPage);
        List<UserAdmin> userAdmins = userAdminPage.getRecords();

        if (CollectionUtil.isNotEmpty(userAdmins)) {
            List<UserAdminRes> userAdminResList = new ArrayList<>();

            List<Integer> userIds = CommonUtil.column(userAdmins, UserAdmin::getUserId);
            Map<Integer, UserInfo> userInfoMap = userInfoRepository.getUserInfoMap(userIds);

            for (UserAdmin userAdmin : userAdmins) {
                UserAdminRes userAdminRes = BeanUtil.copyProperties(userAdmin, UserAdminRes.class);
                Integer userId = userAdmin.getUserId();

                if (userInfoMap.containsKey(userId)) {
                    UserInfo userInfo = userInfoMap.get(userId);
                    userAdminRes.setUserAccount(userInfo.getUserAccount());
                    userAdminRes.setUserNickname(userInfo.getUserNickname());
                }
                userAdminResList.add(userAdminRes);
            }
            userAdminResPage.setRecords(userAdminResList);
        }

        return userAdminResPage;
    }

    @Override
    public Integer getSellerUserId(Integer storeId) {
        UserAdmin userAdmin = null;

        if (CheckUtil.isEmpty(storeId)) {
            userAdmin = findOne(new QueryWrapper<UserAdmin>().eq("user_is_superadmin", true));
        } else {
            userAdmin = findOne(new QueryWrapper<UserAdmin>().eq("store_id", storeId).eq("role_id", 2));
        }

        if (userAdmin == null) {
            throw new BusinessException(__("商家信息不存在!"));
        }

        return userAdmin.getUserId();
    }

    @Override
    public Integer getNoticeUserId(Integer storeId) {
        Integer userId = null;

        if (CheckUtil.isEmpty(storeId)) {
            userId = configBaseService.getConfig("message_notice_user_id", 10001);
        } else {
            UserAdmin userAdmin = findOne(new QueryWrapper<UserAdmin>().eq("store_id", storeId).eq("role_id", 2));

            if (userAdmin == null) {
                throw new BusinessException(__("商家信息不存在!"));
            }
            userId = userAdmin.getUserId();
        }

        return userId;
    }
}
