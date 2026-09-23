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
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.pt.dao.ProductIndexDao;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.repository.ProductIndexRepository;
import com.wechuang.mallshop.sys.repository.CurrencyBaseRepository;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * <p>
 * 产品索引表-不读取数据只读主键 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-03-13
 */
@Repository
public class ProductIndexRepositoryImpl extends BaseRepositoryImpl<ProductIndexDao, ProductIndex> implements ProductIndexRepository {
    @Autowired
    private LangMetaRepository langMetaRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private CurrencyBaseRepository currencyBaseRepository;

    @Override
    public List<ProductIndex> gets(Collection<? extends Serializable> a) {
        List<ProductIndex> list = super.gets(a);

        String to = ContextUtil.getToLang();
        if (to == null) {
            return list;
        }

        boolean multiCurrencyEnable = configBaseService.getConfig("multi_currency_enable", false);
        String currencyLang = to.replace("_", "-");

        for (ProductIndex it : list) {
            if (ObjectUtil.isNotEmpty(it)) {
                //使用product_base 中那么替换即可。
                String translate = langMetaRepository.getTranslate(it.getProductName(), to, ConstantConfig.BASE_LANG, "pt_product_base", Convert.toStr(it.getProductId()), "product_name", 0);
                it.setProductName(translate);

                if (multiCurrencyEnable) {
                    //多语言汇率转换
                    it.setProductUnitPriceMin(currencyBaseRepository.getMultiCurrencyRate(it.getProductUnitPriceMin(), currencyLang));
                    it.setProductUnitPriceMax(currencyBaseRepository.getMultiCurrencyRate(it.getProductUnitPriceMax(), currencyLang));
                }
            }
        }

        return list;
    }
}
