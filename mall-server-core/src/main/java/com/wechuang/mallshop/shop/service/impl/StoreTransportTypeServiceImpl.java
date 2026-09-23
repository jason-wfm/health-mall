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

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.model.entity.ProductBase;
import com.wechuang.mallshop.pt.repository.ProductBaseRepository;
import com.wechuang.mallshop.shop.model.entity.StoreTransportType;
import com.wechuang.mallshop.shop.model.req.StoreTransportTypeListReq;
import com.wechuang.mallshop.shop.repository.StoreTransportTypeRepository;
import com.wechuang.mallshop.shop.service.StoreTransportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 自定义物流运费及售卖区域类型表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-11
 */
@Service
public class StoreTransportTypeServiceImpl extends BaseServiceImpl<StoreTransportTypeRepository, StoreTransportType, StoreTransportTypeListReq> implements StoreTransportTypeService {

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private StoreTransportTypeRepository storeTransportTypeRepository;

    /**
     * 修改物流运费内置状态
     *
     * @param transportTypeId
     * @param transportTypeBuildin
     * @return
     */
    @Override
    public boolean editState(Integer transportTypeId, Boolean transportTypeBuildin) {
        StoreTransportType storeTransportType = new StoreTransportType();
        storeTransportType.setTransportTypeId(transportTypeId);
        storeTransportType.setTransportTypeBuildin(transportTypeBuildin);

        return edit(storeTransportType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeStoreTransportType(Integer transportTypeId) {
        QueryWrapper<ProductBase> productBaseQueryWrapper = new QueryWrapper<>();
        productBaseQueryWrapper.eq("transport_type_id", transportTypeId);
        List<Serializable> productIds = productBaseRepository.findKey(productBaseQueryWrapper);

        if (CollectionUtil.isNotEmpty(productIds)) {
            throw new BusinessException(String.format(__("该运费模板已被使用！productIds：%s！"), productIds.toString()));
        }

        // todo removeItem
        return remove(transportTypeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateTransport(StoreTransportType storeTransportType) {

        if (storeTransportType.getTransportIsDefault()) {
            Integer storeId = storeTransportType.getStoreId();

            QueryWrapper<StoreTransportType> typeQueryWrapper = new QueryWrapper<>();
            typeQueryWrapper.eq("store_id", CheckUtil.isNotEmpty(storeId) ? storeId : 0);
            typeQueryWrapper.eq("transport_is_default", true);

            if (CheckUtil.isNotEmpty(storeTransportType.getTransportTypeId())) {
                typeQueryWrapper.ne("transport_type_id", storeTransportType.getTransportTypeId());
            }
            List<StoreTransportType> storeTransportTypes = find(typeQueryWrapper);

            if (CollectionUtil.isNotEmpty(storeTransportTypes)) {
                storeTransportTypes.forEach(t -> {
                    t.setTransportIsDefault(false);
                });

                if (!storeTransportTypeRepository.saveOrUpdate(storeTransportTypes)) {
                    throw new BusinessException(__("批量修改失败！"));
                }
            }
        }

        return storeTransportTypeRepository.saveOrUpdate(storeTransportType);
    }
}
