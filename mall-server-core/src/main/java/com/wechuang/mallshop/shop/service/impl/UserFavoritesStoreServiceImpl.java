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
package com.wechuang.mallshop.shop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.shop.model.entity.StoreAnalytics;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.UserFavoritesStore;
import com.wechuang.mallshop.shop.model.req.UserFavoritesStoreListReq;
import com.wechuang.mallshop.shop.model.vo.UserFavoritesStoreVo;
import com.wechuang.mallshop.shop.repository.StoreAnalyticsRepository;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.repository.UserFavoritesStoreRepository;
import com.wechuang.mallshop.shop.service.UserFavoritesStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 收藏的店铺 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2025-06-05
 */
@Service
public class UserFavoritesStoreServiceImpl extends BaseServiceImpl<UserFavoritesStoreRepository, UserFavoritesStore, UserFavoritesStoreListReq> implements UserFavoritesStoreService {

    @Autowired
    private StoreAnalyticsRepository storeAnalyticsRepository;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(UserFavoritesStore userFavoritesStore) {
        StoreAnalytics storeAnalytics = storeAnalyticsRepository.get(userFavoritesStore.getStoreId());
        if (storeAnalytics != null) {
            storeAnalytics.setStoreFavoriteNum(storeAnalytics.getStoreFavoriteNum() + 1);
            storeAnalyticsRepository.edit(storeAnalytics);
        } else {
            storeAnalytics = new StoreAnalytics();
            storeAnalytics.setStoreId(userFavoritesStore.getStoreId());
            storeAnalytics.setStoreFavoriteNum(1);
            storeAnalyticsRepository.add(storeAnalytics);
        }

        return add(userFavoritesStore);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(UserFavoritesStore userFavoritesStore) {
        StoreAnalytics storeAnalytics = storeAnalyticsRepository.get(userFavoritesStore.getStoreId());
        if (storeAnalytics != null) {
            storeAnalytics.setStoreFavoriteNum(storeAnalytics.getStoreFavoriteNum() - 1);
            storeAnalyticsRepository.edit(storeAnalytics);
        }

        return remove(userFavoritesStore.getFavoritesStoreId());
    }

    @Override
    public IPage<UserFavoritesStoreVo> getList(UserFavoritesStoreListReq userFavoritesStoreListReq) {
        IPage<UserFavoritesStoreVo> favoritesStoreVoIPage = new Page<>();

        IPage<UserFavoritesStore> favoritesStorePage = lists(userFavoritesStoreListReq);
        BeanUtil.copyProperties(favoritesStorePage, favoritesStoreVoIPage);
        List<UserFavoritesStore> favoritesStores = favoritesStorePage.getRecords();

        if (CollectionUtil.isNotEmpty(favoritesStores)) {
            List<UserFavoritesStoreVo> favoritesStoreVos = new ArrayList<>();
            List<Integer> storeIds = CommonUtil.column(favoritesStores, UserFavoritesStore::getStoreId);
            List<StoreBase> storeBases = storeBaseRepository.gets(storeIds);

            if (CollectionUtil.isNotEmpty(storeBases)) {
                Map<Integer, StoreBase> storeBaseMap = storeBases.stream().collect(Collectors.toMap(StoreBase::getStoreId, StoreBase -> StoreBase, (k1, k2) -> k1));

                for (UserFavoritesStore favoritesStore : favoritesStores) {
                    UserFavoritesStoreVo userFavoritesStoreVo = BeanUtil.copyProperties(favoritesStore, UserFavoritesStoreVo.class);
                    Integer storeId = userFavoritesStoreVo.getStoreId();

                    if (storeBaseMap.containsKey(storeId)) {
                        StoreBase storeBase = storeBaseMap.get(storeId);

                        userFavoritesStoreVo.setStoreName(storeBase.getStoreName());
                        userFavoritesStoreVo.setStoreLogo(storeBase.getStoreLogo());
                    }
                    favoritesStoreVos.add(userFavoritesStoreVo);
                }
                favoritesStoreVoIPage.setRecords(favoritesStoreVos);
            }
        }

        return favoritesStoreVoIPage;
    }

}
