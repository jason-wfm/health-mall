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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserCard;
import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.model.req.UserCardListReq;
import com.wechuang.mallshop.account.repository.UserCardRepository;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.account.service.UserCardService;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户会员卡 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2024-07-10
 */
@Service
public class UserCardServiceImpl extends BaseServiceImpl<UserCardRepository, UserCard, UserCardListReq> implements UserCardService {

    @Autowired
    private UserLevelRepository userlevelRepository;

    @Override
    public IPage<UserCard> getList(UserCardListReq userCardListReq) {

        Page<UserCard> lists = lists(userCardListReq);   //获得该对象的page对象的list集合 lists方法是自己封装的

        //lists.getRecords()即获得该page对象的普通list集合对象
        if (lists != null && CollectionUtil.isNotEmpty(lists.getRecords())) {
            List<UserCard> ListUserCard = lists.getRecords();
            //通过UserCard对象的user_level_id一列值获得对应的UserLevel对象的所有集合
            //这里map集合的 Integer值为集合的user_level_id值，即key值
            Map<Integer, UserLevel> userLevelMap = userlevelRepository.getUserLevelMap(CommonUtil.column(ListUserCard, UserCard::getUserLevelId));
            //遍历UserCard对象的map集合
            for (UserCard userCard : ListUserCard) {
                if (CollUtil.isNotEmpty(userLevelMap)) {
                    UserLevel userLevel = userLevelMap.get(userCard.getUserLevelId());
                    if (userLevel != null) {
                        userCard.setUserLevelName(userLevel.getUserLevelName());
                    }
                }
            }
        }
        return lists;
    }
}





