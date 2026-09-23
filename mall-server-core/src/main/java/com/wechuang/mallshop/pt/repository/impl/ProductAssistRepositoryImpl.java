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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.pt.dao.ProductAssistDao;
import com.wechuang.mallshop.pt.model.entity.ProductAssist;
import com.wechuang.mallshop.pt.model.entity.ProductAssistItem;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import com.wechuang.mallshop.pt.repository.ProductAssistItemRepository;
import com.wechuang.mallshop.pt.repository.ProductAssistRepository;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 商品辅助属性表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Repository
public class ProductAssistRepositoryImpl extends BaseRepositoryImpl<ProductAssistDao, ProductAssist> implements ProductAssistRepository {

    @Autowired
    private ProductAssistItemRepository productAssistItemRepository;

    @Override
    public Boolean updateAssistItem(Integer assistId) {
        QueryWrapper<ProductAssistItem> assistItemQueryWrapper = new QueryWrapper<>();
        assistItemQueryWrapper.eq("assist_id", assistId);
        List<ProductAssistItem> assistItemList = productAssistItemRepository.find(assistItemQueryWrapper);

        List<String> column = CommonUtil.column(assistItemList, ProductAssistItem::getAssistItemName);
        ProductAssist productAssist = new ProductAssist();
        productAssist.setAssistId(assistId);
        productAssist.setAssistItem(CollUtil.join(column, ","));

        return edit(productAssist);
    }

    /**
     * 获取商品辅助属性
     *
     * @return
     */
    @Override
    public List<ProductAssistOutput> getAssists(String assistIds) {
        if (CheckUtil.isEmpty(assistIds)) {
            return new ArrayList<>();
        }

        List<Integer> assistIdList = Convert.toList(Integer.class, assistIds);

        if (CollUtil.isEmpty(assistIdList)) {
            return new ArrayList<>();
        }

        QueryWrapper<ProductAssist> assistQueryWrapper = new QueryWrapper<>();
        assistQueryWrapper.in("assist_id", assistIdList);
        List<ProductAssist> assistList = find(assistQueryWrapper);

        List<ProductAssistOutput> assistOutPuts = assistList.stream().map(assist -> {
            ProductAssistOutput productAssistOutPut = new ProductAssistOutput();
            BeanUtils.copyProperties(assist, productAssistOutPut);
            return productAssistOutPut;
        }).collect(Collectors.toList());

        QueryWrapper<ProductAssistItem> assistItemQueryWrapper = new QueryWrapper<>();
        assistItemQueryWrapper.in("assist_id", assistIdList);
        List<ProductAssistItem> assistItemList = productAssistItemRepository.find(assistItemQueryWrapper);

        for (ProductAssistOutput assistOutPut : assistOutPuts) {
            List<ProductAssistItem> assistItems = new ArrayList<>();
            for (ProductAssistItem assistItem : assistItemList) {
                if (assistOutPut.getAssistId().equals(assistItem.getAssistId())) {
                    assistItems.add(assistItem);
                }
            }
            assistOutPut.setItems(assistItems);
        }

        return assistOutPuts;
    }

    @Autowired
    private LangMetaRepository langMetaRepository;

    @Override
    public List<ProductAssist> gets(Collection<? extends Serializable> a) {
        List<ProductAssist> list = super.gets(a);

        String to = ContextUtil.getToLang();
        if (to == null) {
            return list;
        }

        for (ProductAssist it : list) {
            if (ObjectUtil.isNotEmpty(it)) {
                it.setAssistName(langMetaRepository.getTranslate(it.getAssistName(), to, ConstantConfig.BASE_LANG, "pt_product_assist", Convert.toStr(it.getAssistName()), "assist_name", 0));
            }
        }

        return list;
    }
}
