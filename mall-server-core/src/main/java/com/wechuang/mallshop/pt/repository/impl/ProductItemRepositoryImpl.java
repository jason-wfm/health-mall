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
package com.wechuang.mallshop.pt.repository.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.consts.ConstantRedis;
import com.wechuang.mallshop.core.thread.RequestContextVariable;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.core.web.service.RedisService;
import com.wechuang.mallshop.pt.dao.ProductItemDao;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.repository.ProductItemRepository;
import com.wechuang.mallshop.sys.repository.CurrencyBaseRepository;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * <p>
 * 商品SKU表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-03-13
 */
@Repository
public class ProductItemRepositoryImpl extends BaseRepositoryImpl<ProductItemDao, ProductItem> implements ProductItemRepository {
    @Autowired
    private ProductItemDao productItemDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private CurrencyBaseRepository currencyBaseRepository;


    public int lockSkuStock(Long itemId, int cartQuantity) {
        int i = productItemDao.lockSkuStock(itemId, cartQuantity);

        if (i > 0) {
            removeCache(itemId);
        }

        return i;
    }

    public int releaseSkuStock(Long itemId, int cartQuantity) {
        int i = productItemDao.releaseSkuStock(itemId, cartQuantity);

        if (i > 0) {
            removeCache(itemId);
        }

        return i;
    }

    public int pickingSkuStock(Long itemId, int cartQuantity) {
        int i = productItemDao.pickingSkuStock(itemId, cartQuantity);

        if (i > 0) {
            removeCache(itemId);
        }

        return i;
    }

    @Override
    public IPage<Long> listItemKey(Page<ProductItemInput> page, ProductItemInput params) {
        return productItemDao.listItemKey(page, params);
    }

    public boolean removeCache(Long itemId) {
        //删除缓存
        String key = ConstantRedis.Cache_NameSpace + "pt_product_item:" + itemId;
        if (StrUtil.isNotBlank(key)) {
            RequestContextVariable.addCacheKey(key);
            if (!TransactionSynchronizationManager.isActualTransactionActive()) {
                redisService.del(key);
            }
        }

        return true;
    }


    @Autowired
    private LangMetaRepository langMetaRepository;

    @Override
    public List<ProductItem> gets(Collection<? extends Serializable> a) {
        List<ProductItem> list = super.gets(a);

        String to = ContextUtil.getToLang();
        if (to == null) {
            return list;
        }

        boolean multiCurrencyEnable = configBaseService.getConfig("multi_currency_enable", false);
        String currencyLang = to.replace("_", "-");

        for (ProductItem it : list) {
            if (ObjectUtil.isNotEmpty(it)) {
                it.setItemName(langMetaRepository.getTranslate(it.getItemName(), to, ConstantConfig.BASE_LANG, "pt_product_item", Convert.toStr(it.getItemId()), "product_item_name", 0));

                /*
                List<Map> productSpecList = new ArrayList<>();
                productSpecList = JSONUtil.parseArray(it.getItemSpec(), Map.class);

                if (CollUtil.isNotEmpty(productSpecList)) {
                    try {
                        for (Map spec : productSpecList) {
                            Object id = spec.get("id");
                            Object name = spec.get("name");
                            if (name != null) {
                                if (CheckUtil.isNotEmpty(name.toString())) {
                                    String sepc = langMetaRepository.getTranslate(name.toString(), to, ConstantConfig.BASE_LANG, "spec", "spec", null, 0);
                                    if (CheckUtil.isNotEmpty(sepc)) {
                                        spec.put("name", sepc);
                                    }
                                }
                            }

                            for (Map specItem : (List<Map>) spec.get("item")) {
                                Object specItemName = specItem.get("name");
                                if (specItemName != null) {
                                    if (CheckUtil.isNotEmpty(specItemName.toString())) {
                                        String itemName = langMetaRepository.getTranslate(specItemName.toString(), to, ConstantConfig.BASE_LANG, "spec", "spec", null, 0);
                                        if (CheckUtil.isNotEmpty(itemName)) {
                                            specItem.put("name", itemName);
                                        }
                                    }
                                }
                            }
                        }

                        // 使用ObjectMapper转换为JSON字符串
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            String jsonString = mapper.writeValueAsString(productSpecList);
                            it.setItemSpec(jsonString);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    } catch (Exception e) {
                        //throw new BusinessException(e.getMessage());
                    }
                }
                 */

                //多语言汇率转换
                if (multiCurrencyEnable) {
                    it.setItemCostPrice(currencyBaseRepository.getMultiCurrencyRate(it.getItemCostPrice(), currencyLang));
                    it.setItemSalePrice(currencyBaseRepository.getMultiCurrencyRate(it.getItemSalePrice(), currencyLang));
                    it.setItemSavePrice(currencyBaseRepository.getMultiCurrencyRate(it.getItemSavePrice(), currencyLang));
                    it.setItemUnitPrice(currencyBaseRepository.getMultiCurrencyRate(it.getItemUnitPrice(), currencyLang));
                    it.setItemMarketPrice(currencyBaseRepository.getMultiCurrencyRate(it.getItemMarketPrice(), currencyLang));
                }
            }
        }

        return list;
    }
}
