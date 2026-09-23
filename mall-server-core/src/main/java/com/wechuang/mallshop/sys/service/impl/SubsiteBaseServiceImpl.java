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
package com.wechuang.mallshop.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.repository.UserAdminRepository;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.SubsiteBase;
import com.wechuang.mallshop.sys.model.req.SubsiteBaseListReq;
import com.wechuang.mallshop.sys.model.res.SubsiteBaseRes;
import com.wechuang.mallshop.sys.repository.SubsiteBaseRepository;
import com.wechuang.mallshop.sys.service.SubsiteBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 城市分站表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2025-01-06
 */
@Service
public class SubsiteBaseServiceImpl extends BaseServiceImpl<SubsiteBaseRepository, SubsiteBase, SubsiteBaseListReq> implements SubsiteBaseService {

    @Autowired
    private UserAdminRepository userAdminRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public IPage<SubsiteBaseRes> getList(SubsiteBaseListReq subsiteBaseListReq) {
        IPage<SubsiteBaseRes> baseResPage = new Page<>();
        IPage<SubsiteBase> lists = lists(subsiteBaseListReq);

        if (lists != null && CollectionUtil.isNotEmpty(lists.getRecords())) {
            BeanUtil.copyProperties(lists, baseResPage);
            List<SubsiteBase> records = lists.getRecords();
            List<SubsiteBaseRes> subsiteList = new ArrayList<>();

            List<Integer> ids = CommonUtil.column(records, SubsiteBase::getSubsiteId);
            QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
            userAdminQueryWrapper.in("subsite_id", ids);
            List<UserAdmin> userAdmins = userAdminRepository.find(userAdminQueryWrapper);

            if (CollectionUtil.isNotEmpty(userAdmins)) {
                Map<Integer, Integer> userIdMap = userAdmins.stream().collect(Collectors.toMap(UserAdmin::getSubsiteId, UserAdmin::getUserId, (k1, k2) -> k1));
                List<Integer> userIds = CommonUtil.column(userAdmins, UserAdmin::getUserId);
                Map<Integer, UserInfo> userInfoMap = userInfoRepository.getUserInfoMap(userIds);

                for (SubsiteBase subsiteBase : records) {
                    SubsiteBaseRes subsiteBaseRes = BeanUtil.copyProperties(subsiteBase, SubsiteBaseRes.class);
                    Integer userId = userIdMap.get(subsiteBaseRes.getSubsiteId());
                    UserInfo userInfo = userInfoMap.get(userId);

                    if (userInfo != null) {
                        subsiteBaseRes.setUserAccount(userInfo.getUserAccount());
                        subsiteBaseRes.setUserNickname(userInfo.getUserNickname());
                    }
                    subsiteList.add(subsiteBaseRes);
                }
                baseResPage.setRecords(subsiteList);
            }
        }

        return baseResPage;
    }
}
