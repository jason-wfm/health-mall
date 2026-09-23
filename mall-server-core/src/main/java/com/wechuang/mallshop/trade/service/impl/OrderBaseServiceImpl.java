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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.model.entity.ProductValidPeriod;
import com.wechuang.mallshop.pt.repository.ProductValidPeriodRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.entity.OrderInfo;
import com.wechuang.mallshop.trade.model.entity.OrderItem;
import com.wechuang.mallshop.trade.model.req.OrderBaseListReq;
import com.wechuang.mallshop.trade.model.res.OrderCommentRes;
import com.wechuang.mallshop.trade.model.vo.EvaluationVo;
import com.wechuang.mallshop.trade.model.vo.OrderItemVo;
import com.wechuang.mallshop.trade.repository.OrderBaseRepository;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import com.wechuang.mallshop.trade.repository.OrderItemRepository;
import com.wechuang.mallshop.trade.service.OrderBaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 订单详细信息-检索不分表也行，cache 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-07-03
 */
@Service
public class OrderBaseServiceImpl extends BaseServiceImpl<OrderBaseRepository, OrderBase, OrderBaseListReq> implements OrderBaseService {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private ProductValidPeriodRepository productValidPeriodRepository;

    /**
     * 获取店铺的评论
     *
     * @param evaluationVo
     * @return
     */
    @Override
    public OrderCommentRes getEvaluationItem(EvaluationVo evaluationVo) {
        OrderCommentRes commentRes = new OrderCommentRes();

        String orderId = evaluationVo.getOrderId();
        Integer userId = evaluationVo.getUserId();
        QueryWrapper<OrderItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("user_id", userId).in("order_item_evaluation_status", evaluationVo.getOrderItemEvaluationStatus());

        // 如果传入订单则单个订单进行查询，否则查询出已完成的订单ID
        List<Serializable> orderIds = null;
        if (StrUtil.isNotEmpty(orderId)) {
            itemQueryWrapper.eq("order_id", orderId);
        } else {
            QueryWrapper<OrderInfo> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.eq("user_id", userId).in("order_state_id", StateCode.ORDER_STATE_FINISH, StateCode.ORDER_STATE_RECEIVED);
            orderIds = orderInfoRepository.findKey(infoQueryWrapper);

            if (CollUtil.isNotEmpty(orderIds)) {
                itemQueryWrapper.in("order_id", orderIds);
            }
        }
        List<OrderItem> orderItems = orderItemRepository.find(itemQueryWrapper);
        List<OrderItemVo> orderItemVos = new ArrayList<>();
        orderItems.forEach(orderItem -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem, orderItemVo);
            orderItemVos.add(orderItemVo);
        });

        commentRes.setItems(orderItemVos);
        commentRes.setNo(0);
        commentRes.setYes(0);

        List<Long> itemIds = new ArrayList<>();
        Iterator<OrderItem> iterator = orderItems.iterator();
        while (iterator.hasNext()) {
            OrderItem orderItem = iterator.next();
            Long itemId = orderItem.getItemId();
            if (itemIds.contains(itemId)) {
                iterator.remove();
            } else {
                itemIds.add(itemId);
            }
        }

        if (StrUtil.isBlank(orderId) && CollUtil.isNotEmpty(orderIds)) {
            QueryWrapper<OrderItem> evaluationNoWrapper = new QueryWrapper<>();
            evaluationNoWrapper.eq("user_id", userId).eq("order_item_evaluation_status", StateCode.ORDER_ITEM_EVALUATION_YES).in("order_id", orderIds);
            int noNum = orderItemRepository.findKey(evaluationNoWrapper).size();

            QueryWrapper<OrderItem> evaluationYesWrapper = new QueryWrapper<>();
            evaluationYesWrapper.eq("user_id", userId).eq("order_item_evaluation_status", StateCode.ORDER_ITEM_EVALUATION_YES).in("order_id", orderIds);
            int yesNum = orderItemRepository.findKey(evaluationYesWrapper).size();
            commentRes.setNo(noNum);
            commentRes.setYes(yesNum);
        }

        return commentRes;
    }

    @Override
    public Long getWithdrawTime() {
        //订单可提现时间
        Date now = new Date();
        Long withdrawTime = now.getTime();
        Float withdrawReceivedDay = configBaseService.getConfig("withdraw_received_day", 7f);
        int second = NumberUtil.mul(withdrawReceivedDay, 60, 60, 24).intValue();

        if (withdrawTime >= 0) {
            withdrawTime = DateUtil.offsetSecond(now, -second).getTime();
        } else {
            throw new BusinessException(__("系统设置可结算时间有误！"));
        }

        return withdrawTime;
    }

    @Override
    public boolean handleCanStudy(Long productId, String orderId) {
        boolean result = false;

        ProductValidPeriod productValidPeriod = productValidPeriodRepository.get(productId);

        if (productValidPeriod == null) {
            throw new BusinessException(__("虚拟商品信息不存在！"));
        }
        Integer validPeriod = productValidPeriod.getProductValidPeriod();
        Integer productValidityDuration = productValidPeriod.getProductValidityDuration();
        Long productValidityEnd = productValidPeriod.getProductValidityEnd();

        if (ObjectUtil.equal(validPeriod, 1001)) {
            result = true;
        } else if (ObjectUtil.equal(validPeriod, 1002)) {

            if (CheckUtil.isNotEmpty(productValidityEnd)) {
                result = productValidityEnd - new Date().getTime() > 0;
            }
        } else if (ObjectUtil.equal(validPeriod, 1003)) {
            // 查询购买记录
            OrderBase shopOrderBase = orderBaseRepository.get(orderId);

            if (shopOrderBase == null) {
                throw new BusinessException(__("该订单详细信息不存在！"));
            }
            Date orderTime = shopOrderBase.getOrderTime();
            DateTime validTime = DateUtil.offsetDay(DateUtil.beginOfDay(orderTime), productValidityDuration);
            result = validTime.getTime() - new Date().getTime() > 0;
        }

        return result;
    }
}
