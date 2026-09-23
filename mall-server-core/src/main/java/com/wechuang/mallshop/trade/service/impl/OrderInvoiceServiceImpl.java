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
package com.wechuang.mallshop.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.entity.OrderInvoice;
import com.wechuang.mallshop.trade.model.req.OrderInvoiceListReq;
import com.wechuang.mallshop.trade.model.res.OrderInvoiceRes;
import com.wechuang.mallshop.trade.repository.OrderBaseRepository;
import com.wechuang.mallshop.trade.repository.OrderInvoiceRepository;
import com.wechuang.mallshop.trade.service.OrderInvoiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 订单发票管理表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-09
 */
@Service
public class OrderInvoiceServiceImpl extends BaseServiceImpl<OrderInvoiceRepository, OrderInvoice, OrderInvoiceListReq> implements OrderInvoiceService {

    @Autowired
    private OrderBaseRepository orderBaseRepository;


    @Override
    public IPage<OrderInvoiceRes> getList(OrderInvoiceListReq orderInvoiceListReq) {
        IPage<OrderInvoiceRes> orderInvoiceResPage = new Page<>();
        IPage<OrderInvoice> lists = lists(orderInvoiceListReq);

        if (CollectionUtil.isNotEmpty(lists.getRecords())) {
            BeanUtils.copyProperties(lists, orderInvoiceResPage);
            List<OrderInvoice> orderInvoices = lists.getRecords();
            List<String> orderIds = CommonUtil.column(orderInvoices, OrderInvoice::getOrderId);
            List<OrderBase> orderBases = orderBaseRepository.gets(orderIds);

            if (CollectionUtil.isEmpty(orderBases)) {
                throw new BusinessException(__("订单信息不存在！"));
            }
            Map<String, Integer> orderState = orderBases.stream().collect(Collectors.toMap(OrderBase::getOrderId, OrderBase::getOrderStateId, (k1, k2) -> k1));
            List<OrderInvoiceRes> orderInvoiceResList = new ArrayList<>();

            for (OrderInvoice orderInvoice : orderInvoices) {
                OrderInvoiceRes orderInvoiceRes = BeanUtil.copyProperties(orderInvoice, OrderInvoiceRes.class);

                if (orderState.containsKey(orderInvoice.getOrderId())) {
                    orderInvoiceRes.setOrderStateId(orderState.get(orderInvoice.getOrderId()));
                }
                orderInvoiceResList.add(orderInvoiceRes);
            }
            orderInvoiceResPage.setRecords(orderInvoiceResList);
        }

        return orderInvoiceResPage;
    }
}
