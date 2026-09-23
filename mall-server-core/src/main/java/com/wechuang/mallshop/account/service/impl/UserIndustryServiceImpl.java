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
package com.wechuang.mallshop.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.model.entity.UserIndustry;
import com.wechuang.mallshop.account.model.req.UserIndustryListReq;
import com.wechuang.mallshop.account.model.res.UserIndustryRes;
import com.wechuang.mallshop.account.repository.UserIndustryRepository;
import com.wechuang.mallshop.account.service.UserIndustryService;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户行业表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2025-03-17
 */
@Service
public class UserIndustryServiceImpl extends BaseServiceImpl<UserIndustryRepository, UserIndustry, UserIndustryListReq> implements UserIndustryService {


    @Override
    public List<UserIndustryRes> tree(UserIndustryListReq userIndustryListReq) {
        List<UserIndustry> userIndustries = find(new BaseQueryWrapper<UserIndustry, UserIndustryListReq>(userIndustryListReq).getWrapper());

        if (CollectionUtil.isEmpty(userIndustries)) {
            return null;
        }
        List<UserIndustryRes> userIndustryResList = BeanUtil.copyToList(userIndustries, UserIndustryRes.class);

        return CommonUtil.toTreeData(userIndustryResList, 0,
                UserIndustryRes::getIndustryParentId,
                UserIndustryRes::getIndustryId,
                UserIndustryRes::setChildren);
    }

    @Override
    public boolean removeById(Integer industryId) {
        QueryWrapper<UserIndustry> userIndustryQueryWrapper = new QueryWrapper<>();
        userIndustryQueryWrapper.eq("industry_parent_id", industryId);
        long count = count(userIndustryQueryWrapper);

        if (count > 0) {
            throw new BusinessException(String.format("有%d个子行业，不可删除！", count));
        }

        return remove(industryId);
    }

}
