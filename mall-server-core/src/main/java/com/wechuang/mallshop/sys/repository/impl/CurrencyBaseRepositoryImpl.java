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
package com.wechuang.mallshop.sys.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.sys.dao.CurrencyBaseDao;
import com.wechuang.mallshop.sys.model.entity.CurrencyBase;
import com.wechuang.mallshop.sys.repository.CurrencyBaseRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


/**
 * <p>
 * 货币设置表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-08-08
 */
@Repository
public class CurrencyBaseRepositoryImpl extends BaseRepositoryImpl<CurrencyBaseDao, CurrencyBase> implements CurrencyBaseRepository {
    private static final Map<String, BigDecimal> currencyRateMap = new HashMap<>();

    @Override
    public BigDecimal getMultiCurrencyRate(BigDecimal price, String currencyLang) {
        if (price == null) {
            return price;
        }

        if (!currencyRateMap.containsKey(currencyLang)) {
            CurrencyBase currencyBase = findOne(new LambdaQueryWrapper<CurrencyBase>().eq(CurrencyBase::getCurrencyLang, currencyLang));
            if (currencyBase == null) {
                throw new BusinessException(__(String.format("%s 汇率未设置", currencyLang)));
                //return BigDecimal.ONE;
            }

            if (currencyBase.getCurrencyExchangeRate().equals(BigDecimal.ZERO)) {
                throw new BusinessException(__(String.format("%s 汇率不能设置未空", currencyLang)));
            }

            currencyRateMap.put(currencyLang, currencyBase.getCurrencyExchangeRate());
        }

        return currencyRateMap.get(currencyLang).multiply(price);
    }

    @Override
    public Map<String, BigDecimal> initCurrencyRateMap() {

        if (currencyRateMap.isEmpty()) {
            List<CurrencyBase> currencyBases = find(new LambdaQueryWrapper<CurrencyBase>());

            for (CurrencyBase it : currencyBases) {
                currencyRateMap.put(it.getCurrencyLang(), it.getCurrencyExchangeRate());
            }
        }

        return currencyRateMap;
    }

    @Override
    public boolean edit(CurrencyBase a) {
        boolean edit = super.edit(a);
        currencyRateMap.remove(a.getCurrencyLang());

        return edit;
    }

    public boolean ifEffective(String to) {
        return currencyRateMap.containsKey(to);
    }

    @Override
    public CurrencyBase getCurrency() {
        String currencyLang = ContextUtil.getCurrentLang();

        if (currencyLang != null) {
            /*
            switch (currencyLang) {
                case "en_US":
                case "en_GB":
                    currencyLang = "en_GB";
                    break;
                case "es_ES":
                case "es_MX":
                    currencyLang = "es_MX";
                    break;
            }
             */

            currencyLang = currencyLang.replace("_", "-");
            CurrencyBase currencyBase = findOne(new LambdaQueryWrapper<CurrencyBase>().eq(CurrencyBase::getCurrencyLang, currencyLang));

            if (currencyBase == null) {
                throw new BusinessException(__(String.format("%s 汇率未设置", currencyLang)));
            }

            if (currencyBase.getCurrencyExchangeRate().equals(BigDecimal.ZERO)) {
                throw new BusinessException(__(String.format("%s 汇率不能设置未空", currencyLang)));
            }

            return currencyBase;
        }

        return null;
    }
}
