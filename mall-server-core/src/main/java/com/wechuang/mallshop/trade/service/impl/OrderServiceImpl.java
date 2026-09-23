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
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.*;
import com.wechuang.mallshop.account.model.vo.ExperienceVo;
import com.wechuang.mallshop.account.repository.*;
import com.wechuang.mallshop.account.service.UserInvoiceService;
import com.wechuang.mallshop.account.service.WechatService;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.analytics.dao.AnalyticsOrderDao;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import com.wechuang.mallshop.analytics.model.vo.OrderNumVo;
import com.wechuang.mallshop.common.api.LevelCode;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.config.WxShippingUtil;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.excel.EasyExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.*;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.common.web.service.MessageService;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.invoicing.model.entity.StockBill;
import com.wechuang.mallshop.invoicing.model.entity.StockBillItem;
import com.wechuang.mallshop.invoicing.model.entity.WarehouseItem;
import com.wechuang.mallshop.invoicing.model.vo.StockBillVo;
import com.wechuang.mallshop.invoicing.repository.StockBillItemRepository;
import com.wechuang.mallshop.invoicing.repository.StockBillRepository;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.pay.model.entity.*;
import com.wechuang.mallshop.pay.repository.*;
import com.wechuang.mallshop.pay.service.UserResourceService;
import com.wechuang.mallshop.pt.dao.ProductItemDao;
import com.wechuang.mallshop.pt.model.entity.*;
import com.wechuang.mallshop.pt.model.input.ProductEditStockInput;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.*;
import com.wechuang.mallshop.pt.service.ProductBaseService;
import com.wechuang.mallshop.pt.service.ProductItemService;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.StoreAnalytics;
import com.wechuang.mallshop.shop.model.entity.StoreExpressLogistics;
import com.wechuang.mallshop.shop.model.entity.StoreShippingAddress;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.repository.*;
import com.wechuang.mallshop.shop.service.UserVoucherService;
import com.wechuang.mallshop.sys.model.entity.CurrencyBase;
import com.wechuang.mallshop.sys.model.entity.ExpressBase;
import com.wechuang.mallshop.sys.model.entity.FeedbackBase;
import com.wechuang.mallshop.sys.repository.CurrencyBaseRepository;
import com.wechuang.mallshop.sys.repository.ExpressBaseRepository;
import com.wechuang.mallshop.sys.repository.FeedbackBaseRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.NumberSeqService;
import com.wechuang.mallshop.trade.excel.BatchOrderTemp;
import com.wechuang.mallshop.trade.excel.BatchOrderTempListener;
import com.wechuang.mallshop.trade.model.entity.*;
import com.wechuang.mallshop.trade.model.input.*;
import com.wechuang.mallshop.trade.model.output.CheckoutOutput;
import com.wechuang.mallshop.trade.model.output.OrderAddOutput;
import com.wechuang.mallshop.trade.model.output.OrderNumOutput;
import com.wechuang.mallshop.trade.model.req.BatchOrderListReq;
import com.wechuang.mallshop.trade.model.req.OrderInfoListReq;
import com.wechuang.mallshop.trade.model.req.OrderInvoiceAddReq;
import com.wechuang.mallshop.trade.model.req.OrderReviewReq;
import com.wechuang.mallshop.trade.model.res.*;
import com.wechuang.mallshop.trade.model.vo.*;
import com.wechuang.mallshop.trade.repository.*;
import com.wechuang.mallshop.trade.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.consts.ConstantConfig.ORDER_PREFIX;
import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 订单详细信息-检索不分表也行，cache 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-07-03
 */
@Repository
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private OrderDataRepository orderDataRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ConsumeTradeRepository consumeTradeRepository;

    @Autowired
    private ConsumeDepositRepository consumeDepositRepository;

    @Autowired
    private OrderDeliveryAddressRepository orderDeliveryAddressRepository;

    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @Autowired
    private OrderStateLogRepository orderStateLogRepository;

    @Autowired
    private OrderLogisticsRepository orderLogisticsRepository;

    @Autowired
    private ConsumeRecordRepository consumeRecordRepository;

    @Autowired
    private StockBillRepository stockBillRepository;

    @Autowired
    private StockBillItemRepository stockBillItemRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private NumberSeqService numberSeqService;

    @Autowired
    private OrderReturnRepository orderReturnRepository;

    @Autowired
    private OrderReturnItemRepository orderReturnItemRepository;

    @Autowired
    private UserCartService userCartService;

    @Autowired
    private UserResourceRepository userResourceRepository;

    @Autowired
    private UserVoucherRepository userVoucherRepository;

    @Autowired
    private UserVoucherService userVoucherService;

    @Autowired
    private StoreAnalyticsRepository storeAnalyticsRepository;

    @Autowired
    private UserInvoiceService userInvoiceService;

    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private OrderReturnService orderReturnService;

    @Autowired
    private ProductBaseService productBaseService;

    @Autowired
    private StoreExpressLogisticsRepository storeExpressLogisticsRepository;

    @Autowired
    private ExpressBaseRepository expressBaseRepository;

    @Autowired
    private StoreShippingAddressRepository storeShippingAddressRepository;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private FeedbackBaseRepository feedbackBaseRepository;

    @Autowired
    private ActivityBaseRepository activityBaseRepository;

    @Autowired
    private UserResourceService UserResourceService;


    @Autowired
    private UserAnalyticsRepository UserAnalyticsRepository;

    @Autowired
    private CurrencyBaseRepository currencyBaseRepository;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private UserBankCardRepository userBankCardRepository;

    @Autowired
    private ProductItemDao productItemDao;

    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Autowired
    private ConsumeWithdrawRepository consumeWithdrawRepository;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private BatchOrderRepository batchOrderRepository;

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private UserInvoiceRepository userInvoiceRepository;

    @Autowired
    private ProductValidPeriodRepository productValidPeriodRepository;

    @Autowired
    private AnalyticsOrderDao analyticsOrderDao;

    /**
     * 读取订单
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderVo detail(String orderId) {

        OrderVo detail = new OrderVo();

        // 信息表
        OrderInfo orderInfo = orderInfoRepository.get(orderId);
        BeanUtils.copyProperties(orderInfo, detail);

        // 详情表
        OrderData orderData = orderDataRepository.get(orderId);
        BeanUtils.copyProperties(orderData, detail);

        // 基础表
        OrderBase orderBase = orderBaseRepository.get(orderId);
        BeanUtils.copyProperties(orderBase, detail);

        if (CheckUtil.isNotEmpty(orderInfo.getOrderSaleId())) {
            UserInfo userInfo = userInfoRepository.get(orderInfo.getOrderSaleId());

            if (userInfo != null) {
                detail.setSaleName(userInfo.getUserNickname());
            }
        }

        // 读取订单商品
        List<OrderItem> orderItems = orderItemRepository.find(new QueryWrapper<OrderItem>().in("order_id", orderId));

        if (CollectionUtil.isNotEmpty(orderItems)) {
            for (OrderItem orderItem : orderItems) {
                String itemSpecName = StrUtil.replaceChars(orderItem.getItemName(), ",", " ");
                orderItem.setProductItemName(orderItem.getProductName() + " " + itemSpecName);
            }
        }

        detail.setItems(orderItems);

        //售后服务
        List<OrderReturnItem> returnItemList = orderReturnItemRepository.find(new QueryWrapper<OrderReturnItem>().eq("order_id", orderId).ne("return_state_id", StateCode.RETURN_PROCESS_CANCEL));

        // 处理为map
        Map<Long, List<String>> orderReturnItemMap = new HashMap<>();

        for (OrderReturnItem item : returnItemList) {
            if (!orderReturnItemMap.containsKey(item.getOrderItemId())) {
                orderReturnItemMap.put(item.getOrderItemId(), new ArrayList<>());
            }

            orderReturnItemMap.get(item.getOrderItemId()).add(item.getReturnId());
        }
        Integer kindId = orderInfo.getKindId();

        for (OrderItem item : orderItems) {
            // 是否可以退货
            item.setIfReturn(item.getOrderItemQuantity() > item.getOrderItemReturnNum() && ifReturn(orderInfo.getOrderStateId(), orderInfo.getOrderIsPaid()));

            List<String> returnIds = ObjectUtil.defaultIfNull(orderReturnItemMap.get(item.getOrderItemId()), new ArrayList<>());
            item.setReturnIds(returnIds);
        }


        //商品库存
        List<Long> itemIds = CommonUtil.column(orderItems, OrderItem::getItemId);
        List<ProductItem> productItems = productItemRepository.gets(itemIds);

        List<WarehouseItem> warehouseItems = new ArrayList<>();
        for (ProductItem it : productItems) {
            WarehouseItem i = new WarehouseItem();
            BeanUtils.copyProperties(it, i);
            i.setWarehouseId(0);
            i.setItemId(it.getItemId());
            i.setWarehouseItemQuantity(it.getItemQuantity());
            warehouseItems.add(i);
        }

        detail.setWarehouseItems(warehouseItems);

        // 配送地址
        OrderDeliveryAddress orderDeliveryAddress = orderDeliveryAddressRepository.get(orderId);
        detail.setDelivery(orderDeliveryAddress);

        // 订单日志
        List<OrderStateLog> orderStateLogs = orderStateLogRepository.find(new QueryWrapper<OrderStateLog>().in("order_id", orderId));
        detail.setLogItems(orderStateLogs);

        // 物流记录
        List<OrderLogistics> orderLogistics = orderLogisticsRepository.find(new QueryWrapper<OrderLogistics>().in("order_id", orderId));
        detail.setLogistics(orderLogistics);

        // StockBill
        List<StockBillVo> stockBill = stockBillRepository.findDetail(new QueryWrapper<StockBill>().in("order_id", orderId));
        detail.setStockBill(stockBill);

        // ConsumeRecord
        List<ConsumeRecord> consumeRecords = consumeRecordRepository.find(new QueryWrapper<ConsumeRecord>().in("order_id", orderId).eq("trade_type_id", StateCode.TRADE_TYPE_SALES));
        detail.setConsumeRecord(consumeRecords);


        // ConsumeTrade
        List<ConsumeTrade> consumeTrades = consumeTradeRepository.find(new QueryWrapper<ConsumeTrade>().in("order_id", orderId));
        if (!consumeTrades.isEmpty()) {
            detail.setConsumeTrade(consumeTrades.get(0));
        }

        ConsumeDeposit consumeDeposit = consumeDepositRepository.findOne(new QueryWrapper<ConsumeDeposit>().eq("order_id", orderId));

        if (consumeDeposit != null && Objects.equals(consumeDeposit.getDepositPaymentType(), StateCode.PAYMENT_TYPE_BANK)) {
            detail.setConsumeDeposit(consumeDeposit);
        }

        //发票
        QueryWrapper<OrderInvoice> orderInvoiceQueryWrapper = new QueryWrapper<>();
        orderInvoiceQueryWrapper.eq("order_id", orderId);
        long count = orderInvoiceRepository.count(orderInvoiceQueryWrapper);
        detail.setInvoiceIsApply(count > 0);

        //是否取消
        detail.setIfBuyerCancel(ifCancel(orderInfo.getOrderStateId(), orderInfo.getOrderIsPaid()));

        // 订单倒计时
        boolean showCancelTime = configBaseService.getConfig("show_cancel_time", false);
        Float orderAutocancelTime = configBaseService.getConfig("order_autocancel_time", 0.0f);
        if (showCancelTime && orderAutocancelTime > 0 && Objects.equals(detail.getOrderStateId(), StateCode.ORDER_STATE_WAIT_PAY)) {
            Date orderTime = detail.getOrderTime();
            orderTime = DateUtil.offsetSecond(orderTime, (int) (orderAutocancelTime * 60 * 60));
            long remainPayTime = (orderTime.getTime() - new DateTime().getTime()) / 1000;
            detail.setRemainPayTime(remainPayTime);
        }

        return detail;
    }

    /**
     * 订单搜索查询列表
     *
     * @param in
     * @return
     */
    @Override
    public Page<OrderVo> lists(OrderInfoListReq in) {
        Page<OrderVo> out = new Page<>();
        QueryWrapper<OrderInfo> queryWrapper = null;

        if (CheckUtil.isEmpty(in.getOrderStateId())) {
            in.setOrderStateId(null);
            queryWrapper = new BaseQueryWrapper<OrderInfo, OrderInfoListReq>(in).getWrapper();
        } else {
            if (Objects.equals(in.getOrderStateId(), StateCode.ORDER_STATE_WAIT_SHIPPING) || Objects.equals(in.getOrderStateId(), StateCode.ORDER_STATE_PICKING)) {
                in.setOrderStateId(null);
                queryWrapper = new BaseQueryWrapper<OrderInfo, OrderInfoListReq>(in).getWrapper().in("order_state_id", Arrays.asList(StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING));
            } else {
                queryWrapper = new BaseQueryWrapper<OrderInfo, OrderInfoListReq>(in).getWrapper();
            }
        }

        if (StrUtil.isNotEmpty(in.getOrderIds())) {
            queryWrapper.in("order_id", Convert.toList(String.class, in.getOrderIds()));
        }

        //代客订单
        if (in.getIsReplace() != null) {
            if (in.getIsReplace()) {
                queryWrapper.ne("order_sale_id", 0);
            } else {
                queryWrapper.eq("order_sale_id", 0);
            }
        }

        // 门店订单
        if (in.getIsChain() != null) {
            if (in.getIsChain()) {
                queryWrapper.ne("chain_id", 0);
            } else {
                queryWrapper.eq("chain_id", 0);
            }
        }

        // 订单搜索查询列表
        Page<OrderInfo> lists = orderInfoRepository.lists(queryWrapper, in.getPage(), in.getSize());
        BeanUtils.copyProperties(lists, out);
        out.setRecords(BeanUtil.copyToList(lists.getRecords(), OrderVo.class));

        //是否可以取消
        if (CollectionUtil.isNotEmpty(out.getRecords())) {
            for (OrderVo orderVo : out.getRecords()) {
                orderVo.setIfBuyerCancel(ifCancel(orderVo.getOrderStateId(), orderVo.getOrderIsPaid()));
            }
        }

        List<String> order_id_row = CommonUtil.column(out.getRecords(), OrderVo::getOrderId);

        //判断是否有退款退货
        List<String> orderReturnIdList = new ArrayList<>();

        if (CollUtil.isNotEmpty(order_id_row)) {
            QueryWrapper<OrderReturn> qw = new QueryWrapper<>();
            qw.in("order_id", order_id_row);
            List<OrderReturn> orderReturnList = orderReturnRepository.find(qw);

            orderReturnIdList = CommonUtil.column(orderReturnList, OrderReturn::getOrderId);
        }

        //判断是否开了发票
        List<String> orderInvoiceIdList = new ArrayList<>();

        if (CollUtil.isNotEmpty(order_id_row)) {
            QueryWrapper<OrderInvoice> orderInvoiceQueryWrapper = new QueryWrapper<>();
            orderInvoiceQueryWrapper.in("order_id", order_id_row);
            List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.find(orderInvoiceQueryWrapper);

            orderInvoiceIdList = CommonUtil.column(orderInvoiceList, OrderInvoice::getOrderId);
        }

        List<String> ids = new ArrayList<>();

        // 补全商品基础表信息
        ids = CommonUtil.column(lists.getRecords(), OrderInfo::getOrderId);

        if (CollUtil.isNotEmpty(ids)) {
            List<OrderData> orderDataList = orderDataRepository.gets(ids);
            List<OrderBase> orderBaseList = orderBaseRepository.gets(ids);

            // 读取订单商品
            List<OrderItem> orderItems = orderItemRepository.find(new QueryWrapper<OrderItem>().in("order_id", ids).orderByDesc("order_item_id"));

            // 处理为map
            Map<String, List<OrderItem>> orderItemMap = new HashMap<>();
            Map<Long, Integer> stockBillItemNumMap = new HashMap<>();

            //根据订单查询发货单
            QueryWrapper<OrderLogistics> logisticsQueryWrapper = new QueryWrapper<>();
            logisticsQueryWrapper.in("order_id", ids);
            List<OrderLogistics> orderLogistics = orderLogisticsRepository.find(logisticsQueryWrapper);

            List<String> stockBillIds = CommonUtil.column(orderLogistics, OrderLogistics::getStockBillId);

            if (CollUtil.isNotEmpty(stockBillIds)) {
                QueryWrapper<StockBillItem> billeItemQueryWrapper = new QueryWrapper<>();
                billeItemQueryWrapper.in("stock_bill_id", stockBillIds);

                List<StockBillItem> stockBillItems = stockBillItemRepository.find(billeItemQueryWrapper);
                for (StockBillItem stockBillItem : stockBillItems) {
                    if (!stockBillItemNumMap.containsKey(stockBillItem.getOrderItemId())) {
                        stockBillItemNumMap.put(stockBillItem.getOrderItemId(), stockBillItem.getBillItemQuantity());
                    } else {
                        stockBillItemNumMap.put(stockBillItem.getOrderItemId(), stockBillItem.getBillItemQuantity() + stockBillItemNumMap.get(stockBillItem.getOrderItemId()));
                    }
                }
            }

            for (OrderItem item : orderItems) {
                if (!orderItemMap.containsKey(item.getOrderId())) {
                    orderItemMap.put(item.getOrderId(), new ArrayList<>());
                }

                //比较发货数量以及出库单数量是否对应订单数量
                if (stockBillItemNumMap.containsKey(item.getOrderItemId())) {
                    if (stockBillItemNumMap.get(item.getOrderItemId()).equals(item.getOrderItemQuantity())) {
                        item.setItemShipping(1);//已发货
                    } else {
                        item.setItemShipping(2);//部分发货
                    }
                }

                orderItemMap.get(item.getOrderId()).add(item);
            }

            for (OrderBase item : orderBaseList) {
                for (OrderVo vo : out.getRecords()) {
                    if (Objects.equals(item.getOrderId(), vo.getOrderId())) {
                        vo.setOrderNumber(item.getOrderNumber());
                        vo.setOrderTime(item.getOrderTime());
                        vo.setOrderProductAmount(item.getOrderProductAmount());
                        vo.setOrderPaymentAmount(item.getOrderPaymentAmount());
                        vo.setCurrencyId(item.getCurrencyId());
                        vo.setCurrencySymbolLeft(item.getCurrencySymbolLeft());
                        vo.setStoreName(item.getStoreName());
                        vo.setUserNickname(item.getUserNickname());
                    }
                }
            }

            for (OrderData item : orderDataList) {
                for (OrderVo vo : out.getRecords()) {
                    if (Objects.equals(item.getOrderId(), vo.getOrderId())) {
                        vo.setOrderReturnStatus(item.getOrderReturnStatus());
                        vo.setOrderRefundStatus(item.getOrderRefundStatus());
                        vo.setOrderMessage(item.getOrderMessage());
                        vo.setOrderShippingFee(item.getOrderShippingFee());
                        vo.setOrderCommissionFee(item.getOrderCommissionFee());
                        vo.setOrderCommissionFeeRefund(item.getOrderCommissionFeeRefund());
                        vo.setOrderRefundAgreeAmount(item.getOrderRefundAgreeAmount());
                        vo.setOrderDesc(item.getOrderDesc());
                    }
                }
            }

            // ConsumeTrade
            List<ConsumeTrade> consumeTrades = consumeTradeRepository.find(new QueryWrapper<ConsumeTrade>().in("order_id", ids));
            for (ConsumeTrade item : consumeTrades) {
                for (OrderVo vo : out.getRecords()) {
                    if (Objects.equals(item.getOrderId(), vo.getOrderId())) {
                        vo.setTradePaymentAmount(item.getTradePaymentAmount());
                    }
                }
            }

            for (OrderVo vo : out.getRecords()) {
                vo.setItems(orderItemMap.get(vo.getOrderId()));

                vo.setReturnFlag(orderReturnIdList.contains(vo.getOrderId()));

                //是否开了发票
                vo.setInvoiceIsApply(orderInvoiceIdList.contains(vo.getOrderId()));
            }

        }

        return out;
    }

    /**
     * 新增
     *
     * @param in
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderAddOutput add(CheckoutInput in) {
        CheckoutOutput out = userCartService.checkout(in);
        out.setIn(in);

        //添加订单
        OrderAddOutput output = addOrder(out);

        return output;
    }


    // 资源分配
    public static <T> void allocateResource(
            BigDecimal totalNeed,
            BigDecimal totalUse,
            List<T> items,
            Function<T, BigDecimal> needGetter,
            BiConsumer<T, BigDecimal> useSetter
    ) {

        if (totalNeed.compareTo(BigDecimal.ZERO) <= 0
                || totalUse.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal allocatedTotal = BigDecimal.ZERO;
        int size = items.size();

        for (int i = 0; i < size; i++) {
            T item = items.get(i);

            BigDecimal itemNeed = needGetter.apply(item);
            BigDecimal itemUse;

            if (i == size - 1) {
                itemUse = totalUse.subtract(allocatedTotal);
            } else {
                itemUse = totalUse
                        .multiply(itemNeed)
                        .divide(totalNeed, 2, RoundingMode.HALF_UP);

                allocatedTotal = allocatedTotal.add(itemUse);
            }

            useSetter.accept(item, itemUse);
        }
    }

    /**
     * 下单操作
     *
     * @param cartData 信息
     * @return bool  是否成功
     */
    public OrderAddOutput addOrder(CheckoutOutput cartData) {
        Date now = new Date();

        Integer userId = cartData.getUserId();

        String buyerUserNickname = cartData.getIn().getUserNickname();
        Integer orderSaleId = cartData.getIn().getOrderSaleId();
        Integer gbId = cartData.getIn().getGbId();
        Integer activityId = cartData.getIn().getActivityId();

        Float salePersonRate = configBaseService.getConfig("sale_person_rate", 100f);

        if (salePersonRate.compareTo(0f) <= 0) {
            throw new BusinessException(__("销售员折扣比率不能小于等于零！"));
        }


        BigDecimal orderSelMoneyAmount = BigDecimal.ZERO;
        BigDecimal orderSelPointsAmount = BigDecimal.ZERO;
        BigDecimal orderSelSpAmount = BigDecimal.ZERO;

        BigDecimal orderNeedPayPointsAmount = cartData.getOrderPointsAmount() == null ? BigDecimal.ZERO : cartData.getOrderPointsAmount();
        if (orderNeedPayPointsAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException(__("开源版不支持使用积分抵扣或积分换购"));
        }
        BigDecimal orderResourceExt1 = BigDecimal.ZERO;
        BigDecimal orderResourceExt1Use = BigDecimal.ZERO;

        UserResource userResource = userResourceRepository.get(cartData.getUserId());
        List<String> orderIdRow = null;

        boolean usePoint = false;
        BigDecimal pointsValueRate = BigDecimal.ZERO;

        // 判断众宝支付的众宝是否足够，不够则提示。 使用第二种资源扩展字段。
        BigDecimal orderNeedSpAmount = cartData.getOrderSpAmount();
        BigDecimal orderResourceExt2 = BigDecimal.ZERO;
        BigDecimal orderResourceExt2Use = BigDecimal.ZERO;

        if (CheckUtil.isNotEmpty(orderNeedSpAmount)) {
            orderResourceExt2 = orderNeedSpAmount;

            BigDecimal userSpTotal = userResource.getUserSp();
            if (userSpTotal.compareTo(orderNeedSpAmount) > -1) {
                orderResourceExt2Use = orderResourceExt2;
            } else {
                Float spVaueRate = configBaseService.getConfig("sp_vaue_rate", 0f);

                // 必须扣积分,设置错误，不扣积分，报异常。
                if (spVaueRate.floatValue() <= 0) {
                    orderResourceExt2Use = BigDecimal.ZERO;
                    throw new BusinessException(__("积分2不足，无法下单！"));
                } else {
                    // 按照已有的去使用
                    orderResourceExt2Use = userSpTotal;
                }
            }

            // 扣除众宝
            if (ObjectUtil.compare(orderResourceExt2Use, BigDecimal.ZERO) != 0) {
                String desc = String.format("%s 众宝兑换", orderResourceExt2Use);
                // todo User_ResourceModel::sp
            }
        }

        List<Long> cartIds = new ArrayList<>();
        orderIdRow = new ArrayList<>();

        CheckoutInput checkoutRow = cartData.getIn();
        Map category_rate_row = new HashMap();

        Integer chain_id = checkoutRow.getChainId();
        if (CheckUtil.isNotEmpty(chain_id) && chain_id > 0) {
            throw new BusinessException(__("开源版不支持连锁门店（O2O）下单"));
        }

        // 判断库存是否可以下单
        for (StoreItemVo storeItem : cartData.getItems()) {
            List<ProductItemVo> itemList = storeItem.getItems();

            for (ProductItemVo itemTmpRow : itemList) {

                //起订量
                Integer cartQuantity = itemTmpRow.getCartQuantity();
                Integer productMinimumOrder = itemTmpRow.getProductMinimumOrder();

                if (cartQuantity.compareTo(productMinimumOrder) < 0) {
                    throw new BusinessException(String.format(__("商品: %s 未达到起订量 %s，无法下单！"), itemTmpRow.getProductItemName(), productMinimumOrder));
                }

                if (itemTmpRow.getIsOos()) {
                    throw new BusinessException(String.format(__("商品: %s 不在可售区域"), itemTmpRow.getProductName()));
                }

                //0元主商品，不可以下单（开源版不支持纯积分换购）
                BigDecimal salePx = Convert.toBigDecimal(itemTmpRow.getItemSalePrice());
                BigDecimal unitPts = Convert.toBigDecimal(itemTmpRow.getItemUnitPoints());
                if (salePx.compareTo(BigDecimal.ZERO) <= 0 && unitPts.compareTo(BigDecimal.ZERO) > 0) {
                    throw new BusinessException(String.format(__("开源版不支持积分换购商品：%s"), itemTmpRow.getProductName()));
                }
                if (salePx.compareTo(BigDecimal.ZERO) <= 0 && unitPts.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessException(String.format(__("商品: %s 总价为0，不可以下单，请联系商家！"), itemTmpRow.getProductName()));
                }

                Integer cart_quantity = itemTmpRow.getCartQuantity();

                // 直接判断库存
                Integer item_quantity = itemTmpRow.getItemQuantity();
                Integer itemQuantityFrozen = itemTmpRow.getItemQuantityFrozen();
                Integer kind_id = itemTmpRow.getKindId();

                if (item_quantity < cart_quantity && ObjectUtil.notEqual(kind_id, StateCode.PRODUCT_KIND_EDU)) {
                    throw new BusinessException(String.format(__("商品: %s 库存不足！当前可购买： %d"), itemTmpRow.getProductName(), itemTmpRow.getItemQuantity()));
                }

                // 商品库存小于等于库存预警值,通知卖家
                Integer stockWarning = configBaseService.getConfig("stock_warning", 5);

                if ((item_quantity - itemQuantityFrozen) <= stockWarning) {
                    Integer sellerId = userAdminService.getNoticeUserId(itemTmpRow.getStoreId());

                    String messageId = "commodity-inventory-notice";
                    Map<String, Object> args = new HashMap<>();
                    args.put("product_id", itemTmpRow.getProductId());
                    args.put("item_id", itemTmpRow.getItemId());

                    messageService.sendNoticeMsg(sellerId, messageId, args);
                }

                // 判断下单数量必须大于0
                if (cart_quantity <= 0) {
                    throw new BusinessException(String.format(__("商品: %s 购买数量必须大于： %d"), itemTmpRow.getProductName(), 0));
                }
            }
        }

        for (StoreItemVo storeItemVo : cartData.getItems()) {
            List<ProductItemVo> itemsList = storeItemVo.getItems();
            ActivityBase activity = storeItemVo.getActivityBase();

            BigDecimal salePersonDiscount = storeItemVo.getSalePersonDiscount();
            // 判断店铺状态，关闭状态不可以下单
            if (!Boolean.TRUE.equals(storeItemVo.getStoreIsOpen())) {
                throw new BusinessException(__("店铺关闭中，不可以下单！"));
            }

            //类型判断
            List<Integer> kindIds = CommonUtil.column(itemsList, ProductItemVo::getKindId);

            if (kindIds.size() > 1) {
                throw new BusinessException(__("订单只可以购买同一种类商品"));
            }

            List<Integer> kinds = Arrays.asList(StateCode.PRODUCT_KIND_FUWU, StateCode.PRODUCT_KIND_CARD, StateCode.PRODUCT_KIND_EDU);

            if (kinds.contains(kindIds.get(0))) {
                if (itemsList.size() > 1) {
                    throw new BusinessException(__("服务类商品每单只可以购买一种商品"));
                }
            }

            OrderData orderData = new OrderData();
            BigDecimal orderPlusDiscountAmount = BigDecimal.ZERO;

            String orderId = numberSeqService.getNextSeqString(ORDER_PREFIX);

            List<OrderItem> item_rows = new ArrayList();
            OrderInfo orderInfo = new OrderInfo();

            //订单信息
            if (StrUtil.isNotBlank(orderId)) {
                // 快递信息更新
                UserDeliveryAddress address = cartData.getUserDeliveryAddress();

                if (ObjectUtil.isNotEmpty(address)) {
                    OrderDeliveryAddress delivery = new OrderDeliveryAddress();
                    delivery.setOrderId(orderId);
                    delivery.setDaName(address.getUdName());
                    delivery.setDaIntl(address.getUdIntl());
                    delivery.setDaMobile(address.getUdMobile());
                    delivery.setDaTelephone(address.getUdTelephone());
                    delivery.setDaProvinceId(address.getUdProvinceId());
                    delivery.setDaProvince(address.getUdProvince());
                    delivery.setDaCityId(address.getUdCityId());
                    delivery.setDaCity(address.getUdCity());
                    delivery.setDaCountyId(address.getUdCountyId());
                    delivery.setDaCounty(address.getUdCounty());
                    delivery.setDaAddress(address.getUdAddress());
                    delivery.setDaPostalcode(address.getUdPostalcode());
                    delivery.setDaTagName(address.getUdTagName());
                    delivery.setDaLatitude(address.getUdLatitude());
                    delivery.setDaLongitude(address.getUdLongitude());
                    delivery.setDaTime(now);

                    if (!orderDeliveryAddressRepository.save(delivery)) {
                        throw new BusinessException(__("保存订单配送地址数据失败!"));
                    }
                }
            } else {
                throw new BusinessException(__("生成订单编号异常!"));
            }

            Integer storeId = storeItemVo.getStoreId();
            String store_name = storeItemVo.getStoreName();
            // store_id=0 平台单默认自营（与 golershop OrderInfo 落库一致）
            Boolean storeIsSelfsupport = storeItemVo.getStoreIsSelfsupport();
            if (!CheckUtil.isNotEmpty(storeId)) {
                storeIsSelfsupport = true;
            }

            // 优惠券使用标记更新
            if (CheckUtil.isNotEmpty(storeItemVo.getUserVoucherId())) {
                UserVoucher userVoucher = new UserVoucher();
                //userVoucher.setUserVoucherId(storeItemVo.getUserVoucherId());
                userVoucher.setVoucherStateId(StateCode.VOUCHER_STATE_USED);
                userVoucher.setOrderId(orderId);
                userVoucher.setUserVoucherActivetime(now);

                if (!userVoucherRepository.edit(userVoucher, new QueryWrapper<UserVoucher>().eq("user_voucher_id", storeItemVo.getUserVoucherId()).eq("voucher_state_id", StateCode.VOUCHER_STATE_UNUSED))) {
                    throw new BusinessException(__("订单优惠券信息失败"));
                }
            }
            // end 优惠券使用标记更新


            //使用掉的积分额度
            BigDecimal order_resource_ext1_use_current = BigDecimal.ZERO;
            BigDecimal order_resource_ext1_need_money = BigDecimal.ZERO; //积分不足，使用钱支付的部分。
            BigDecimal productPointsSel = storeItemVo.getPointsAmount() == null ? BigDecimal.ZERO : storeItemVo.getPointsAmount(); // 店铺订单所需积分

            // 需要ext1，但是ext1不足， 将ext1变为money使用
            if (CheckUtil.isNotEmpty(orderResourceExt1)) {
                if (orderResourceExt1.compareTo(orderResourceExt1Use) > 0) {
                    order_resource_ext1_use_current = NumberUtil.div(NumberUtil.mul(orderResourceExt1Use, productPointsSel), orderResourceExt1);
                    // 将积分变成钱去支付
                    BigDecimal order_resource_ext1_need = NumberUtil.sub(productPointsSel, order_resource_ext1_use_current);

                    // 将积分换成钱
                    order_resource_ext1_need_money = NumberUtil.mul(pointsValueRate, order_resource_ext1_need);
                    BigDecimal order_money_select_items = storeItemVo.getMoneyAmount();

                    storeItemVo.setMoneyAmount(NumberUtil.add(order_money_select_items, NumberUtil.max(BigDecimal.ZERO, order_resource_ext1_need_money)));
                } else {
                    order_resource_ext1_use_current = productPointsSel;
                }
            }

            storeItemVo.setPointsAmount(order_resource_ext1_use_current); // 订单实际使用积分

            //积分不足，使用钱支付的部分均分到对应商品上。
            BigDecimal order_money_select_items = storeItemVo.getMoneyItemAmount();
            BigDecimal freight = storeItemVo.getFreightAmount();

            BigDecimal voucherAmount = storeItemVo.getVoucherAmount();

            BigDecimal order_payment_amount = storeItemVo.getMoneyAmount();
            order_payment_amount = NumberUtil.max(order_payment_amount, BigDecimal.valueOf(0));


            // Begin 均分积分
            if (CheckUtil.isNotEmpty(order_resource_ext1_use_current)) {
                allocateResource(
                        productPointsSel,
                        order_resource_ext1_use_current,
                        itemsList,
                        ProductItemVo::getItemPointsSubtotal,
                        ProductItemVo::setItemPointsSubtotal
                );
                usePoint = true;
            } else {
                usePoint = false;
            }
            // END 均分积分

            // begain 均分优惠券
            BigDecimal item_share_voucher = BigDecimal.valueOf(0);  //SKU占比
            BigDecimal now_tmp_voucher_total = BigDecimal.valueOf(0); //已分配额度
            BigDecimal tmp_item_total = BigDecimal.valueOf(0); //使用优惠券商品总额

            if (CheckUtil.isNotEmpty(voucherAmount)) {
                List<Long> voucher_item_ids = new ArrayList<>();
                UserVoucher userVoucher = userVoucherRepository.get(storeItemVo.getUserVoucherId());

                if (userVoucher != null && StrUtil.isNotEmpty(userVoucher.getItemId())) {
                    voucher_item_ids = Convert.toList(Long.class, userVoucher.getItemId());
                }

                //涉及商品个数
                int size = 0;

                //取出参与的产品的总值
                for (int idx = 0; idx < itemsList.size(); idx++) {
                    ProductItemVo _item = itemsList.get(idx);

                    if ((CollUtil.isNotEmpty(voucher_item_ids) && voucher_item_ids.contains(_item.getItemId())) || CollUtil.isEmpty(voucher_item_ids)) {
                        BigDecimal subtotal_tmp = Convert.toBigDecimal(_item.getItemSubtotal());
                        tmp_item_total = NumberUtil.add(tmp_item_total, subtotal_tmp);

                        size++;
                    }
                }

                int i = 0;

                for (int idx = 0; idx < itemsList.size(); idx++) {
                    ProductItemVo _item = itemsList.get(idx);

                    if ((CollUtil.isNotEmpty(voucher_item_ids) && voucher_item_ids.contains(_item.getItemId())) || CollUtil.isEmpty(voucher_item_ids)) {
                        i++;

                        //最后一个商品
                        if (i == size) {
                            item_share_voucher = NumberUtil.sub(voucherAmount, now_tmp_voucher_total);
                        } else {
                            item_share_voucher = NumberUtil.round(NumberUtil.mul(NumberUtil.div(_item.getItemSubtotal(), tmp_item_total), voucherAmount), 2);
                            now_tmp_voucher_total = NumberUtil.add(now_tmp_voucher_total, item_share_voucher);
                        }
                        _item.setItemVoucher(item_share_voucher);
                    }

                }
            }
            //end 优惠券均分

            //todo 处理店铺活动优惠

            // 店铺活动（开源版仅优惠券/弹窗，无礼包、砍价等销量写回）
            if (ObjectUtil.isNotEmpty(storeItemVo.getActivityBase())) {
                ActivityBase activityBase = storeItemVo.getActivityBase();
                Integer tid = activityBase.getActivityTypeId();
                if (tid != null && !Objects.equals(tid, StateCode.ACTIVITY_TYPE_VOUCHER) && !Objects.equals(tid, StateCode.ACTIVITY_TYPE_POP)) {
                    throw new BusinessException(__("该营销活动类型已停用，请清空购物车后重新下单。"));
                }
            }

            //todo 满减活动
            /*
            BigDecimal item_share_giftbag = BigDecimal.ZERO;

            //满减均摊
            if (CollUtil.isNotEmpty(reduction)) {
                for (int ri = 0; ri < ddr_len; ri++) {
                    Map value = discount_detail_rows.get(ri);

                    if (Convert.toList(value.get("item_ids")).contains(item.get("item_id"))) {
                        //数组最后一个用减法
                        if ((ri + 1) == itemsList.size()) {
                            item_share_giftbag = NumberUtil.sub(Convert.toBigDecimal(value.get("discount")), reduction_discount);
                            reduction_discount = BigDecimal.ZERO;
                        } else {
                            item_share_giftbag = NumberUtil.mul(NumberUtil.div(Convert.toBigDecimal(value.get("discount")), Convert.toBigDecimal(value.get("old_amount"))), Convert.toBigDecimal(item.get("subtotal")));
                            reduction_discount = NumberUtil.add(reduction_discount, item_share_giftbag);
                        }
                    }
                }
            }

             */


            //1、订单基础表
            OrderBase orderBase = new OrderBase();

            // 订单默认状态
            List<Integer> stateIdList = configBaseService.getStateIdList();
            Integer orderStateId = stateIdList.get(0);

            orderBase.setOrderId(orderId);
            orderBase.setOrderStateId(orderStateId);
            orderBase.setOrderProductAmount(storeItemVo.getProductAmount());
            orderBase.setOrderPaymentAmount(order_payment_amount);

            // 增加汇率
            if (configBaseService.getConfig("multi_currency_enable", false)) {
                CurrencyBase currency = currencyBaseRepository.getCurrency();
                if (currency != null) {
                    orderBase.setCurrencyId(currency.getCurrencyId());
                    orderBase.setCurrencySymbolLeft(currency.getCurrencySymbolLeft());
                }
            }

            // 修改最终下单数据
            orderSelMoneyAmount = NumberUtil.add(orderSelMoneyAmount, order_payment_amount);
            orderSelPointsAmount = NumberUtil.add(orderSelPointsAmount, order_resource_ext1_use_current);
            orderSelSpAmount = NumberUtil.add(orderSelSpAmount, orderResourceExt2);


            // 应付金额/应支付金额:order_goods_amount - order_discount_amount + order_shipping_fee - order_voucher_price - order_points_fee - order_adjust_fee
            // 手工调整默认为0，order_points_fee积分折扣暂未开启

            orderBase.setOrderTime(now);
            orderBase.setStoreId(storeId);
            orderBase.setStoreName(store_name);
            orderBase.setUserId(userId);
            orderBase.setUserNickname(buyerUserNickname);

            // 订单基础信息保存
            boolean flag = orderBaseRepository.save(orderBase);

            Integer delivery_type_id = checkoutRow.getDeliveryTypeId();

            // 2、订单信息保存处理
            if (flag) {
                //String orderTitle = itemsList.stream().map(s -> s.getItemName()).collect(Collectors.joining("|"));
                //String collect = itemsList.stream().map(ProductItemVo::getProductName).collect(Collectors.joining("|"));
                String orderTitle = itemsList.stream().map(s -> s.getProductName()).collect(Collectors.joining("|"));
                orderTitle = orderTitle.substring(0, Math.min(orderTitle.length(), 190));

                Integer subsite_id = 0;
                Integer payment_type_id = checkoutRow.getPaymentTypeId();
                boolean order_is_offline = false;
                Integer salesperson_id = 0;
                Integer distributor_user_id = 0;
                Integer store_type = 1;
                Integer _order_type = checkoutRow.getOrderType();
                String src_order_id = checkoutRow.getSrcOrderId();
                Integer payment_form_id = 1;
                Integer cart_type_id = 1;


                orderInfo.setOrderId(orderId);
                orderInfo.setOrderTitle(orderTitle); // 订单标题
                orderInfo.setStoreId(storeId);  // 卖家店铺编号
                orderInfo.setSubsiteId(subsite_id); // 所属分站
                orderInfo.setUserId(userId); // 买家编号

                if (CheckUtil.isNotEmpty(orderSaleId)) {
                    orderInfo.setOrderSaleId(orderSaleId);
                }

                orderInfo.setKindId(storeItemVo.getKindId()); // 订单种类(ENUM): 1201-实物 ; 1202-虚拟
                orderInfo.setOrderLockStatus(false);  // 锁定状态(BOOL):0-是正常;1-锁定
                orderInfo.setOrderIsSettlemented(0); // 订单是否结算(BOOL): 1-已结算; 0-未结算

                orderInfo.setOrderBuyerEvaluationStatus(0); // 买家针对订单对店铺评价(ENUM): 0-未评价;1-已评价;  2-已过期未评价
                orderInfo.setOrderSellerEvaluationStatus(0); // 卖家评价状态(ENUM):0-未评价;1-已评价;  2-已过期未评价
                orderInfo.setCreateTime(now.getTime());
                orderInfo.setOrderBuyerHidden(false); // 买家删除(BOOL): 1-是; 0-否
                orderInfo.setOrderShopHidden(false); // 店铺删除(BOOL): 1-是; 0-否
                orderInfo.setPaymentTypeId(payment_type_id); // 支付方式(ENUM):2-到付;1-在线支付
                orderInfo.setOrderStateId(orderStateId);
                orderInfo.setUpdateTime(now.getTime());
                orderInfo.setOrderIsReceived(false); // 订单审核(BOOL):0-未审核;1-已审核;
                orderInfo.setOrderFinanceReview(false); // 财务状态(BOOL):0-未审核;1-已审核
                orderInfo.setOrderIsPaid(StateCode.ORDER_PAID_STATE_NO); // 付款状态(BOOL):0-未付款;6-付款待审核;7-部分付款;1-已付款
                orderInfo.setOrderIsOut(StateCode.ORDER_PICKING_STATE_NO);
                orderInfo.setOrderIsShipped(StateCode.ORDER_SHIPPED_STATE_NO);
                orderInfo.setOrderIsReceived(false); // 收货状态(BOOL):0-未收货;1-已收货
                orderInfo.setChainId(chain_id);
                orderInfo.setDeliveryTypeId(delivery_type_id); // 配送方式
                orderInfo.setOrderIsOffline(order_is_offline); // 线下订单
                orderInfo.setCartTypeId(cart_type_id); // 类型
                orderInfo.setSalespersonId(salesperson_id); // 销售员编号，可以扩展为数组
                orderInfo.setDistributorUserId(distributor_user_id); // 分销商ID distributor_id = storeId
                orderInfo.setStoreIsSelfsupport(storeIsSelfsupport);
                orderInfo.setStoreType(store_type);
                orderInfo.setSrcOrderId(src_order_id);
                orderInfo.setOrderType(_order_type);
                orderInfo.setOrderIsSync(3); //默认不需要同步

                /*
                if (ObjectUtil.isNotEmpty(productIndex) && CheckUtil.isNotEmpty(productIndex.getCoupon_typeId())) {
                    orderInfo.setCoupon_typeId(productIndex.getCoupon_typeId());
                }
                */
                //orderInfo.setActivity_json(JSONUtil.toJsonStr(storeItemVo.get("discount_detail_rows")));

                orderInfo.setPaymentFormId(payment_form_id);

                // 店铺活动 - 非排他
                if (ObjectUtil.isNotEmpty(storeItemVo.getActivityBase())) {
                    orderInfo.setActivityId(Convert.toStr(storeItemVo.getActivityBase().getActivityId()));
                    orderInfo.setActivityTypeId(Convert.toStr(storeItemVo.getActivityBase().getActivityTypeId()));
                } else {
                    //单品活动
                    List<Integer> activityIds = CommonUtil.column(itemsList, ProductItemVo::getActivityId);

                    if (CollUtil.isNotEmpty(activityIds)) {
                        List<Integer> activityTypeIds = itemsList.stream().map(s -> {
                            if (ObjectUtil.isNotEmpty(s.getActivityInfo())) {
                                return s.getActivityInfo().getActivityTypeId();
                            }

                            return null;
                        }).filter(Objects::nonNull).distinct().collect(Collectors.toList());

                        orderInfo.setActivityId(StrUtil.join(",", activityIds));
                        orderInfo.setActivityTypeId(StrUtil.join(",", activityTypeIds));
                    }
                }

                // 订单基本info信息保存
                if (!orderInfoRepository.save(orderInfo)) {
                    throw new BusinessException(__("保存订单基础数据失败!"));
                }
            } else {
                throw new BusinessException(__("保存订单基础数据失败!"));
            }
            // end 订单信息保存处理

            if (flag) {
                // 服务类虚拟订单数据
                if (StateCode.PRODUCT_KIND_ENTITY != storeItemVo.getKindId().intValue()) {
                    if (StateCode.PRODUCT_KIND_EDU == storeItemVo.getKindId().intValue()) {
                        // todo 具体业务逻辑， 在支付完成处理。
                    } else {
                        if (Objects.equals(storeItemVo.getKindId(), StateCode.PRODUCT_KIND_CARD)) {
                            // 卡券类，发送code， 则代表交易完成，类似充话费、虚拟卡号等等
                            // todo 具体业务逻辑， 在支付完成处理。
                        }
                    }
                }

                BigDecimal shareSalePersonDiscount = BigDecimal.ZERO;

                // 店铺商品信息item_rows
                for (int i = 0; i < itemsList.size(); i++) {
                    ProductItemVo item = itemsList.get(i);

                    Long cartId = item.getCartId();

                    if (CheckUtil.isNotEmpty(cartId)) {
                        cartIds.add(cartId);
                    }

                    Boolean is_on_sale = item.getIsOnSale();

                    if (is_on_sale) {
                        OrderItem item_row = new OrderItem();

                        item_row.setOrderId(orderId);
                        item_row.setUserId(userId); // 买家user_id  冗余
                        item_row.setStoreId(storeId); // 店铺ID
                        item_row.setProductId(item.getProductId()); // 产品id
                        item_row.setProductName(item.getProductName());
                        item_row.setItemId(item.getItemId()); // 货品id
                        item_row.setItemSrcId(item.getItemSrcId()); // 货品id
                        item_row.setItemName(item.getItemName()); // 商品名称
                        item_row.setOrderItemFile(""); // 订单附件
                        item_row.setCategoryId(item.getCategoryId()); // 商品对应的类目ID

                        item_row.setItemUnitPrice(item.getItemUnitPrice()); // 商品价格单价
                        item_row.setItemUnitPoints(item.getItemUnitPoints()); // 商品价格单价
                        item_row.setItemUnitSp(BigDecimal.valueOf(0)); // 商品价格单价
                        item_row.setOrderItemSalePrice(item.getItemSalePrice()); // 商品实际成交价单价
                        item_row.setItemPolicyPrice(item.getItemPolicyPrice()); //会员专属价

                        item_row.setItemPlusDiscountAmount(item.getItemPlusDiscountAmount()); // PLUS优惠金额
                        item_row.setItemLevelDiscountAmount(item.getItemLevelDiscountAmount()); // 会员等级优惠金额
                        orderPlusDiscountAmount = orderPlusDiscountAmount.add(item.getItemPlusDiscountAmount());

                        item_row.setItemCostPrice(item.getItemCostPrice()); // 成本价
                        item_row.setOrderItemQuantity(item.getCartQuantity()); // 商品数量
                        item_row.setOrderItemInventoryLock(item.getProductInventoryLock()); // 锁库存时机
                        item_row.setOrderItemImage(item.getProductImage()); // 商品图片
                        item_row.setOrderItemReturnNum(0); // 退货数量
                        item_row.setOrderItemAmount(item.getItemSubtotal()); // 商品实际总金额 =  item_sale_price * goods_quantity
                        item_row.setOrderItemDiscountAmount(item.getItemDiscountAmount()); // 优惠金额  负数
                        item_row.setPolicyDiscountrate(item.getItemPolicyDiscountrate()); // 价格策略折扣率%
                        item_row.setOrderItemVoucher(item.getItemVoucher()); // 均分优惠券

                        BigDecimal itemSubtotal = item.getItemSubtotal();

                        if (CheckUtil.isNotEmpty(salePersonDiscount)) {
                            if (i + 1 == itemsList.size()) {
                                item_row.setOrderItemSalePersonDiscount(salePersonDiscount.subtract(shareSalePersonDiscount));
                            } else {
                                BigDecimal itemSaleSubtotal = itemSubtotal.multiply(BigDecimal.valueOf(salePersonRate)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                                item_row.setOrderItemSalePersonDiscount(NumberUtil.sub(itemSubtotal, itemSaleSubtotal));
                                shareSalePersonDiscount = shareSalePersonDiscount.add(item_row.getOrderItemSalePersonDiscount());
                            }
                        }

                        //item_row.setOrder_item_redemption_voucher(item_share_redemption); // 均分提货券
                        item_row.setOrderItemConfirmStatus(true);// 默认用户审核

                        /*
                        item_row.setItem_purchase_price(item_purchase_price);
                        item_row.setItem_purchase_rate(item_purchase_rate);
                        item_row.setItem_sales_rate(item_sales_rate);*/

                        // todo 积分费用
                        item_row.setOrderItemAdjustFee(BigDecimal.ZERO); // 手工调整金额 负数

                        item_row.setOrderItemPointsFee(usePoint ? item.getItemPointsSubtotal() : BigDecimal.ZERO); // 积分费用

                        BigDecimal orderItemPaymentAmount = item.getItemSubtotal();
                        if (CheckUtil.isNotEmpty(item_row.getOrderItemVoucher())) {
                            orderItemPaymentAmount = NumberUtil.sub(orderItemPaymentAmount, item_row.getOrderItemVoucher());
                        }

                        if (CheckUtil.isNotEmpty(item_row.getOrderItemReduce())) {
                            orderItemPaymentAmount = NumberUtil.sub(orderItemPaymentAmount, item_row.getOrderItemReduce());
                        }

                        if (CheckUtil.isNotEmpty(item_row.getOrderItemSalePersonDiscount())) {
                            orderItemPaymentAmount = NumberUtil.sub(orderItemPaymentAmount, item_row.getOrderItemSalePersonDiscount());
                        }
                        item_row.setOrderItemPaymentAmount(orderItemPaymentAmount); // 订单商品实际支付金额

                        item_row.setOrderItemEvaluationStatus(false); // 评价状态: 0-未评价;1-已评价

                        //活动订单处理
                        if (ObjectUtil.isNotEmpty(item.getActivityId()) && item.getActivityInfo() != null) {
                            item_row.setActivityTypeId(item.getActivityInfo().getActivityTypeId()); // 活动类型:0-默认;1101-加价购=搭配宝;1102-店铺满赠-小礼品;1103-限时折扣;1104-优惠套装;1105-店铺优惠券coupon优惠券;1106-拼团;1107-满减送;1108-阶梯价
                            item_row.setActivityId(item.getActivityId()); // 促销活动ID:与activity_type_id搭配使用, 团购ID/限时折扣ID/优惠套装ID

                            if (Objects.equals(item_row.getActivityTypeId(), StateCode.ACTIVITY_TYPE_GROUPBOOKING)
                                    || Objects.equals(item_row.getActivityTypeId(), StateCode.ACTIVITY_TYPE_CUTPRICE)
                                    || Objects.equals(item_row.getActivityTypeId(), StateCode.ACTIVITY_TYPE_GIFTBAG)) {
                                throw new BusinessException(__("该营销活动类型已停用，请清空购物车后重新下单。"));
                            }
                        } else if (activity != null) {
                            //砍价和A+B活动信息
                            item_row.setActivityTypeId(activity.getActivityTypeId());
                            item_row.setActivityId(activity.getActivityId());
                        }

                        item_row.setOrderItemDiscountAmount(item.getItemDiscountAmount()); // 优惠金额  负数


                        // 根据分类获取category_commission_rate
                        if (ObjectUtil.notEqual(item.getKindId(), StateCode.PRODUCT_KIND_EDU)) {
                            if (category_rate_row.get(item.getCategoryId()) != null) {

                            } else {
                                ProductCategory category_row = productCategoryRepository.get(item.getCategoryId());
                                category_rate_row.put(item.getCategoryId(), category_row.getCategoryCommissionRate());
                            }
                        }

                        /*
                        // 1.判断是否为供应商产品

                        // 根据单品计算佣金
                        if (store_tmp_row != null && store_tmp_row.getStore_type() == 2) {
                            // 供应商订单，不分佣。
                            item_row.setOrder_item_commission_rate(BigDecimal.valueOf(0)); // 分佣金比例

                            BigDecimal order_item_payment_amount = item_row.getOrder_item_payment_amount();
                            BigDecimal order_item_commission_rate = item_row.getOrder_item_commission_rate();
                            item_row.setOrder_item_commission_fee(NumberUtil.mul(order_item_payment_amount, NumberUtil.div(order_item_commission_rate, 100))); // 分佣金

                            // 平台价 - 成本结算价 = 分佣额度
                            BigDecimal item_platform_supplier_commission_fee = BigDecimal.valueOf(0);
                            BigDecimal item_fee = NumberUtil.max(BigDecimal.valueOf(0), item_platform_supplier_commission_fee);

                            BigDecimal _item_unit_price = item_row.getItem_unit_price();
                            item_row.setOrder_item_commission_rate(NumberUtil.mul(NumberUtil.div(item_fee, _item_unit_price), 100)); // 分佣金比例
                            item_row.setOrder_item_commission_fee(item_fee); // 分佣金
                        } else {
                        */
                        // 允许分销
                        Boolean productDistEnable = Convert.toBool(item.getProductDistEnable(), false);

                        if (productDistEnable) {
                            // 存在单品平台佣金行为
                            BigDecimal productCommissionRate = item.getProductCommissionRate();
                            BigDecimal paymentAmount = item_row.getOrderItemPaymentAmount();

                            if (ObjectUtil.compare(productCommissionRate, BigDecimal.ZERO) > 0) {
                                BigDecimal orderItemCommissionRate = productCommissionRate;
                                item_row.setOrderItemCommissionRate(orderItemCommissionRate); // 分佣金比例
                                item_row.setOrderItemCommissionFee(NumberUtil.div(NumberUtil.mul(paymentAmount, orderItemCommissionRate), 100)); // 分佣金
                            } else {
                                BigDecimal orderItemCommissionRate = (BigDecimal) category_rate_row.get(item.getCategoryId());
                                item_row.setOrderItemCommissionRate(orderItemCommissionRate);
                                item_row.setOrderItemCommissionFee(NumberUtil.div(NumberUtil.mul(paymentAmount, orderItemCommissionRate), 100)); // 分佣金
                            }
                        } else {
                            item_row.setOrderItemCommissionRate(BigDecimal.valueOf(0));
                            item_row.setOrderItemCommissionFee(BigDecimal.valueOf(0)); // 分佣金
                        }
                        /*
                        }


                        // 单品佣金
                        JSONObject source_item_id = (JSONObject) cartData.get("source_item_id");
                        JSONObject json_item = (JSONObject) source_item_id.get(item_row.getItemId());
                        if (json_item != null) {
                            Integer tmp_user_id = Convert.toInt(json_item.get("u"));
                            item_row.setOrderItemSalerId(tmp_user_id);
                        } else {
                            item_row.setOrderItemSalerId(0);
                        }

                        // 来源订单
                        String src_order_id = Convert.toStr(cartData.get("src_order_id"));
                        if (StrUtil.isNotBlank(src_order_id)) {
                            item_row.setSrc_orderId(src_order_id);
                        } else {
                            item_row.setSrc_orderId("");
                        }

                        if (CollUtil.isEmpty(pulse_packages)) {
                            item_rows.add(item_row);
                        }
                        */

                        // start 判断增加冻结库存
                        if (ObjectUtil.equal(item.getProductInventoryLock(), 1001)) {
                            if (productItemRepository.lockSkuStock(item.getItemId(), item.getCartQuantity()) <= 0) {
                                throw new BusinessException(String.format(__("更改: %s 冻结库存失败!"), item.getItemId()));
                            }
                        }

                        if (CheckUtil.isNotEmpty(item.getGiveId())) {
                            item_row.setOrderGiveId(item.getGiveId());
                        }
                        // end

                        item_rows.add(item_row);
                    } else {
                        if (!Boolean.TRUE.equals(storeItemVo.getStoreIsOpen())) {
                            throw new BusinessException(String.format(__("店铺：%s 已关闭，商品：%s 不可下单"), storeItemVo.getStoreName(), item.getItemName()));
                        }
                        throw new BusinessException(String.format(__("商品： %s 已经下架，不可下单"), item.getItemName()));
                    }
                }
                //满即送
                List<ProductItemVo> items = storeItemVo.getItems();

                for (ProductItemVo item : items) {
                    List<ProductItemVo> pulseGiftCart = item.getPulseGiftCart();

                    if (CollectionUtil.isNotEmpty(pulseGiftCart)) {
                        ProductItemVo productItemVo = pulseGiftCart.get(0);
                        OrderItem item_row = new OrderItem();

                        Long cartId = productItemVo.getCartId();

                        if (CheckUtil.isNotEmpty(cartId)) {
                            cartIds.add(cartId);
                        }

                        item_row.setOrderId(orderId);
                        item_row.setUserId(userId);
                        item_row.setProductId(productItemVo.getProductId());
                        item_row.setItemId(productItemVo.getItemId());
                        item_row.setProductName(productItemVo.getProductName());
                        item_row.setItemName(productItemVo.getItemName());
                        item_row.setOrderItemQuantity(productItemVo.getCartQuantity());
                        item_row.setOrderItemPaymentAmount(BigDecimal.ZERO);
                        item_row.setItemUnitSp(BigDecimal.ZERO);
                        item_row.setOrderItemCommissionFee(BigDecimal.ZERO);
                        item_row.setOrderItemImage(productItemVo.getProductImage());
                        item_row.setActivityTypeId(StateCode.ACTIVITY_TYPE_GIFT);
                        item_row.setActivityId(productItemVo.getActivityId());
                        item_row.setItemUnitPrice(productItemVo.getItemUnitPrice());
                        // 与主商品一致：店铺、类目、锁库存策略及下单冻结（1001 下单锁 / 1002 支付锁，支付时在 setPaidYes 冻结）
                        item_row.setStoreId(storeId);
                        item_row.setCategoryId(productItemVo.getCategoryId());
                        item_row.setItemSrcId(productItemVo.getItemSrcId());
                        item_row.setItemCostPrice(productItemVo.getItemCostPrice());
                        item_row.setOrderItemInventoryLock(productItemVo.getProductInventoryLock());
                        item_row.setOrderItemFile("");
                        item_row.setOrderItemReturnNum(0);
                        item_row.setOrderItemConfirmStatus(true);
                        item_row.setOrderItemEvaluationStatus(false);
                        if (ObjectUtil.equal(productItemVo.getProductInventoryLock(), 1001)) {
                            if (productItemRepository.lockSkuStock(productItemVo.getItemId(), productItemVo.getCartQuantity()) <= 0) {
                                throw new BusinessException(String.format(__("更改: %s 冻结库存失败!"), productItemVo.getItemId()));
                            }
                        }
                        item_rows.add(item_row);
                    }
                }

                for (OrderItem item_row : item_rows) {
                    if (item_row.getOrderItemPaymentAmount().compareTo(BigDecimal.ZERO) < 0) {
                        throw new BusinessException(__("活动价格设置有误"));
                    }
                }

                if (!orderItemRepository.saves(item_rows)) {
                    throw new BusinessException(__("保存订单信息数据失败!"));
                }


                orderData.setOrderId(orderId); // 订单编号
                orderData.setOrderDesc(""); // 订单描述
                orderData.setOrderDelayTime(0); // 延迟时间,默认为0 - 收货确认
                orderData.setDeliveryTypeId(delivery_type_id); // 配送方式

                /*
                //orderData.setDeliveryTimeId(delivery_time_id); // 配送时间:要求，不限、周一~周五、周末等等

                // 配送时间新加
                orderData.setDelivery_time(delivery_time);
                orderData.setDelivery_time_rang(CheckUtil.isNotEmpty(delivery_istimer) ? delivery_time_rang : 0);
                orderData.setDelivery_istimer(delivery_istimer);
                orderData.setBuyer_mobile(buyer_mobile);
                orderData.setOrder_heka(order_heka);
                 */
                String userMessage = "";
                if (CollUtil.isNotEmpty(checkoutRow.getMessage())) {
                    userMessage = ObjectUtil.defaultIfNull(checkoutRow.getMessage().get(storeId), "");
                }
                orderData.setOrderMessage(userMessage); // 买家订单留言
                orderData.setOrderItemAmount(storeItemVo.getMoneyItemAmount()); // 商品总价格/商品金额, 不包含运费
                orderData.setOrderAdjustFee(BigDecimal.valueOf(0)); // 手工调整费用店铺优惠

                orderData.setPointsValueRate(pointsValueRate); // 记录积分价值比例
                BigDecimal orderPointsFee = storeItemVo.getPointsAmount().multiply(pointsValueRate).setScale(2, RoundingMode.HALF_UP);
                orderData.setOrderPointsFee(orderPointsFee); // 积分抵扣金额

                if (CheckUtil.isNotEmpty(salePersonDiscount)) {
                    orderData.setOrderSalePersonDiscount(salePersonDiscount);
                }

                // 积分抵扣费用 是否采用负数？
                orderData.setOrderDiscountAmount(storeItemVo.getDiscountAmount()); // 折扣价格/优惠总金额
                orderData.setOrderShippingFeeAmount(storeItemVo.getFreightAmount()); // 运费价格/运费金额
                orderData.setOrderShippingFee(storeItemVo.getFreightAmount()); // 实际运费金额-卖家可修改

                orderData.setOrderPlusDiscountAmount(orderPlusDiscountAmount); // 订单PLUS优惠金额
                orderData.setOrderLevelDiscountAmount(storeItemVo.getLevelDiscountAmount()); // 订单等级优惠金额
                orderData.setPlusShippingDiscount(storeItemVo.getPlusShippingDiscount()); // 订单PLUS运费优惠金额

                orderData.setVoucherId(storeItemVo.getUserVoucherId()); // 优惠券id/优惠券/返现:发放选择使用
                //orderData.setVoucher_number(voucher_code); // 优惠券编码
                orderData.setVoucherPrice(storeItemVo.getVoucherAmount()); // 优惠券面额

                orderData.setOrderResourceExt1(order_resource_ext1_use_current);
                /*
                BigDecimal store_rebate = Convert.toBigDecimal(storeItemVo.get("store_rebate"));


                // todo 红包
                orderData.setRedpacketId(0); // 红包id-平台优惠券
                orderData.setRedpacket_number("0"); // 红包编码
                orderData.setRedpacket_price(BigDecimal.ZERO); // 红包面额
                orderData.setOrder_redpacket_price(BigDecimal.ZERO); // 红包抵扣订单金额

                orderData.setOrder_resource_ext2(order_resource_ext2_use);
                orderData.setOrder_refund_status(0); // 退款状态:0-是无退款;1-是部分退款;2-是全部退款
                orderData.setOrder_return_status(0); // 退货状态(ENUM):0-是无退货;1-是部分退货;2-是全部退货
                orderData.setOrder_return_num(0); // 退货数量
                */

                BigDecimal orderCommissionFee = item_rows.stream().map(s -> s.getOrderItemCommissionFee()).reduce(BigDecimal::add).get();
                orderData.setOrderCommissionFee(orderCommissionFee); // 平台交易佣金

                /*
                orderData.setOrder_commission_fee_refund(BigDecimal.ZERO); // 平台交易佣金-退款
                orderData.setOrder_points_add(BigDecimal.ZERO); // 订单赠送积分
                orderData.setOrder_double_points_add(item_sum_points); // 多倍积分赠送
                orderData.setActivity_double_pointsId(activity_double_points_id);
                orderData.setOrder_bp_add(0); // 订单赠送积分
                orderData.setOrder_rebate(store_rebate);
                orderData.setOrder_promotion_info("0");
                orderData.setOrder_cancel_identity(1);
                orderData.setOrder_cancel_reason("");
                orderData.setOrder_redemption_price(dedu_price);// 订单提货券抵扣金额
                 */

                //如果有满返优惠券，先保存活动ID,付款后发放优惠券。
                //List<ActivityInfoVo> activityInfoList = storeItemVo.getActivitys().getManhui();
                if (CollUtil.isNotEmpty(storeItemVo.getManhuiActivityIds())) {
                    orderData.setActivityManhuiId(CollUtil.join(storeItemVo.getManhuiActivityIds(), ","));
                    orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_NO);
                } else {
                    orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_NO);
                }

                if (!orderDataRepository.save(orderData)) {
                    throw new BusinessException(__("保存订单数据失败!"));
                }

                if (CollUtil.isNotEmpty(checkoutRow.getInvoice())) {
                    Map<Integer, Integer> userInvoiceIds = checkoutRow.getInvoice();

                    if (userInvoiceIds.containsKey(storeId)) {
                        Integer userInvoiceId = userInvoiceIds.get(storeId);
                        UserInvoice userInvoice = userInvoiceService.get(userInvoiceId);

                        if (userInvoice != null) {

                            OrderInvoice orderInvoice = new OrderInvoice();

                            orderInvoice.setUserId(orderBase.getUserId());
                            orderInvoice.setStoreId(orderBase.getStoreId());
                            orderInvoice.setOrderId(orderId);


                            orderInvoice.setInvoiceTitle(userInvoice.getInvoiceTitle());
                            orderInvoice.setInvoiceCompanyCode(userInvoice.getInvoiceCompanyCode());
                            orderInvoice.setInvoiceIsCompany(userInvoice.getInvoiceIsCompany());
                            orderInvoice.setInvoiceAddress(userInvoice.getInvoiceAddress());
                            orderInvoice.setInvoicePhone(userInvoice.getInvoicePhone());
                            orderInvoice.setInvoiceBankname(userInvoice.getInvoiceBankname());
                            orderInvoice.setInvoiceBankaccount(userInvoice.getInvoiceBankaccount());
                            orderInvoice.setInvoiceContent(orderInfo.getOrderTitle());

                            orderInvoice.setInvoiceType(userInvoice.getInvoiceType());
                            orderInvoice.setInvoiceAmount(orderBase.getOrderPaymentAmount());
                            orderInvoice.setOrderIsPaid(false);
                            orderInvoice.setInvoiceTime(now.getTime());

                            orderInvoice.setInvoiceContactName(userInvoice.getInvoiceContactName());

                            if (!orderInvoiceRepository.save(orderInvoice)) {
                                throw new BusinessException(__("保存发票数据失败!"));
                            }
                        }
                    }
                }
            }

            orderIdRow.add(orderId);

            /*
            // 获取店铺主账号
            ShopStoreBase store_base_row = shopStoreBaseService.get((Integer) orderBase.get("storeId"));
            Integer seller_id = store_base_row.getUserId();
            invoicingCustomerBaseService.doStoreAddCustomer((Integer) orderBase.get("storeId"), (Integer) orderBase.get("buyer_user_id"));

            if (CheckUtil.isEmpty(seller_id)) {
                throw new BusinessException(String.format(__("商家信息有误！商家主账号Id: %s"), seller_id));
            }

             */


            //或者通过API
            ConsumeTrade consume_trade_row = new ConsumeTrade();

            Integer subsite_id = 0;

            consume_trade_row.setOrderId(orderId);
            consume_trade_row.setBuyerId(userId); // 买家编号

            //买家是否开店
            Integer buyer_store_id = 0;
            consume_trade_row.setBuyerStoreId(buyer_store_id);

            Integer sellerId = userAdminService.getSellerUserId(storeId);

            consume_trade_row.setSellerId(sellerId);
            consume_trade_row.setStoreId(storeId); // 卖家id
            consume_trade_row.setSubsiteId(subsite_id); // 所属分站
            consume_trade_row.setChainId(chain_id);
            //consume_trade_row.setOrder_stateId(orderInfo.getOrder_stateId()); // 订单状态
            consume_trade_row.setTradeIsPaid(StateCode.ORDER_PAID_STATE_NO); // 未支付
            consume_trade_row.setTradeTypeId(StateCode.TRADE_TYPE_SHOPPING); // 交易类型
            consume_trade_row.setPaymentChannelId(0); // 支付渠道

            consume_trade_row.setTradeModeId(1); // 交易类型:1-担保交易;  2-直接交易
            consume_trade_row.setCurrencyId(orderBase.getCurrencyId());
            consume_trade_row.setCurrencySymbolLeft(orderBase.getCurrencySymbolLeft());
            consume_trade_row.setOrderPaymentAmount(orderBase.getOrderPaymentAmount()); // 总付款额度: trade_payment_amount + trade_payment_money + trade_payment_recharge_card + trade_payment_points
            consume_trade_row.setOrderCommissionFee(orderData.getOrderCommissionFee()); // 平台交易佣金
            consume_trade_row.setTradePaymentAmount(orderBase.getOrderPaymentAmount()); // 实付金额:在线支付金额
            consume_trade_row.setTradePaymentMoney(BigDecimal.valueOf(0)); // 余额支付
            consume_trade_row.setTradePaymentRechargeCard(BigDecimal.valueOf(0)); // 充值卡余额支付
            consume_trade_row.setTradePaymentPoints(BigDecimal.valueOf(0)); // 积分支付
            consume_trade_row.setTradePaymentCredit(BigDecimal.valueOf(0)); // 信用支付
            consume_trade_row.setTradePaymentRedpack(BigDecimal.valueOf(0)); // 红包支付
            consume_trade_row.setTradeDiscount(orderData.getOrderDiscountAmount()); // 折扣优惠
            consume_trade_row.setTradeAmount(orderData.getOrderItemAmount()); // 总额虚拟:trade_order_amount + trade_discount

            consume_trade_row.setTradeTitle(orderInfo.getOrderTitle()); // 标题
            consume_trade_row.setTradeCreateTime(new Date().getTime());


            if (!consumeTradeRepository.save(consume_trade_row)) {
                throw new BusinessException(__("订单支付信息失败!"));
            } else {
                BigDecimal _order_payment_amount = orderBase.getOrderPaymentAmount();

                if (ObjectUtil.compare(_order_payment_amount, BigDecimal.ZERO) <= 0) {
                    // 订单付款状态处理，
                    // 不需要添加收款记录，直接修改订单状态
                    if (!setPaidYes(orderId)) {
                        throw new BusinessException(__("订单支付状态修改失败!"));
                    } else {
                        cartData.setIsPaid(true);
                    }
                }


                if (CollUtil.isNotEmpty(cartIds)) {
                    if (!userCartService.remove(cartIds)) {
                        throw new BusinessException("删除购物车失败");
                    }
                }
                // 添加订单事件
            }
        }

        cartData.setOrderMoneyAmount(orderSelMoneyAmount);
        cartData.setOrderPointsAmount(orderSelPointsAmount);
        cartData.setOrderSpAmount(orderSelSpAmount);


        OrderAddOutput orderAddOutput = BeanUtil.copyProperties(cartData, OrderAddOutput.class);
        orderAddOutput.setOrderIds(orderIdRow);
        orderAddOutput.setGbId(gbId);

        //代金券使用提醒
        CheckoutInput in = orderAddOutput.getIn();
        List<Integer> userVoucherIds = in.getUserVoucherIds();

        if (CollectionUtil.isNotEmpty(userVoucherIds)) {
            String message_id = "the-use-of-vouchers-to-remind";
            Map<String, Object> args = new HashMap<>();
            messageService.sendNoticeMsg(userId, message_id, args);
        }

        return orderAddOutput;
    }

    /**
     * 是否可以退货
     *
     * @param orderStateId 订单状态
     * @param orderIsPaid
     * @return boolean true-可，false-不可
     */
    private boolean ifReturn(Integer orderStateId, Integer orderIsPaid) {
        List<Integer> orderStates = Arrays.asList(StateCode.ORDER_STATE_SHIPPED, StateCode.ORDER_STATE_RECEIVED, StateCode.ORDER_STATE_FINISH);
        return orderStates.contains(orderStateId) && ObjectUtil.equal(orderIsPaid, StateCode.ORDER_PAID_STATE_YES);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param orderStateNote
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean cancel(String orderId, String orderStateNote) {
        boolean flag = false;

        OrderBase orderBase = orderBaseRepository.get(orderId);
        if (orderBase == null) {
            return flag;
        }

        OrderInfo orderInfo = orderInfoRepository.get(orderId);

        if (orderInfo == null) {
            throw new BusinessException(__("订单信息为空！"));
        }

        QueryWrapper<OrderItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("order_id", orderId);
        List<OrderItem> orderItemRows = orderItemRepository.find(itemQueryWrapper);

        if (CollectionUtil.isEmpty(orderItemRows)) {
            throw new BusinessException(__("订单商品集合为空！"));
        }

        String activityTypeId = orderInfo.getActivityTypeId();

        if (StrUtil.isNotEmpty(activityTypeId)) {
            List<Integer> activityTypeIds = Convert.toList(Integer.class, activityTypeId);

            if (CollectionUtil.isNotEmpty(activityTypeIds)) {

                if (activityTypeIds.contains(StateCode.ACTIVITY_TYPE_MANHUI)) {
                    List<Long> orderItemIds = CommonUtil.column(orderItemRows, OrderItem::getOrderItemId);

                    //满返活动 优惠券判断
                    QueryWrapper<UserVoucher> userVoucherQueryWrapper = new QueryWrapper<>();
                    userVoucherQueryWrapper.in("src_order_item_id", orderItemIds);
                    List<UserVoucher> userVoucherList = userVoucherRepository.find(userVoucherQueryWrapper);

                    if (CollectionUtil.isNotEmpty(userVoucherList)) {
                        //收回优惠券
                        userVoucherService.withdrawVoucher(orderItemIds);
                    }
                }
            }
        }

        //拼团支付，不可取消
        //if (checkPaidFlag && !ifCancel(orderInfo.getOrderStateId(), orderInfo.getOrderIsPaid())) {
        if (!ifCancel(orderInfo.getOrderStateId(), orderInfo.getOrderIsPaid())) {
            throw new BusinessException(__("无符合取消条件的订单"));
        }

        if (orderBase.getOrderStateId().intValue() != StateCode.ORDER_STATE_CANCEL) {
            flag = editNextState(orderId, orderBase.getOrderStateId(), StateCode.ORDER_STATE_CANCEL, orderStateNote);
            if (!flag) {
                return flag;
            }
        } else {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        //判断是否出库，出库会释放冻结库存，取消出库后的订单，不释放冻结库存
        List<Integer> orderStates = Arrays.asList(StateCode.ORDER_STATE_WAIT_PAY, StateCode.ORDER_STATE_WAIT_REVIEW, StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW, StateCode.ORDER_STATE_PICKING);
        if (orderStates.contains(orderInfo.getOrderStateId())) {
            //部分出库商品数量
            List<StockBillItem> billItems = stockBillItemRepository.find(new QueryWrapper<StockBillItem>().eq("order_id", orderId));
            Map<Long, Integer> billItemQuantityAll = new HashMap<>();

            for (StockBillItem billItem : billItems) {
                if (billItem.getOrderItemId() != null) {
                    Integer quantity = billItemQuantityAll.get(billItem.getItemId());

                    if (quantity != null) {
                        quantity = quantity + billItem.getBillItemQuantity();
                    } else {
                        quantity = billItem.getBillItemQuantity();
                    }

                    billItemQuantityAll.put(billItem.getItemId(), quantity);
                }
            }

            //释放冻结库存
            for (OrderItem orderItem : orderItemRows) {
                // start 释放冻结库存
                Integer orderItemInventoryLock = orderItem.getOrderItemInventoryLock();
                if (ObjectUtil.equal(orderItemInventoryLock, 1001) || (StateCode.ORDER_PAID_STATE_YES == orderInfo.getOrderIsPaid().intValue() && ObjectUtil.equal(orderItemInventoryLock, 1002))) {
                    Integer releaseQuantity = orderItem.getOrderItemQuantity();
                    Integer quantity = billItemQuantityAll.get(orderItem.getItemId());

                    //去掉部分出库商品数量
                    if (quantity != null) {
                        releaseQuantity = releaseQuantity - quantity;

                        //出库未发货，可以注释掉后， 商品数量需要手工入库。
                        ProductEditStockInput input = new ProductEditStockInput();
                        input.setItemId(orderItem.getItemId());
                        input.setItemQuantity(quantity);
                        input.setBillTypeId(StateCode.BILL_TYPE_IN);
                        boolean success = productItemService.batchEditStock(Collections.singletonList(input));
                    }

                    //严格应该是影响行数 <= 0  报错
                    if (releaseQuantity > 0 && productItemRepository.releaseSkuStock(orderItem.getItemId(), releaseQuantity) < 0) {
                        throw new BusinessException(String.format(__("释放: %s 冻结库存失败!"), orderItem.getItemId()));
                    }
                }
            }
        }

        if (!Objects.equals(orderInfo.getOrderIsShipped(), StateCode.ORDER_SHIPPED_STATE_NO)) {
            //部分发货
            throw new BusinessException(__("订单部分发货，不可取消，请联系商家！"));
        }

        //todo 判断是否需要退款
        if (!Objects.equals(orderInfo.getOrderIsPaid(), StateCode.ORDER_PAID_STATE_NO)) {
            OrderReturnInput orderReturnInput = new OrderReturnInput();
            orderReturnInput.setReturnAllFlag(true);
            orderReturnInput.setOrderId(orderId);
            orderReturnInput.setReturnBuyerMessage(orderStateNote);

            orderReturnInput.setUserId(orderBase.getUserId());
            orderReturnInput.setReturnAllFlag(false);
            orderReturnInput.setReturnFlag(StateCode.ORDER_NOT_NEED_RETURN_GOODS);
            orderReturnInput.setReviewFlag(true);

            for (OrderItem orderItem : orderItemRows) {
                OrderReturnItemInputVo returnItemInputVo = new OrderReturnItemInputVo();
                returnItemInputVo.setOrderItemId(orderItem.getOrderItemId());

                //应付金额，去掉代金券额度, 满减。
                BigDecimal orderItemCanRefundAmount = orderItem.getOrderItemCanRefundAmount();
                returnItemInputVo.setReturnRefundAmount(NumberUtil.sub(orderItemCanRefundAmount, orderItem.getOrderItemReturnSubtotal()));
                returnItemInputVo.setReturnItemNum(orderItem.getOrderItemQuantity() - orderItem.getOrderItemReturnNum());

                orderReturnInput.getReturnItems().add(returnItemInputVo);
            }

            String retrunId = orderReturnService.addItem(orderReturnInput);
        }

        // 订单取消优惠券退还
        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setOrderId("");
        userVoucher.setVoucherStateId(StateCode.VOUCHER_STATE_UNUSED);
        userVoucherRepository.edit(userVoucher, new QueryWrapper<UserVoucher>().eq("order_id", orderId));

        //取消拼团, 未支付的取消活动数据， 如果支付，则必须通过计划任务取消
        cancelActivity(orderId);
        if (Objects.equals(orderInfo.getOrderIsPaid(), StateCode.ORDER_PAID_STATE_NO)) {

        }

        //取消发票
        orderInvoiceRepository.remove(new QueryWrapper<OrderInvoice>().eq("order_id", orderId).eq("invoice_status", 0));

        return flag;
    }

    /**
     * 支付完成
     *
     * @param orderId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean setPaidYes(String orderId) {
        boolean flag = false;
        OrderInfo orderInfo = orderInfoRepository.get(orderId);
        OrderBase orderBase = orderBaseRepository.get(orderId);
        OrderData orderData = orderDataRepository.get(orderId);

        if (ObjectUtil.isEmpty(orderBase)) {
            throw new BusinessException(String.format(__("订单基础 %s 不存在!"), orderId));
        }

        if (ObjectUtil.isEmpty(orderInfo)) {
            throw new BusinessException(String.format(__("订单信息 %s 不存在!"), orderId));
        }

        if (ObjectUtil.isEmpty(orderData)) {
            throw new BusinessException(String.format(__("订单信息 %s 不存在!"), orderId));
        }

        if (orderInfo.getOrderStateId() == StateCode.ORDER_STATE_WAIT_PAY && orderInfo.getOrderIsPaid() != StateCode.ORDER_PAID_STATE_YES) {
            //库存是否足够
            List<OrderItem> orderItemList = orderItemRepository.find(new QueryWrapper<OrderItem>().eq("order_id", orderId));
            for (OrderItem item : orderItemList) {
                // start 判断增加冻结库存
                if (ObjectUtil.equal(item.getOrderItemInventoryLock(), 1002)) {
                    if (productItemRepository.lockSkuStock(item.getItemId(), item.getOrderItemQuantity()) <= 0) {
                        String format = String.format(__("更改: %s 冻结库存失败!"), item.getItemId());
                        logger.error(format);
                        //不报错，允许执行，日志记录
                        //throw new BusinessException(String.format(__("更改: %s 冻结库存失败!"), item.getItemId()));

                        //库存不足，走自动退款流程
                        cancel(orderId, String.format(__("%s 库存不足，取消订单!"), item.getItemId()));

                        return false;
                    }
                }
                // end
            }

            //获取订单的下一条状态
            Integer nextOrderStateId = null;
            Integer kindId = orderInfo.getKindId();

            if (Objects.equals(kindId, StateCode.PRODUCT_KIND_EDU)) {
                nextOrderStateId = StateCode.ORDER_STATE_FINISH;
            } else {
                nextOrderStateId = getNextOrderStateId(orderInfo.getOrderStateId());
            }

            flag = editNextState(orderId, orderInfo.getOrderStateId(), nextOrderStateId, "");

            if (!flag) {
                return false;
            }

            //更新支付状态
            OrderInfo infoDate = new OrderInfo();
            infoDate.setOrderId(orderId);
            infoDate.setOrderIsPaid(StateCode.ORDER_PAID_STATE_YES);

            Integer orderIsSync = configBaseService.getOrderIsSync();
            infoDate.setOrderIsSync(orderIsSync);

            flag = orderInfoRepository.edit(infoDate);

            if (StrUtil.isNotEmpty(orderData.getActivityManhuiId())) {
                orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_TODO);
            }

            if (!orderDataRepository.edit(orderData)) {
                throw new BusinessException("修改订单详细信息失败！");
            }
            //更新发票订单支付状态
            QueryWrapper<OrderInvoice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);

            OrderInvoice orderInvoice = new OrderInvoice();
            orderInvoice.setOrderIsPaid(true);

            orderInvoiceRepository.edit(orderInvoice, queryWrapper);

            // 读取订单商品，更新销量
            for (OrderItem orderItem : orderItemList) {
                Long productId = orderItem.getProductId();
                Integer orderItemQuantity = orderItem.getOrderItemQuantity();
                ProductIndex productIndex = productIndexRepository.get(productId);
                productIndex.setProductSaleNum(orderItemQuantity + productIndex.getProductSaleNum());
                productIndexRepository.edit(productIndex);
            }


            //用户累计经验值用户升级
            BigDecimal expAmount = NumberUtil.mul(orderBase.getOrderPaymentAmount(), configBaseService.getConfig("exp_consume_rate", BigDecimal.ZERO));
            BigDecimal expMax = configBaseService.getConfig("exp_consume_max", BigDecimal.ZERO);
            BigDecimal exp = NumberUtil.min(expAmount, expMax);

            if (exp.compareTo(BigDecimal.ZERO) > 0) {
                ExperienceVo experienceVo = new ExperienceVo();
                experienceVo.setUserId(orderBase.getUserId());
                experienceVo.setExp(exp);
                experienceVo.setExpTypeId(LevelCode.EXP_TYPE_CONSUME);
                experienceVo.setDesc(__("购买商品获得经验"));
                UserResourceService.experience(experienceVo);
            }
            //用户累计金额用户升级
            UserAnalytics userAnalyticsRow = UserAnalyticsRepository.get(orderBase.getUserId());
            BigDecimal userSpend = orderBase.getOrderPaymentAmount();
            BigDecimal userRefund = BigDecimal.ZERO;
            Integer userOrderBuyNum = 1;
            Integer userOrderReturnNum = 0;
            Integer userProductBuyNum = 0;
            Integer userProductReturnNum = 0;

            if (ObjectUtil.isNotEmpty(userAnalyticsRow)) {
                userSpend = NumberUtil.add(userSpend, userAnalyticsRow.getUserSpend());
                userRefund = NumberUtil.add(userRefund, userAnalyticsRow.getUserRefund());
                userOrderBuyNum = userOrderBuyNum + userAnalyticsRow.getUserOrderBuyNum();
                userOrderReturnNum = userOrderReturnNum + userAnalyticsRow.getUserOrderReturnNum();
                userProductBuyNum = userProductBuyNum + userAnalyticsRow.getUserProductBuyNum();
                userProductReturnNum = userProductReturnNum + userAnalyticsRow.getUserProductReturnNum();
            }

            UserAnalytics userAnalytics = new UserAnalytics();
            userAnalytics.setUserId(orderBase.getUserId());
            userAnalytics.setUserSpend(userSpend);
            userAnalytics.setUserRefund(userRefund);
            userAnalytics.setUserOrderBuyNum(userOrderBuyNum);
            userAnalytics.setUserOrderReturnNum(userOrderReturnNum);
            userAnalytics.setUserProductBuyNum(userProductBuyNum);
            userAnalytics.setUserProductReturnNum(userProductReturnNum);

            UserAnalyticsRepository.save(userAnalytics);

            UserResourceService.checkUpdateUserLevel(orderBase.getUserId(), 0, userSpend);

            // 付款成功，对用户进行提醒
            String messageId = "payment-success-reminding";
            Map<String, Object> args = new HashMap<>();
            args.put("order_id", orderId);
            args.put("product_name", orderInfo.getOrderTitle());
            args.put("order_payment_amount", orderBase.getOrderPaymentAmount());
            args.put("order_add_time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            messageService.sendNoticeMsg(orderBase.getUserId(), messageId, args);

            // 提醒商家发货
            Integer sellerId = userAdminService.getNoticeUserId(orderBase.getStoreId());

            messageId = "notice-of-delivery";
            args = new HashMap<>();
            args.put("order_id", orderId);
            args.put("date", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

            messageService.sendNoticeMsg(sellerId, messageId, args);

        } else {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        // Todo: 处理支付成功逻辑

        return flag;
    }

    /**
     * 审核订单
     *
     * @param orderId
     * @param orderStateNote
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean review(String orderId, String orderStateNote) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderBase orderBase = orderBaseRepository.get(orderId);

        if (orderBase == null) {
            throw new BusinessException("该订单信息不存在！");
        }

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
            //判断活动前置条件
            ifActivity(orderId);

            if (orderBase.getOrderStateId().intValue() == StateCode.ORDER_STATE_WAIT_REVIEW) {
                //获取订单的下一条状态
                Integer nextOrderStateId = getNextOrderStateId(orderBase.getOrderStateId());

                Boolean res = editNextState(orderId, orderBase.getOrderStateId(), nextOrderStateId, orderStateNote);

                return res;
            } else {
                throw new BusinessException("未更改到符合条件的订单！");
            }
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    /**
     * 财务审核
     *
     * @param orderId
     * @param orderStateNote
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean finance(String orderId, String orderStateNote) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderBase orderBase = orderBaseRepository.get(orderId);

        if (orderBase == null) {
            throw new BusinessException("该订单信息不存在！");
        }

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
            //判断活动前置条件
            ifActivity(orderId);

            if (orderBase.getOrderStateId().intValue() == StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW) {
                //获取订单的下一条状态
                int nextOrderStateId = getNextOrderStateId(orderBase.getOrderStateId());

                return editNextState(orderId, orderBase.getOrderStateId(), nextOrderStateId, orderStateNote);
            } else {
                throw new BusinessException("未更改到符合条件的订单！");
            }
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    /**
     * 出库审核
     *
     * @param in
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean picking(OrderPickingInput in) {
        //判断活动前置条件
        ifActivity(in.getOrderId());
        checkOrderReturnWaiting(in.getOrderId());

        if (CollectionUtil.isNotEmpty(in.getItems())) {
            in.setPickingFlag(false);
        }

        OrderBase orderBase = orderBaseRepository.get(in.getOrderId());

        if (orderBase == null) {
            return false;
        }

        if (orderBase.getOrderStateId().intValue() == StateCode.ORDER_STATE_PICKING || orderBase.getOrderStateId().intValue() == StateCode.ORDER_STATE_WAIT_SHIPPING) {

            Integer state = doReviewPicking(in);

            if (CheckUtil.isEmpty(state)) {
                return false;
            }

            if (state.intValue() == StateCode.ORDER_PICKING_STATE_YES && orderBase.getOrderStateId().intValue() == StateCode.ORDER_STATE_PICKING) {
                //获取订单的下一条状态
                Integer nextOrderStateId = getNextOrderStateId(orderBase.getOrderStateId());

                return editNextState(in.getOrderId(), orderBase.getOrderStateId(), nextOrderStateId, "");
            } else {

            }
        } else {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return true;
    }

    /**
     * 判断是否有待审核售后订单条件限制
     *
     * @param orderId
     * @return
     */
    public Boolean checkOrderReturnWaiting(String orderId) {
        List<Serializable> listKey = orderReturnRepository.findKey(new QueryWrapper<OrderReturn>().eq("order_id", orderId).eq("return_state_id", StateCode.RETURN_PROCESS_CHECK));

        if (CollUtil.isNotEmpty(listKey)) {
            throw new BusinessException(String.format(__("有待处理的退款或者退货单: %s，请先处理！"), CollUtil.join(listKey, ",")));
        }

        return true;
    }

    /**
     * 是否可以发货
     *
     * @param orderStateId
     * @return
     */
    private boolean ifShipping(Integer orderStateId) {
        return orderStateId.intValue() == StateCode.ORDER_STATE_WAIT_SHIPPING || orderStateId.intValue() == StateCode.ORDER_STATE_PICKING;
    }

    /**
     * 发货
     *
     * @param in
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean shipping(OrderShippingInput in) {
        //判断活动前置条件
        ifActivity(in.getOrderId());
        checkOrderReturnWaiting(in.getOrderId());

        String orderId = in.getOrderId();
        OrderBase orderBase = orderBaseRepository.get(orderId);

        if (orderBase == null) {
            return false;
        }

        if (ifShipping(orderBase.getOrderStateId())) {
            Integer state = doReviewShipping(in);

            if (CheckUtil.isEmpty(state)) {
                return false;
            }

            if (state.intValue() == StateCode.ORDER_SHIPPED_STATE_YES) {
                //获取订单的下一条状态
                Integer nextOrderStateId = getNextOrderStateId(orderBase.getOrderStateId());

                //当前状态可能为待配货，下一个状态为代发货，则直接更改为已发货。
                if (Objects.equals(nextOrderStateId, StateCode.ORDER_STATE_WAIT_SHIPPING)) {
                    nextOrderStateId = StateCode.ORDER_STATE_SHIPPED;
                }

                return editNextState(orderId, orderBase.getOrderStateId(), nextOrderStateId, "");
            } else {
            }
        } else {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        return true;
    }

    /**
     * 检测是否发货完成
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean checkShippingComplete(String orderId) {
        OrderInfo orderInfo = orderInfoRepository.get(orderId);

        if (ObjectUtil.isEmpty(orderInfo)) {
            throw new BusinessException(String.format("订单 %s 不存在！", orderId));
        }

        // 检测是否发货完成
        boolean isComplete = true;

        // 物流记录
        List<OrderLogistics> orderLogistics = orderLogisticsRepository.find(new QueryWrapper<OrderLogistics>().eq("order_id", orderId));

        // StockBill
        List<StockBill> stockBills = stockBillRepository.find(new QueryWrapper<StockBill>().eq("order_id", orderId));

        List<String> ids = CommonUtil.column(orderLogistics, OrderLogistics::getStockBillId);

        for (StockBill bill : stockBills) {
            if (!ids.contains(bill.getStockBillId())) {
                // 完成发货信息
                isComplete = false;
                break;
            }
        }

        if (isComplete) {
            // 判断商品是否全部出库

            // 订单商品
            List<OrderItem> orderItems = orderItemRepository.find(new QueryWrapper<OrderItem>().eq("order_id", orderId));

            // 已出库商品
            List<StockBillItem> billItems = stockBillItemRepository.find(new QueryWrapper<StockBillItem>().eq("order_id", orderId));

            Map<Long, PickingItem> billItemQuantityAll = new HashMap<>();

            for (OrderItem orderItem : orderItems) {
                //todo 扣除同意退货数量
                billItemQuantityAll.put(orderItem.getOrderItemId(), new PickingItem(orderItem.getItemId(), orderItem.getOrderItemId(), orderItem.getOrderItemQuantity() - orderItem.getOrderItemReturnAgreeNum(), orderItem.getOrderItemSalePrice(), orderItem.getProductId()));
            }

            for (StockBillItem billItem : billItems) {
                if (billItem.getOrderItemId() != null) {
                    PickingItem pickingItem = billItemQuantityAll.get(billItem.getOrderItemId());
                    if (pickingItem != null) {
                        pickingItem.setBillItemQuantity(pickingItem.getBillItemQuantity() - billItem.getBillItemQuantity());
                    } else {
                        throw new BusinessException(String.format("出库数据有误 '%s'", billItem.getOrderItemId()));
                    }
                }
            }

            if (!billItemQuantityAll.isEmpty()) {
                for (PickingItem pickingItem : billItemQuantityAll.values()) {
                    if (pickingItem.getBillItemQuantity() > 0) {
                        isComplete = false;
                        break;
                    }
                }

                if (isComplete) {
                    int state = StateCode.ORDER_SHIPPED_STATE_YES;

                    if (orderInfo.getOrderIsShipped().intValue() != state) {
                        orderInfo.setOrderIsShipped(state);
                        orderInfoRepository.edit(orderInfo);

                        //获取订单的下一条状态
                        int nextOrderStateId = getNextOrderStateId(orderInfo.getOrderStateId());
                        editNextState(orderId, orderInfo.getOrderStateId(), nextOrderStateId, "");
                    }
                }
            }
        }

        return isComplete;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addLogistics(OrderLogistics in) {
        Boolean flag = saveLogistics(in);

        Boolean aBoolean = checkShippingComplete(in.getOrderId());

        if (!aBoolean) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderIsShipped(StateCode.ORDER_SHIPPED_STATE_PART);

            QueryWrapper<OrderInfo> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.eq("order_id", in.getOrderId());
            infoQueryWrapper.in("order_state_id", StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING);
            orderInfoRepository.edit(orderInfo, infoQueryWrapper);
        }

        return flag;
    }

    @Override
    public Boolean saveLogistics(OrderLogistics in) {
        Integer logisticsId = in.getLogisticsId();
        Integer ssId = in.getSsId();

        StoreExpressLogistics storeExpressLogistics = storeExpressLogisticsRepository.get(logisticsId);

        if (storeExpressLogistics == null) {
            throw new BusinessException("物流信息不存在！");
        }

        StoreShippingAddress storeShippingAddress = storeShippingAddressRepository.get(ssId);

        if (storeShippingAddress == null) {
            throw new BusinessException("发货地址信息不存在！");
        }

        in.setExpressName(storeExpressLogistics.getExpressName());
        in.setExpressId(storeExpressLogistics.getExpressId());

        in.setLogisticsPhone(storeShippingAddress.getSsIntl() + storeShippingAddress.getSsMobile());
        in.setLogisticsMobile(storeShippingAddress.getSsIntl() + storeShippingAddress.getSsMobile());
        in.setLogisticsContacter(storeShippingAddress.getSsContacter());
        in.setLogisticsAddress(storeShippingAddress.getSsAddress());
        in.setLogisticsPostcode(storeShippingAddress.getSsPostalcode());

        Boolean flag = orderLogisticsRepository.save(in);

        return flag;
    }

    /**
     * 确认收货
     *
     * @param orderId
     * @param orderStateNote
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean receive(String orderId, String orderStateNote) {
        OrderBase orderBase = orderBaseRepository.get(orderId);

        if (orderBase == null) {
            return false;
        }

        if (orderBase.getOrderStateId().intValue() == StateCode.ORDER_STATE_SHIPPED) {
            //获取订单的下一条状态
            int nextOrderStateId = getNextOrderStateId(orderBase.getOrderStateId());
            Boolean res = editNextState(orderId, orderBase.getOrderStateId(), nextOrderStateId, orderStateNote);

            if (res) {
                // 同步店铺销量：确认收货后累计订单商品数量到 store_analytics.store_sales_num
                if (CheckUtil.isNotEmpty(orderBase.getStoreId()) && orderBase.getStoreId() > 0) {
                    List<OrderItem> orderItems = orderItemRepository.find(new QueryWrapper<OrderItem>().eq("order_id", orderId));
                    int soldNum = orderItems.stream()
                            .filter(Objects::nonNull)
                            .mapToInt(item -> Convert.toInt(item.getOrderItemQuantity()))
                            .sum();

                    if (soldNum > 0) {
                        StoreAnalytics storeAnalytics = storeAnalyticsRepository.get(orderBase.getStoreId());
                        if (storeAnalytics != null) {
                            storeAnalytics.setStoreSalesNum(Convert.toInt(storeAnalytics.getStoreSalesNum()) + soldNum);
                            if (!storeAnalyticsRepository.edit(storeAnalytics)) {
                                throw new BusinessException(__("店铺销量更新失败！"));
                            }
                        } else {
                            storeAnalytics = new StoreAnalytics();
                            storeAnalytics.setStoreId(orderBase.getStoreId());
                            storeAnalytics.setStoreSalesNum(soldNum);
                            if (!storeAnalyticsRepository.add(storeAnalytics)) {
                                throw new BusinessException(__("店铺销量更新失败！"));
                            }
                        }
                    }
                }
            }

            return res;
        } else {
            throw new BusinessException("未更改到符合条件的订单！");
        }
    }

    // getNextOrderStateId 读取启用配置，根据当前orderStateId获得下一状态 sc_order_process
    private Integer getNextOrderStateId(Integer orderStateId) {
        return configBaseService.getNextOrderStateId(orderStateId);
    }

    /**
     * 添加订单日志
     *
     * @param log
     * @return
     */
    private boolean addOrderStateLog(OrderStateLog log) {
        return orderStateLogRepository.add(log);
    }


    /**
     * 修改订单为下一个待处理状态
     *
     * @param orderId
     * @param orderStateId
     * @param nextOrderStateId
     * @param orderStateNote
     * @return
     */
    @Override
    public Boolean editNextState(String orderId, Integer orderStateId, Integer nextOrderStateId, String orderStateNote) {
        //下一个状态存在
        if (nextOrderStateId.intValue() != StateCode.ORDER_STATE_CANCEL) {
            List<Integer> stateIdList = configBaseService.initOrderProcess();

            if (!stateIdList.contains(nextOrderStateId)) {
                throw new BusinessException("订单下个状态不符合配置要求！");
            }
        }

        //必须更新到记录
        OrderBase orderBase = new OrderBase();
        orderBase.setOrderId(orderId);
        orderBase.setOrderStateId(nextOrderStateId);
        boolean result = orderBaseRepository.edit(orderBase, new QueryWrapper<OrderBase>().eq("order_id", orderId).eq("order_state_id", orderStateId));

        if (!result) {
            throw new BusinessException("未更改到符合条件的订单！");
        }

        //订单信息更改
        OrderInfo oldInfo = new OrderInfo();
        oldInfo.setOrderId(orderId);
        oldInfo.setOrderStateId(orderStateId);

        OrderInfo newInfo = orderInfoRepository.get(orderId);
        newInfo.setOrderStateId(nextOrderStateId);

        if (nextOrderStateId.intValue() != StateCode.ORDER_STATE_CANCEL) {
            switch (orderStateId) {
                case StateCode.ORDER_STATE_WAIT_PAY:
                    //newInfo.OrderIsPaid = true //放入支付回调更改
                    break;
                case StateCode.ORDER_STATE_WAIT_REVIEW:
                    oldInfo.setOrderIsReview(0);
                    newInfo.setOrderIsReview(1);
                    break;
                case StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW:
                    oldInfo.setOrderFinanceReview(false);
                    newInfo.setOrderFinanceReview(true);
                    break;
                case StateCode.ORDER_STATE_PICKING:
                    newInfo.setOrderIsOut(StateCode.ORDER_PICKING_STATE_YES);
                    break;
                case StateCode.ORDER_STATE_WAIT_SHIPPING:
                    newInfo.setOrderIsShipped(StateCode.ORDER_SHIPPED_STATE_YES);
                    break;
                case StateCode.ORDER_STATE_SHIPPED:
                    newInfo.setOrderIsReceived(true);
                    newInfo.setOrderReceivedTime(new Date());
                    break;
                default:
                    break;
            }
        } else {
            //订单取消,如果订单同步过，则继续同步。
            if (newInfo.getOrderIsSync().intValue() == 1) {
                newInfo.setOrderIsSync(0);
            }
        }

        newInfo.setUpdateTime(new Date().getTime());

        //必须更新到记录
        result = orderInfoRepository.edit(newInfo);
        if (!result) {
            throw new BusinessException("未更改到符合条件的订单！");
        }


        Integer userId = 0;
        String userAccount = "";
        ContextUser user = ContextUtil.getLoginUser();

        if (ObjectUtil.isNotEmpty(user)) {
            userId = user.getUserId();
            userAccount = user.getUserAccount();
        }

        OrderStateLog log = new OrderStateLog();
        log.setOrderId(orderId);
        log.setOrderStateId(nextOrderStateId);
        log.setOrderStatePreId(orderStateId);
        log.setUserId(userId);
        log.setUserAccount(userAccount);
        log.setOrderStateNote(orderStateNote);
        log.setOrderStateTime(new Date());

        result = addOrderStateLog(log);

        return result;
    }


    /**
     * 出库审核 - 逻辑封装 - 涉及进销存
     *
     * @param in
     * @return
     */
    @Override
    public Integer doReviewPicking(OrderPickingInput in) {
        //清理数据
        if (CollUtil.isNotEmpty(in.getItems())) {
            Iterator<PickingItem> iterator = in.getItems().iterator();
            while (iterator.hasNext()) {
                PickingItem pickingItem = iterator.next();

                if (pickingItem.getBillItemQuantity() <= 0) {
                    iterator.remove();
                }
            }
        }

        int state = 0;

        //订单商品
        List<OrderItem> orderItems = orderItemRepository.find(new QueryWrapper<OrderItem>().eq("order_id", in.getOrderId()));
        if (orderItems == null) {
            return state;
        }

        List<Long> itemIds = CommonUtil.column(orderItems, OrderItem::getItemId);
        List<ProductItem> productItems = productItemRepository.gets(itemIds);

        if (CollectionUtil.isEmpty(productItems)) {
            throw new BusinessException("商品SKU不存在！");
        }
        Map<Long, Integer> itemQuantityMap = productItems.stream().collect(Collectors.toMap(ProductItem::getItemId, ProductItem::getItemQuantity, (k1, k2) -> k1));

        //已出库商品
        List<StockBillItem> billItems = stockBillItemRepository.find(new QueryWrapper<StockBillItem>().eq("order_id", in.getOrderId()));


        //已同意退货商品

        //差量商品
        Map<Long, PickingItem> billItemQuantityAll = new HashMap<>();
        Map<Long, PickingItem> billItemWaiting = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            //todo 扣除同意退货数量
            billItemQuantityAll.put(orderItem.getOrderItemId(), new PickingItem(orderItem.getItemId(), orderItem.getOrderItemId(), orderItem.getOrderItemQuantity() - orderItem.getOrderItemReturnAgreeNum(), orderItem.getOrderItemSalePrice(), orderItem.getProductId()));
        }

        for (StockBillItem billItem : billItems) {
            if (billItem.getOrderItemId() != null) {
                PickingItem value = billItemQuantityAll.get(billItem.getOrderItemId());
                if (value != null) {
                    value.setBillItemQuantity(value.getBillItemQuantity() - billItem.getBillItemQuantity());
                } else {
                    throw new BusinessException(String.format("出库数据有误 '%s'", billItem.getOrderItemId()));
                }
            }
        }

        if (in.getPickingFlag()) {
            billItemWaiting.putAll(billItemQuantityAll);
        } else {
            //todo 指定的出库，需要判断是否符合billItemQuantityAll中的要求。
            for (PickingItem item : in.getItems()) {
                if (item.getBillItemQuantity() > 0) {
                    billItemWaiting.put(item.getOrderItemId(), new PickingItem(item.getItemId(), item.getOrderItemId(), item.getBillItemQuantity(), item.getBillItemPrice(), item.getProductId()));
                }
            }
        }

        if (billItemWaiting.isEmpty()) {
            throw new BusinessException(String.format("无待出库出库数据: %s", in.getOrderId()));
        }

        //商品库存

        //出库单
        Date now = new Date();
        ContextUser user = ContextUtil.getLoginUser();

        String stockBillId = numberSeqService.getNextSeqString("OUT");

        StockBill stockBill = new StockBill();
        stockBill.setStockBillId(stockBillId);
        stockBill.setStockBillChecked(true);
        stockBill.setStockBillDate(now);
        stockBill.setStockBillModifyTime(now);
        stockBill.setStockBillTime(now.getTime());
        stockBill.setBillTypeId(CheckUtil.isEmpty(in.getBillTypeId()) ? StateCode.STOCK_OUT_ALL : in.getBillTypeId());
        stockBill.setStockTransportTypeId(CheckUtil.isEmpty(in.getStockTransportTypeId()) ? StateCode.STOCK_OUT_SALE : in.getStockTransportTypeId());
        stockBill.setStoreId(0);
        stockBill.setWarehouseId(0);
        stockBill.setOrderId(in.getOrderId());
        stockBill.setStockBillRemark("");
        stockBill.setEmployeeId(user.getUserId());
        stockBill.setAdminId(user.getUserId());
        stockBill.setStockBillOtherMoney(BigDecimal.ZERO);
        stockBill.setStockBillAmount(BigDecimal.ZERO);  // 订单金额
        stockBill.setStockBillEnable(true);  // 是否有效(BOOL):1-有效; 0-无效
        stockBill.setStockBillSrcId(""); // 关联编号

        //单据金额
        BigDecimal stockBillAmount = BigDecimal.ZERO;

        for (PickingItem pickingItem : billItemWaiting.values()) {
            //单据商品小计
            BigDecimal billItemSubtotal = pickingItem.getBillItemPrice().multiply(BigDecimal.valueOf(pickingItem.getBillItemQuantity()));

            StockBillItem stockBillItem = new StockBillItem();
            stockBillItem.setStockBillId(stockBillId);
            stockBillItem.setOrderId(in.getOrderId());
            stockBillItem.setOrderItemId(pickingItem.getOrderItemId());
            Long itemId = pickingItem.getItemId();
            stockBillItem.setItemId(itemId);
            stockBillItem.setWarehouseItemQuantity(itemQuantityMap.get(itemId));
            stockBillItem.setBillItemQuantity(pickingItem.getBillItemQuantity());
            stockBillItem.setBillItemUnitPrice(pickingItem.getBillItemPrice());
            stockBillItem.setBillItemSubtotal(billItemSubtotal);
            stockBillItem.setProductId(pickingItem.getProductId());
            stockBillItem.setWarehouseId(stockBill.getWarehouseId());
            stockBillItem.setStockTransportTypeId(CheckUtil.isEmpty(in.getStockTransportTypeId()) ? StateCode.STOCK_OUT_SALE : in.getStockTransportTypeId());
            stockBillItem.setBillTypeId(CheckUtil.isEmpty(in.getBillTypeId()) ? StateCode.STOCK_OUT_ALL : in.getBillTypeId());
            stockBillItem.setStoreId(0);

            OrderItem orderItem = orderItemRepository.get(stockBillItem.getOrderItemId());

            if (orderItem != null) {
                stockBillItem.setProductName(orderItem.getProductName());
                stockBillItem.setItemName(orderItem.getItemName());
            }

            stockBillItemRepository.add(stockBillItem);

            // 释放冻结库存
            if (productItemRepository.pickingSkuStock(pickingItem.getItemId(), pickingItem.getBillItemQuantity()) <= 0) {
                throw new BusinessException(String.format(__("扣减: %s 库存失败!"), pickingItem.getItemId()));
            }

            //
            PickingItem quantityAllItem = billItemQuantityAll.get(pickingItem.getOrderItemId());
            if (quantityAllItem != null) {
                quantityAllItem.setBillItemQuantity(quantityAllItem.getBillItemQuantity() - pickingItem.getBillItemQuantity());
            }

            stockBillAmount = stockBillAmount.add(billItemSubtotal);
        }

        stockBill.setStockBillAmount(stockBillAmount);
        stockBillRepository.add(stockBill);

        //判断是否已经全部出库， 需要修改订单状态
        state = StateCode.ORDER_PICKING_STATE_YES;

        for (PickingItem pickingItem : billItemQuantityAll.values()) {
            if (pickingItem.getBillItemQuantity() > 0) {
                state = StateCode.ORDER_PICKING_STATE_PART;
                break;
            }
        }

        OrderInfo orderInfo = orderInfoRepository.get(in.getOrderId());
        orderInfo.setOrderIsOut(state);
        orderInfoRepository.edit(orderInfo);

        return state;
    }

    /**
     * 发货审核  - 涉及快递单号处理
     *
     * @param in
     * @return
     */
    @Override
    public Integer doReviewShipping(OrderShippingInput in) {
        int state = 0;

        //如果为出库状态
        OrderInfo orderInfo = orderInfoRepository.get(in.getOrderId());
        if (orderInfo.getOrderIsOut().intValue() != StateCode.ORDER_PICKING_STATE_YES) {
            OrderPickingInput pickingInput = new OrderPickingInput();
            pickingInput.setOrderId(in.getOrderId());
            pickingInput.setPickingFlag(true);
            doReviewPicking(pickingInput);
        }

        //发货
        //出库单无对应发货信息的，完成发货操作
        //物流记录
        List<OrderLogistics> orderLogistics = orderLogisticsRepository.find(new QueryWrapper<OrderLogistics>().eq("order_id", in.getOrderId()));

        if (orderLogistics == null) {
            return state;
        }

        //StockBill
        List<StockBill> stockBills = stockBillRepository.find(new QueryWrapper<StockBill>().eq("order_id", in.getOrderId()));

        if (CollectionUtil.isEmpty(stockBills)) {
            return state;
        }

        List<String> ids = CommonUtil.column(orderLogistics, OrderLogistics::getStockBillId);
        //List<String> ids = orderLogistics.stream().map(OrderLogistics::getStockBillId).collect(Collectors.toList());

        boolean electronEnable = configBaseService.getConfig("electron_enable", false);

        if (electronEnable) {
            Integer shippingType = in.getShippingType();

            if (shippingType != null && shippingType.equals(2)) {
                StoreExpressLogistics expressLogistics = storeExpressLogisticsRepository.get(in.getLogisticsId());

                if (expressLogistics == null) {
                    throw new BusinessException(__("物流不存在"));
                }
                ExpressBase expressBase = expressBaseRepository.get(expressLogistics.getExpressId());

                if (expressBase == null) {
                    throw new BusinessException(__("快递不存在"));
                }

                ElectronVo electronVo = new ElectronVo();
                electronVo.setOrderCode(in.getOrderId());
                electronVo.setShipperCode(expressBase.getExpressPinyin());
                electronVo.setPayType(1);

                String electronCustomerName = configBaseService.getConfig("electron_customer_name");
                String electronCustomerPwd = configBaseService.getConfig("electron_customer_pwd");
                String electronSendSite = configBaseService.getConfig("electron_send_site");
                String electronSendStaff = configBaseService.getConfig("electron_send_staff");
                String electronMonthCode = configBaseService.getConfig("electron_month_code");
                electronVo.setCustomerName(electronCustomerName); //如果您没有此账号，请联系合作的快递网点负责人申请
                electronVo.setCustomerPwd(electronCustomerPwd);
                electronVo.setSendSite(electronSendSite);
                electronVo.setSendStaff(electronSendStaff);
                electronVo.setMonthCode(electronMonthCode);

                electronVo.setExpType("1");

                Integer ssId = in.getSsId();
                StoreShippingAddress storeShippingAddress = storeShippingAddressRepository.get(ssId);

                if (storeShippingAddress == null) {
                    throw new BusinessException(__("发货地址不存在"));
                }

                SenderVo senderVo = new SenderVo();
                senderVo.setName(storeShippingAddress.getSsName());
                senderVo.setMobile(storeShippingAddress.getSsMobile());
                senderVo.setProvinceName(storeShippingAddress.getSsProvince());
                senderVo.setCityName(storeShippingAddress.getSsCity());
                senderVo.setExpAreaName(storeShippingAddress.getSsCounty());
                senderVo.setAddress(storeShippingAddress.getSsAddress());
                electronVo.setSender(senderVo);

                OrderDeliveryAddress orderDeliveryAddress = orderDeliveryAddressRepository.get(in.getOrderId());

                if (orderDeliveryAddress == null) {
                    throw new BusinessException(__("收货地址不存在"));
                }

                ReceiverVo receiverVo = new ReceiverVo();
                receiverVo.setName(orderDeliveryAddress.getDaName());
                receiverVo.setMobile(orderDeliveryAddress.getDaMobile());
                receiverVo.setProvinceName(orderDeliveryAddress.getDaProvince());
                receiverVo.setCityName(orderDeliveryAddress.getDaCity());
                receiverVo.setExpAreaName(orderDeliveryAddress.getDaCounty());
                receiverVo.setAddress(orderDeliveryAddress.getDaAddress());
                electronVo.setReceiver(receiverVo);

                ArrayList<CommodityVo> commodityVos = new ArrayList<>();
                CommodityVo commodityVo = new CommodityVo();
                commodityVo.setGoodsName("衣服");
                commodityVos.add(commodityVo);
                electronVo.setCommodity(commodityVos);

                electronVo.setQuantity(1);
                electronVo.setRemark(in.getLogisticsExplain());

                String electron = orderLogisticsService.getElectron(electronVo);

                in.setOrderTrackingNumber(electron);
            }
        }


        for (StockBill bill : stockBills) {
            if (!ids.contains(bill.getStockBillId())) {
                //完成发货信息
                OrderLogistics newLogistics = new OrderLogistics();
                newLogistics.setStockBillId(bill.getStockBillId());

                BeanUtils.copyProperties(in, newLogistics);

                Boolean flag = saveLogistics(newLogistics);
                //Boolean flag = orderLogisticsRepository.add(newLogistics);

                if (!flag) {
                    throw new BusinessException(__("发货信息错误"));
                }

                StoreExpressLogistics expressLogistics = storeExpressLogisticsRepository.get(in.getLogisticsId());

                // 发货通知
                String messageId = "order_complete_shipping";
                Map<String, Object> args = new HashMap<>();
                args.put("order_id", in.getOrderId());
                args.put("logistics_name", expressLogistics.getExpressName());
                args.put("order_tracking_number", newLogistics.getOrderTrackingNumber());
                messageService.sendNoticeMsg(orderInfo.getUserId(), messageId, args);
            }
        }

        state = StateCode.ORDER_SHIPPED_STATE_YES;
        boolean flag = orderInfoRepository.edit(new OrderInfo().setOrderId(in.getOrderId()).setOrderIsShipped(state));


        return state;
    }

    /**
     * 审核订单到某个状态
     *
     * @param orderId
     * @param toOrderStateId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean reviewToState(String orderId, Integer toOrderStateId) {
        checkOrderReturnWaiting(orderId);

        Boolean flag = true;

        try {
            if (toOrderStateId.intValue() == StateCode.ORDER_STATE_CANCEL) {
                return cancel(orderId, "");
            } else {
                int tryCount = 0;
                while (tryCount < 10) {
                    //读取订单
                    OrderBase orderBase = orderBaseRepository.get(orderId);

                    //订单已经为目标状态
                    if (orderBase.getOrderStateId().intValue() == toOrderStateId) {
                        return true;
                        //break;
                    }

                    //获取订单的下一条状态
                    int nextOrderStateId = getNextOrderStateId(orderBase.getOrderStateId());

                    if (nextOrderStateId == StateCode.ORDER_STATE_WAIT_SHIPPING) {
                        OrderPickingInput input = new OrderPickingInput().setOrderId(orderId).setPickingFlag(true);
                        flag = picking(input);
                    } else if (nextOrderStateId == StateCode.ORDER_STATE_SHIPPED) {
                        OrderShippingInput input = new OrderShippingInput().setOrderId(orderId);
                        flag = shipping(input);
                    } else {
                        flag = editNextState(orderId, orderBase.getOrderStateId(), nextOrderStateId, "");
                    }

                    tryCount++;
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelActivity(String orderId) {
        // 开源版已移除拼团等营销数据写回，保留方法供取消订单流程调用
    }

    /**
     * 订单是否可以取消
     *
     * @param orderStateId
     * @param orderIsPaid  支付后不给取消
     * @return
     */
    private boolean ifCancel(Integer orderStateId, Integer orderIsPaid) {
        List<Integer> orderStates = Arrays.asList(StateCode.ORDER_STATE_WAIT_PAY, StateCode.ORDER_STATE_WAIT_REVIEW, StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW, StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING);
        //return orderStates.contains(orderStateId) && ObjectUtil.equal(orderIsPaid, StateCode.ORDER_PAID_STATE_NO);
        return orderStates.contains(orderStateId);
    }


    /**
     * 根据用户id获取用户订单统计信息
     *
     * @param userId
     * @return
     */
    @Override
    public OrderNumOutput getOrderStatisticsInfo(Integer userId) {
        OrderNumOutput orderNumOutput = new OrderNumOutput();

        // 全部订单
        // 完成订单数
        CompletableFuture<Void> infoFuture1 = CompletableFuture.runAsync(() -> {
            //已完成
            OrderNumInput orderNumInput = new OrderNumInput();
            orderNumInput.setUserId(userId);
            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_FINISH);
            orderNumInput.setKindId(StateCode.PRODUCT_KIND_ENTITY);
            orderNumOutput.setFinNumEntity(getOrderNum(orderNumInput));

            orderNumInput.setKindId(StateCode.PRODUCT_KIND_FUWU);
            orderNumOutput.setFinNumV(getOrderNum(orderNumInput));

            // 取消订单数
            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_CANCEL);
            orderNumInput.setKindId(StateCode.PRODUCT_KIND_ENTITY);
            orderNumOutput.setCancelNumEntity(getOrderNum(orderNumInput));

            orderNumInput.setKindId(StateCode.PRODUCT_KIND_FUWU);
            orderNumOutput.setCancelNumV(getOrderNum(orderNumInput));
        }, executor);


        CompletableFuture<Void> infoFuture2 = CompletableFuture.runAsync(() -> {
            // 待发货货订单数
            OrderNumInput orderNumInput = new OrderNumInput();
            orderNumInput.setUserId(userId);
            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_PICKING);
            orderNumInput.setKindId(StateCode.PRODUCT_KIND_ENTITY);
            Long orderPickingNum = getOrderNum(orderNumInput);

            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_WAIT_SHIPPING);
            Long orderShippingNum = getOrderNum(orderNumInput);
            orderNumOutput.setWaitShippingNumEntity(orderPickingNum + orderShippingNum);


            orderNumInput.setKindId(StateCode.PRODUCT_KIND_FUWU);
            orderPickingNum = getOrderNum(orderNumInput);

            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_WAIT_SHIPPING);
            orderNumInput.setKindId(StateCode.PRODUCT_KIND_FUWU);
            orderShippingNum = getOrderNum(orderNumInput);
            orderNumOutput.setWaitShippingNumV(orderPickingNum + orderShippingNum);


            // 已发货货订单数
            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_SHIPPED);
            orderNumInput.setKindId(StateCode.PRODUCT_KIND_ENTITY);
            orderNumOutput.setShipNumEntity(getOrderNum(orderNumInput));

            orderNumInput.setKindId(StateCode.PRODUCT_KIND_FUWU);
            orderNumOutput.setShipNumV(getOrderNum(orderNumInput));

        }, executor);

        CompletableFuture<Void> infoFuture3 = CompletableFuture.runAsync(() -> {
            // 等待支付订单数
            OrderNumInput orderNumInput = new OrderNumInput();
            orderNumInput.setUserId(userId);
            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_WAIT_PAY);
            orderNumInput.setKindId(StateCode.PRODUCT_KIND_ENTITY);
            orderNumOutput.setWaitPayNumEntity(getOrderNum(orderNumInput));

            orderNumInput.setKindId(StateCode.PRODUCT_KIND_FUWU);
            orderNumOutput.setWaitPayNumV(getOrderNum(orderNumInput));

            QueryWrapper<OrderReturn> returnQueryWrapper = new QueryWrapper<>();
            returnQueryWrapper.in("return_state_id", StateCode.RETURN_PROCESS_SUBMIT, StateCode.RETURN_PROCESS_CHECK, StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND).eq("buyer_user_id", userId);

            Long returningNum = orderReturnRepository.count(returnQueryWrapper);
            orderNumOutput.setReturningNum(returningNum);
        }, executor);

        try {
            CompletableFuture.allOf(infoFuture1, infoFuture2, infoFuture3).get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return orderNumOutput;
    }

    /**
     * 订单数量
     */
    public Long getOrderNum(OrderNumInput in) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotEmpty(in.getOrderStateId())) {
            queryWrapper.eq("order_state_id", in.getOrderStateId());
        } else {

            if (CollectionUtil.isNotEmpty(in.getOrderStateIds())) {
                queryWrapper.in("order_state_id", in.getOrderStateIds());
            }
        }

        if (ObjectUtil.isNotEmpty(in.getUserId())) {
            queryWrapper.eq("user_id", in.getUserId());
        }

        if (ObjectUtil.isNotEmpty(in.getKindId())) {
            queryWrapper.eq("kind_id", in.getKindId());
        }

        if (ObjectUtil.isNotEmpty(in.getOrderStime())) {
            queryWrapper.ge("create_time", in.getOrderStime());
        }

        if (ObjectUtil.isNotEmpty(in.getOrderEtime())) {
            queryWrapper.le("create_time", in.getOrderEtime());
        }

        if (CheckUtil.isNotEmpty(in.getStoreId())) {
            queryWrapper.eq("store_id", in.getStoreId());
        }

        if (in.getOrderBuyerEvaluationStatus() != null) {
            queryWrapper.eq("order_buyer_evaluation_status", in.getOrderBuyerEvaluationStatus());
        }

        return orderInfoRepository.count(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean doUpdateOrders(List<String> orderIds) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isPlatform()) {
            if (CollectionUtil.isEmpty(orderIds)) {
                throw new BusinessException(__("订单编号为空！"));
            }
            List<OrderInfo> orderInfos = new ArrayList<>();
            for (String orderId : orderIds) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(orderId);
                orderInfo.setOrderWithdrawConfirm(true);
                orderInfos.add(orderInfo);
            }

            return orderInfoRepository.edit(orderInfos);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public void autoCancelOrder() {
        // 更新未付款订单，取消超时订单
        List<String> orderIds = orderInfoService.getAutoCancelOrderId();
        for (String orderId : orderIds) {
            cancel(orderId, "系统自动关单");

            //清理缓存
            configBaseService.cleanRequestCache();
        }
    }

    @Override
    @Transactional
    public void autoReceive() {
        // 更新为确认收货
        List<String> order_id_receipt = orderInfoService.getAutoFinishOrderId();
        if (CollUtil.isNotEmpty(order_id_receipt)) {
            for (String order_id : order_id_receipt) {
                receive(order_id, __("系统自动收货"));

                //清理缓存
                configBaseService.cleanRequestCache();
            }
        }
    }

    @Override
    public void returnHidden() {
        List<String> orderIds = orderInfoService.getReturnHiddenOrderIds();

        if (CollectionUtil.isNotEmpty(orderIds)) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderReturnHidden(true);
            QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
            orderInfoQueryWrapper.in("order_id", orderIds);

            if (!orderInfoRepository.edit(orderInfo, orderInfoQueryWrapper)) {
                throw new BusinessException(__("修改退货按钮隐藏失败！"));
            }

            //清理缓存
            configBaseService.cleanRequestCache();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addOrderInvoice(OrderInvoiceAddReq orderInvoiceAddReq) {
        UserInfo userInfo = userInfoRepository.get(orderInvoiceAddReq.getUserId());

        if (userInfo == null) {
            throw new BusinessException(__("用户信息不存在！"));
        }

        OrderBase orderBase = orderBaseRepository.get(orderInvoiceAddReq.getOrderId());

        if (orderBase == null) {
            throw new BusinessException(__("订单信息不存在！"));
        }
        OrderInfo orderInfo = orderInfoRepository.get(orderInvoiceAddReq.getOrderId());

        if (orderInfo == null) {
            throw new BusinessException(__("订单详细信息不存在！"));
        }

        UserInvoice userInvoice = userInvoiceService.get(orderInvoiceAddReq.getUserInvoiceId());

        if (userInvoice == null) {
            throw new BusinessException(__("用户发票不存在！"));
        }

        OrderInvoice orderInvoice = new OrderInvoice();
        BeanUtils.copyProperties(orderBase, orderInvoice);
        BeanUtils.copyProperties(userInvoice, orderInvoice);
        orderInvoice.setInvoiceContent(orderInfo.getOrderTitle());
        orderInvoice.setInvoiceAmount(orderBase.getOrderPaymentAmount());
        orderInvoice.setOrderIsPaid(true);
        orderInvoice.setInvoiceStatus(0);
        orderInvoice.setInvoiceTime(new Date().getTime());
        String invoiceContactEmail = userInvoice.getInvoiceContactEmail();

        if (StrUtil.isNotEmpty(invoiceContactEmail)) {
            orderInvoice.setUserEmail(invoiceContactEmail);
        } else if (StrUtil.isNotEmpty(userInfo.getUserEmail())) {
            orderInvoice.setUserEmail(userInfo.getUserEmail());
        }

        if (!orderInvoiceRepository.save(orderInvoice)) {
            throw new BusinessException(__("保存发票数据失败!"));
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderAddOutput replaceAdd(CheckoutInput orderBase) {
        Integer userId = ContextUtil.checkLoginUserId();
        orderBase.setOrderSaleId(userId);

        UserInfo userInfo = userInfoRepository.get(orderBase.getUserId());

        if (userInfo == null) {
            throw new BusinessException(__("买家用户不存在!"));
        }
        //用户名称
        orderBase.setUserNickname(userInfo.getUserNickname());

        return add(orderBase);
    }

    /**
     * 判断是否有活动条件限制
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean ifActivity(String orderId) {
        OrderInfo orderInfo = orderInfoRepository.get(orderId);
        if (orderInfo == null) {
            throw new BusinessException(__("订单信息不存在，无法校验活动前置条件"));
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean companyOrderReview(OrderReviewReq orderReviewReq) {
        throw new BusinessException(__("开源版不支持企业订单审核"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verification(String orderId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderInfo orderInfo = orderInfoRepository.get(orderId);

        if (orderInfo == null) {
            throw new BusinessException("该订单信息不存在！");
        }

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderInfo, OrderInfo::getStoreId)) {
            if (Objects.equals(orderInfo.getOrderIsShipped(), StateCode.ORDER_SHIPPED_STATE_YES)) {
                throw new BusinessException(__("已发货，无法操作！"));
            }
            orderInfo.setOrderIsShipped(StateCode.ORDER_SHIPPED_STATE_YES);

            if (!orderInfoRepository.edit(orderInfo)) {
                throw new BusinessException(__("修改订单信息失败！"));
            }
            Integer nextOrderStateId = getNextOrderStateId(orderInfo.getOrderStateId());

            if (!editNextState(orderId, orderInfo.getOrderStateId(), nextOrderStateId, "")) {
                throw new BusinessException(__("修改订单状态失败！"));
            }

            return true;
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @Override
    public void batchOrderTemp(HttpServletResponse response) {
        //编码问题
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(__("" +
                    "" +
                    "") + "-" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet writeSheet = EasyExcelUtil.writeSelectedSheet(BatchOrderTemp.class, 0, "批量下单模板");
            excelWriter.write(new ArrayList<BatchOrderTemp>(), writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(__("导出Excel编码异常"));
        } catch (IOException e) {
            throw new BusinessException(__("导出Excel文件异常"));
        }

    }

    @Override
    public void importBatchOrderTemp(MultipartFile file) throws Exception {
        BatchOrderTempListener batchOrderTempListener = new BatchOrderTempListener();
        Class<?> tempClass = BatchOrderTemp.class;

        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream)
                // 注册监听器，可以在这里校验字段
                .registerReadListener(batchOrderTempListener)
                .head(tempClass)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                .doReadSync();
    }

    @Override
    public OrderSettlementRes getSettlementList(OrderInfoListReq orderInfoListReq) {
        OrderSettlementRes orderSettlementRes = new OrderSettlementRes();
        long reqPage = orderInfoListReq.getPage() == null ? 1L : orderInfoListReq.getPage().longValue();
        long reqSize = orderInfoListReq.getSize() == null ? 10L : orderInfoListReq.getSize().longValue();
        orderSettlementRes.setItems(OrderSettlementPageRes.empty(reqPage, reqSize));

        Long withdrawTime = orderBaseService.getWithdrawTime();

        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.eq("order_state_id", StateCode.ORDER_STATE_FINISH);
        orderInfoQueryWrapper.le("update_time", withdrawTime);
        orderInfoQueryWrapper.eq("order_is_paid", StateCode.ORDER_PAID_STATE_YES);
        orderInfoQueryWrapper.eq("order_is_settlemented", 0);
        orderInfoQueryWrapper.eq("store_id", orderInfoListReq.getStoreId());

        IPage<OrderInfo> infoIPage = orderInfoService.lists(orderInfoQueryWrapper, orderInfoListReq.getPage(), orderInfoListReq.getSize());

        if (infoIPage != null && CollectionUtil.isNotEmpty(infoIPage.getRecords())) {
            IPage<OrderVo> items = new Page<>();
            BeanUtil.copyProperties(infoIPage, items);
            List<OrderVo> orderVos = BeanUtil.copyToList(infoIPage.getRecords(), OrderVo.class);

            List<String> ids = CommonUtil.column(orderVos, OrderVo::getOrderId);
            QueryWrapper<OrderReturn> returnQueryWrapper = new QueryWrapper<>();
            returnQueryWrapper.eq("return_state_id", StateCode.RETURN_PROCESS_CHECK);
            returnQueryWrapper.in("order_id", ids);
            List<OrderReturn> orderReturns = orderReturnRepository.find(returnQueryWrapper);

            if (CollectionUtil.isNotEmpty(orderReturns)) {
                List<String> returnOrderIds = CommonUtil.column(orderReturns, OrderReturn::getOrderId);

                orderVos.removeIf(orderVo -> returnOrderIds.contains(orderVo.getOrderId()));

                if (CollectionUtil.isEmpty(orderVos)) {
                    orderSettlementRes.setItems(OrderSettlementPageRes.empty(reqPage, reqSize));
                    return orderSettlementRes;
                }
            }

            items.setRecords(orderVos);
            orderSettlementRes.setItems(OrderSettlementPageRes.fromMyBatisPage(items, orderVos));
            List<OrderVo> records = items.getRecords();
            List<String> orderIds = CommonUtil.column(records, OrderVo::getOrderId);

            List<OrderBase> orderBases = orderBaseRepository.gets(orderIds);

            if (CollectionUtil.isEmpty(orderBases)) {
                throw new BusinessException(__("订单信息为空！"));
            }
            Map<String, OrderBase> orderBaseMap = orderBases.stream().collect(Collectors.toMap(OrderBase::getOrderId, OrderBase -> OrderBase, (k1, k2) -> k1));

            List<OrderData> orderDataList = orderDataRepository.gets(orderIds);

            if (CollectionUtil.isEmpty(orderDataList)) {
                throw new BusinessException(__("订单数据信息为空！"));
            }
            Map<String, OrderData> orderDataMap = orderDataList.stream().collect(Collectors.toMap(OrderData::getOrderId, OrderData -> OrderData, (k1, k2) -> k1));

            for (OrderVo record : records) {
                String orderId = record.getOrderId();
                OrderBase orderBase = orderBaseMap.get(orderId);

                if (orderBase != null) {
                    record.setOrderPaymentAmount(orderBase.getOrderPaymentAmount());
                    record.setCurrencySymbolLeft(orderBase.getCurrencySymbolLeft());
                }

                OrderData orderData = orderDataMap.get(orderId);

                if (orderData != null) {
                    record.setOrderCommissionFee(orderData.getOrderCommissionFee());
                }
            }

            BigDecimal settleAmount = orderBases.stream().map(OrderBase::getOrderPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal commFee = orderDataList.stream().map(OrderData::getOrderCommissionFee).reduce(BigDecimal.ZERO, BigDecimal::add);
            orderSettlementRes.setCommissionAmount(commFee);

            settleAmount = NumberUtil.sub(settleAmount, commFee);
            orderSettlementRes.setSettleAmount(settleAmount);

            QueryWrapper<OrderReturn> orderReturnQueryWrapper = new QueryWrapper<>();
            orderReturnQueryWrapper.eq("store_id", orderInfoListReq.getStoreId());
            orderReturnQueryWrapper.eq("return_is_settlemented", 0);
            orderReturnQueryWrapper.in("return_state_id", Arrays.asList(StateCode.RETURN_PROCESS_RECEIVED,
                    StateCode.RETURN_PROCESS_REFUND,
                    StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION,
                    StateCode.RETURN_PROCESS_FINISH));
            orderReturnQueryWrapper.in("order_id", orderIds);
            List<OrderReturn> orderReturnList = orderReturnRepository.find(orderReturnQueryWrapper);

            if (CollectionUtil.isEmpty(orderReturnList)) {
                orderSettlementRes.setRefundAmount(BigDecimal.ZERO);
            } else {
                List<String> returnIds = CommonUtil.column(orderReturnList, OrderReturn::getReturnId);
                orderSettlementRes.setReturnIds(returnIds);
                BigDecimal refundAmount = orderReturnList.stream().map(OrderReturn::getReturnRefundAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal returnCommFee = orderReturnList.stream().map(OrderReturn::getReturnCommisionFee).reduce(BigDecimal.ZERO, BigDecimal::add);
                orderSettlementRes.setRefundAmount(NumberUtil.sub(refundAmount, returnCommFee));
            }
            orderSettlementRes.setWithdrawAmount(NumberUtil.sub(orderSettlementRes.getSettleAmount(), orderSettlementRes.getRefundAmount()));
        }

        return orderSettlementRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean settlementApply(Integer userBankId) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isStore()) {
            throw new BusinessException(__("非商家用户！"));
        }

        Integer storeId = loginUser.getStoreId();
        OrderInfoListReq orderInfoListReq = new OrderInfoListReq();
        orderInfoListReq.setStoreId(storeId);
        orderInfoListReq.setSize(ConstantConfig.MAX_LIST_NUM);
        OrderSettlementRes settlementList = getSettlementList(orderInfoListReq);

        if (settlementList.getItems() == null || CollectionUtil.isEmpty(settlementList.getItems().getItems())) {
            throw new BusinessException(__("无待结算订单！"));
        }

        List<OrderVo> orderVoList = settlementList.getItems().getItems();
        List<String> orderIds = CommonUtil.column(orderVoList, OrderVo::getOrderId);

        Date curTime = new Date(); // 当前时间
        //更改订单结算状态
        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.in("order_id", orderIds);
        orderInfoQueryWrapper.eq("order_is_settlemented", 0);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderIsSettlemented(2);
        orderInfo.setOrderSettlementTime(curTime);

        if (!orderInfoRepository.edit(orderInfo, orderInfoQueryWrapper)) {
            throw new BusinessException(__("更改订单结算状态失败！"));
        }

        ConsumeWithdraw consumeWithdraw = new ConsumeWithdraw();

        //更改退款结算状态
        if (CollectionUtil.isNotEmpty(settlementList.getReturnIds())) {
            consumeWithdraw.setReturnId(CollUtil.join(settlementList.getReturnIds(), ","));

            QueryWrapper<OrderReturn> orderReturnQueryWrapper = new QueryWrapper<>();
            orderReturnQueryWrapper.in("return_id", settlementList.getReturnIds());
            orderReturnQueryWrapper.eq("return_is_settlemented", 0);
            OrderReturn orderReturn = new OrderReturn();
            orderReturn.setReturnIsSettlemented(2);
            orderReturn.setReturnSettlementTime(curTime);

            if (!orderReturnRepository.edit(orderReturn, orderReturnQueryWrapper)) {
                throw new BusinessException(__("更改退款结算状态失败！"));
            }
        } else {
            consumeWithdraw.setReturnId("");
        }

        consumeWithdraw.setUserId(loginUser.getUserId());
        consumeWithdraw.setOrderId(CollUtil.join(orderIds, ","));
        consumeWithdraw.setWithdrawAmount(settlementList.getWithdrawAmount());// 提现额度
        consumeWithdraw.setWithdrawState(0);// 是否成功(BOOL):0-申请中;1-提现通过

        UserBankCard userBankCard = userBankCardRepository.get(userBankId);

        if (userBankCard == null) {
            throw new BusinessException(__("提现银行卡不存在！"));
        }

        if (!Objects.equals(userBankCard.getUserId(), loginUser.getUserId())) {
            throw new BusinessException(__("提现银行卡用户与当前用户不匹配！"));
        }
        consumeWithdraw.setWithdrawBank(userBankCard.getUserBankCardAddress());
        consumeWithdraw.setWithdrawMobile(userBankCard.getUserBankCardMobile());
        consumeWithdraw.setWithdrawAccountNo(userBankCard.getUserBankCardCode());
        consumeWithdraw.setWithdrawAccountName(userBankCard.getUserBankCardName());
        consumeWithdraw.setWithdrawTime(curTime.getTime());
        consumeWithdraw.setStoreId(storeId);

        UserResource userResource = userResourceRepository.get(consumeWithdraw.getUserId());

        if (userResource == null) {
            throw new BusinessException(__("该用户资源不存在！"));
        }
        BigDecimal userMoney = userResource.getUserMoney();
        BigDecimal withdrawAmount = consumeWithdraw.getWithdrawAmount();

        if (userMoney.compareTo(withdrawAmount) < 0) {
            throw new BusinessException(__("用户资金不足，无法申请！"));
        }
        userResource.setUserMoney(userMoney.subtract(withdrawAmount));
        userResource.setUserMoneyFrozen(userResource.getUserMoneyFrozen().add(withdrawAmount));

        if (!userResourceRepository.edit(userResource)) {
            throw new BusinessException(__("修改用户资源表失败！"));
        }

        //提现手续费
        Float withdraw_fee_rate = configBaseService.getConfig("withdraw_fee_rate", 0.00f);
        BigDecimal withdrawFee = NumberUtil.div(NumberUtil.mul(consumeWithdraw.getWithdrawAmount(), withdraw_fee_rate), 100);
        consumeWithdraw.setWithdrawFee(withdrawFee);

        if (!consumeWithdrawRepository.add(consumeWithdraw)) {
            throw new BusinessException(__("保存提现申请数据失败！"));
        }

        return true;
    }

    @Override
    public SellerTodoRes getSellerTodo() {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer storeId = loginUser.getStoreId();

        if (CheckUtil.isEmpty(storeId)) {
            throw new BusinessException(__("店铺编号为空！"));
        }

        SellerTodoRes dashboardRes = new SellerTodoRes();

        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
        orderInfoQueryWrapper.eq("store_id", storeId);
        orderInfoQueryWrapper.in("order_state_id", Arrays.asList(StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING));
        dashboardRes.setWaitShippingNum(orderInfoRepository.count(orderInfoQueryWrapper));

        QueryWrapper<OrderReturn> orderReturnQueryWrapper = new QueryWrapper<>();
        orderReturnQueryWrapper.eq("store_id", storeId);
        orderReturnQueryWrapper.in("return_state_id", Arrays.asList(StateCode.RETURN_PROCESS_SUBMIT, StateCode.RETURN_PROCESS_CHECK));
        dashboardRes.setReviewNum(orderReturnRepository.count(orderReturnQueryWrapper));

        QueryWrapper<ProductIndex> productIndexQueryWrapper = new QueryWrapper<>();
        productIndexQueryWrapper.eq("store_id", storeId);
        productIndexQueryWrapper.eq("product_state_id", StateCode.PRODUCT_STATE_ILLEGAL);
        dashboardRes.setOffNum(productIndexRepository.count(productIndexQueryWrapper));

        Integer stockWarning = configBaseService.getConfig("stock_warning", 5);
        dashboardRes.setProductWarningNum(productItemDao.getStockWarningCount(storeId, stockWarning));

        QueryWrapper<ProductComment> productCommentQueryWrapper = new QueryWrapper<>();
        productCommentQueryWrapper.eq("store_id", storeId);
        productCommentQueryWrapper.eq("comment_is_reply", 0);
        dashboardRes.setWaitReplyNum(productCommentRepository.count(productCommentQueryWrapper));

        QueryWrapper<OrderInvoice> orderInvoiceQueryWrapper = new QueryWrapper<>();
        orderInvoiceQueryWrapper.eq("store_id", storeId);
        orderInvoiceQueryWrapper.eq("order_is_paid", 1);
        orderInvoiceQueryWrapper.eq("invoice_status", 0);
        dashboardRes.setInvoiceWaitNum(orderInvoiceRepository.count(orderInvoiceQueryWrapper));

        return dashboardRes;
    }

    @Override
    public AdminTodoRes getAdminTodo() {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(__("非平台管理员，无权限！"));
        }

        AdminTodoRes adminDashboardRes = new AdminTodoRes();

        QueryWrapper<StoreBase> storeBaseQueryWrapper = new QueryWrapper<>();
        storeBaseQueryWrapper.eq("store_state_id", 3220);
        adminDashboardRes.setStoreCertificationNum(storeBaseRepository.count(storeBaseQueryWrapper));

        QueryWrapper<ProductIndex> productIndexQueryWrapper = new QueryWrapper<>();
        productIndexQueryWrapper.eq("product_verify_id", StateCode.PRODUCT_VERIFY_WAITING);
        adminDashboardRes.setProductVerifyNum(productIndexRepository.count(productIndexQueryWrapper));

        QueryWrapper<ConsumeWithdraw> consumeWithdrawQueryWrapper = new QueryWrapper<>();
        consumeWithdrawQueryWrapper.eq("withdraw_state", 0);
        consumeWithdrawQueryWrapper.eq("withdraw_mode", 0);
        adminDashboardRes.setWithdrawNum(consumeWithdrawRepository.count(consumeWithdrawQueryWrapper));

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_is_authentication", 1);
        adminDashboardRes.setUserCertificationNum(userInfoRepository.count(userInfoQueryWrapper));

        adminDashboardRes.setStoryVerifyNum(0L);

        QueryWrapper<FeedbackBase> feedbackBaseQueryWrapper = new QueryWrapper<>();
        feedbackBaseQueryWrapper.eq("feedback_question_status", false);
        adminDashboardRes.setFeedbackTodoNum(feedbackBaseRepository.count(feedbackBaseQueryWrapper));

        return adminDashboardRes;
    }

    @Override
    public OrderPayRes getPayList(List<String> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            throw new BusinessException(__("订单信息不存在！"));
        }

        Integer userId = ContextUtil.checkLoginUserId();

        QueryWrapper<ConsumeTrade> consumeTradeQueryWrapper = new QueryWrapper<>();
        consumeTradeQueryWrapper.in("order_id", orderIds);
        List<ConsumeTrade> consumeTrades = consumeTradeRepository.find(consumeTradeQueryWrapper);

        if (CollectionUtil.isEmpty(consumeTrades)) {
            throw new BusinessException(__("交易订单信息不存在！"));
        }

        for (ConsumeTrade consumeTrade : consumeTrades) {
            if (!Objects.equals(consumeTrade.getBuyerId(), userId)) {
                throw new BusinessException(__("不是该用户订单，无权限！"));
            }
        }

        OrderPayRes orderPayRes = new OrderPayRes();
        orderPayRes.setConsumeTrades(consumeTrades);
        BigDecimal paymentAmount = consumeTrades.stream().map(ConsumeTrade::getOrderPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        orderPayRes.setOrderPaymentAmount(paymentAmount);

        String orderId = orderIds.get(0);
        if (orderId.startsWith(ORDER_PREFIX)) {
            OrderDeliveryAddress orderDeliveryAddress = orderDeliveryAddressRepository.get(orderIds.get(0));

            if (orderDeliveryAddress == null) {
                throw new BusinessException(__("该订单收货地址不存在！"));
            }

            orderPayRes.setDelivery(orderDeliveryAddress);
        }

        return orderPayRes;
    }


    @Override
    @Transactional
    public void autoUploadShipping() {
        // 更新未付款订单，取消超时订单
        List<String> orderIds = orderInfoService.getOrderShippingWaitPushWx();
        for (String orderId : orderIds) {

            doUploadShipping(orderId);

            //清理缓存
            configBaseService.cleanRequestCache();
        }
    }


    @Override
    public boolean doUploadShipping(String order_id) {
        QueryWrapper<ConsumeDeposit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", order_id);
        ConsumeDeposit consumeDeposit = consumeDepositRepository.findOne(queryWrapper);

        OrderInfo orderInfo = new OrderInfo();
        List<OrderLogistics> olList = new ArrayList<>();
        orderInfo = orderInfoRepository.get(order_id);

        QueryWrapper<OrderLogistics> logisticsWrapper = new QueryWrapper<>();
        logisticsWrapper.eq("order_id", order_id);
        olList = orderLogisticsRepository.find(logisticsWrapper);

        List<Integer> logiticsIds = olList.stream().map(OrderLogistics::getLogisticsId).distinct().collect(Collectors.toList());

        List<StoreExpressLogistics> storeExpressLogistics = new ArrayList<>();

        if (CollUtil.isNotEmpty(logiticsIds)) {
            storeExpressLogistics = storeExpressLogisticsRepository.gets(logiticsIds);
        } else {
            logger.error(String.format("order_id : %s 店铺发货数据异常", order_id));
        }

        if (orderInfo == null) {
            logger.error(String.format("order_id : %s 订单数据异常", order_id));
        }

        if (consumeDeposit == null) {
            return true;
        }

        if (!Objects.equals(consumeDeposit.getPaymentChannelId(), 1403)) {
            return true;
        }

        Integer delivery_mode = 1;
        Boolean isAllDelivered = true;
        List<Map<String, Object>> shippingList = new ArrayList<>();

        //执行推送
        String token = wechatService.getXcxAccessToken(true);

        if (olList.size() > 0) {
            if (olList.size() > 1) {
                delivery_mode = 2;
            }

            for (OrderLogistics orderLogistics : olList) {
                Map shipping = new HashMap<>();
                shipping.put("tracking_no", orderLogistics.getOrderTrackingNumber());
                shipping.put("express_company", orderLogistics.getLogisticsId());
                shipping.put("item_desc", orderInfo.getOrderTitle());
                Integer logiticsId = orderLogistics.getLogisticsId();

                StoreExpressLogistics shopStoreExpressLogistics = new StoreExpressLogistics();
                shopStoreExpressLogistics = storeExpressLogisticsRepository.get(logiticsId);

                if (shopStoreExpressLogistics == null) {
                    logger.error(String.format("order_id : %s 卖家物流方式异常", order_id));
                } else {
                    Map contact = new HashMap<>();
                    contact.put("consignor_contact", maskMiddleFour(shopStoreExpressLogistics.getLogisticsMobile()));
                    shipping.put("contact", contact);
                }

                shippingList.add(shipping);
            }
        }

        String mchId = consumeDeposit.getDepositSellerId();
        String outTradeNo = consumeDeposit.getDepositNo();
        String openid = consumeDeposit.getDepositBuyerId();
        String desc = orderInfo.getOrderTitle();
        String transactionId = consumeDeposit.getDepositTradeNo();
        Integer logistics_type = 1;

        Integer deliveryTypeId = orderInfo.getDeliveryTypeId();

        if (orderInfo.getKindId() != StateCode.PRODUCT_KIND_ENTITY) {
            logistics_type = 3; // 虚拟商品
        } else {
            if (Objects.equals(deliveryTypeId, StateCode.DELIVERY_TYPE_SELF_PICK_UP)) {
                logistics_type = 4; // 用户自提
            } else {
                logistics_type = 1; // 物流发货
            }
        }

        OrderInfo updateOrderInfo = new OrderInfo();
        updateOrderInfo.setOrderId(order_id);

        try {
            WxShippingUtil.uploadShippingInfo(mchId, outTradeNo, token, openid, transactionId, logistics_type, delivery_mode, isAllDelivered, shippingList);
            updateOrderInfo.setOrderPushWxShipping(1);
        } catch (Exception e) {
            logger.error(String.format("order_id : %s 微信推送发货信息失败", order_id));
            updateOrderInfo.setOrderPushWxShipping(2);
        }

        return orderInfoService.edit(updateOrderInfo);
    }

    public static String maskMiddleFour(String str) {
        if (str == null || str.length() < 7) {
            // 字符串太短，不处理或可抛出异常
            return str;
        }

        int start = (str.length() - 4) / 2; // 中间四位开始位置
        StringBuilder masked = new StringBuilder(str);
        for (int i = 0; i < 4; i++) {
            masked.setCharAt(start + i, '*');
        }

        return masked.toString();
    }

    @Override
    public void invoiceHidden() {
        List<String> orderIds = orderInfoService.getInvoiceHiddenOrderIds();

        if (CollectionUtil.isNotEmpty(orderIds)) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderInvoiceHidden(true);
            QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper<>();
            orderInfoQueryWrapper.in("order_id", orderIds);

            if (!orderInfoRepository.edit(orderInfo, orderInfoQueryWrapper)) {
                throw new BusinessException(__("修改开票按钮隐藏失败！"));
            }

            //清理缓存
            configBaseService.cleanRequestCache();
        }
    }

    @Override
    public BatchOrderDataRes batchOrders(BatchOrderListReq batchOrderListReq) {
        Integer userId = batchOrderListReq.getUserId();

        BatchOrderDataRes batchOrderDataRes = new BatchOrderDataRes();
        List<BatchOrderRes> batchOrderResList = new ArrayList<>();

        QueryWrapper<BatchOrder> batchOrderQueryWrapper = new QueryWrapper<>();
        batchOrderQueryWrapper.eq("user_id", userId);

        List<BatchOrder> batchOrders = batchOrderRepository.find(batchOrderQueryWrapper);

        if (CollectionUtil.isNotEmpty(batchOrders)) {
            List<Long> itemIds = CommonUtil.column(batchOrders, BatchOrder::getItemId);
            Map<Long, ProductItemVo> itemVoMap = new HashMap<>();

            List<ProductItemVo> items = productBaseRepository.getItems(itemIds, null);

            if (CollectionUtil.isNotEmpty(items)) {
                itemVoMap = items.stream().collect(Collectors.toMap(ProductItemVo::getItemId, ProductItemVo -> ProductItemVo, (k1, k2) -> k1));
            }
            for (BatchOrder batchOrder : batchOrders) {
                BatchOrderRes batchOrderRes = BeanUtil.copyProperties(batchOrder, BatchOrderRes.class);

                if (!Objects.equals(batchOrderRes.getBatchMatchStatus(), 3) && itemVoMap.containsKey(batchOrder.getItemId())) {
                    ProductItemVo productItemVo = itemVoMap.get(batchOrder.getItemId());
                    if (productItemVo == null) {
                        continue;
                    }
                    batchOrderRes.setProductName(productItemVo.getProductName());
                    batchOrderRes.setItemName(productItemVo.getItemName());
                    batchOrderRes.setProductImage(productItemVo.getProductImage());
                    batchOrderRes.setItemUnitPrice(productItemVo.getItemUnitPrice());
                }

                batchOrderResList.add(batchOrderRes);
            }
            batchOrderDataRes.setItems(batchOrderResList);
            List<BatchOrderRes> exactMatchList = batchOrderResList.stream().filter(item -> item != null && Objects.equals(item.getBatchMatchStatus(), 1)).collect(Collectors.toList());
            List<BatchOrderRes> forInquiryList = batchOrderResList.stream().filter(item -> item != null && Objects.equals(item.getBatchMatchStatus(), 2)).collect(Collectors.toList());
            List<BatchOrderRes> noMatchList = batchOrderResList.stream().filter(item -> item != null && Objects.equals(item.getBatchMatchStatus(), 3)).collect(Collectors.toList());

            Integer batchMatchStatus = batchOrderListReq.getBatchMatchStatus();

            if (CheckUtil.isNotEmpty(batchMatchStatus)) {

                if (Objects.equals(batchMatchStatus, 1)) {
                    batchOrderDataRes.setItems(exactMatchList);
                } else if (Objects.equals(batchMatchStatus, 2)) {
                    batchOrderDataRes.setItems(forInquiryList);
                } else if (Objects.equals(batchMatchStatus, 3)) {
                    batchOrderDataRes.setItems(noMatchList);
                }
            }

            batchOrderDataRes.setAllNum(batchOrderResList.size());
            batchOrderDataRes.setExactMatchNum(exactMatchList.size());
            batchOrderDataRes.setForInquiryNum(forInquiryList.size());
            batchOrderDataRes.setNoMatchNum(noMatchList.size());
        }

        return batchOrderDataRes;
    }

    @Override
    public boolean editBatchOrder(BatchOrder batchOrder) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer userId = user.getUserId();
        Integer industryId = user.getIndustryId();

        batchOrder.setUserId(userId);
        BatchOrder batchOrderRow = batchOrderRepository.get(batchOrder.getBatchId());

        if (batchOrderRow == null) {
            throw new BusinessException(__("该批量下单不存在！"));
        }

        if (!Objects.equals(batchOrderRow.getUserId(), userId)) {
            throw new BusinessException(__("无权限修改！"));
        }

        if (CheckUtil.isNotEmpty(batchOrder.getItemId())) {
            fixBatchOrder(batchOrder, industryId);
        }

        if (batchOrder.getCartQuantity() != null && batchOrder.getCartQuantity().compareTo(1) < 0) {
            throw new BusinessException(__("数量必须为有效数字且不能小于1！"));
        }

        if (!batchOrderRepository.edit(batchOrder)) {
            throw new BusinessException(__("修改批量下单商品失败！"));
        }

        return true;
    }

    private void fixBatchOrder(BatchOrder batchOrder, Integer industryId) {
        if (batchOrder == null) {
            throw new BusinessException(__("该批量下单不存在！"));
        }

        ProductItem productItem = productItemRepository.get(batchOrder.getItemId());

        if (productItem == null) {
            throw new BusinessException(__("该SKU商品不存在！"));
        }
        ProductIndex productIndex = productIndexRepository.get(productItem.getProductId());

        if (productIndex == null) {
            throw new BusinessException(__("该产品索引信息不存在！"));
        }

        if (!Objects.equals(productIndex.getProductStateId(), StateCode.PRODUCT_STATE_NORMAL)) {
            throw new BusinessException(__("该商品未上架！"));
        }

        if (configBaseService.ifIndustry()) {

            if (!Convert.toList(Integer.class, productIndex.getIndustryIds()).contains(industryId)) {
                throw new BusinessException(__("无法选择不符合的行业商品！"));
            }
        }

        batchOrder.setItemNumber(productItem.getItemNumber());

        if (CheckUtil.isNotEmpty(productItem.getItemUnitPrice())) {
            batchOrder.setBatchMatchStatus(1);
        } else {
            batchOrder.setBatchMatchStatus(2);
        }
    }

    @Override
    public boolean addBatchOrder(Long itemId) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        BatchOrder batchOrder = new BatchOrder();
        batchOrder.setUserId(user.getUserId());
        batchOrder.setItemId(itemId);

        fixBatchOrder(batchOrder, user.getIndustryId());

        if (!batchOrderRepository.add(batchOrder)) {
            throw new BusinessException(__("添加批量下单商品失败！"));
        }

        return true;
    }

    @Override
    public boolean removeBatchOrders(List<Integer> batchIds) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer userId = user.getUserId();
        List<BatchOrder> batchOrders = batchOrderRepository.gets(batchIds);

        if (CollectionUtil.isEmpty(batchOrders)) {
            throw new BusinessException(__("批量下单数据不存在！"));
        }

        batchOrders.stream()
                .filter(batchOrder -> batchOrder != null && !Objects.equals(batchOrder.getUserId(), userId))
                .findFirst()
                .ifPresent(batchOrder -> {
                    throw new BusinessException(__("无权限修改！"));
                });

        if (!batchOrderRepository.remove(batchIds)) {
            throw new BusinessException(__("删除批量下单数据失败！"));
        }

        return true;
    }

    @Override
    public boolean reviseOrderInvoice(Integer orderInvoiceId, Integer userInvoiceId) {
        Integer userId = ContextUtil.checkLoginUserId();
        OrderInvoice orderInvoice = orderInvoiceRepository.get(orderInvoiceId);

        if (orderInvoice == null) {
            throw new BusinessException(__("订单发票信息不存在！"));
        }

        UserInvoice userInvoice = userInvoiceRepository.get(userInvoiceId);

        if (userInvoice == null) {
            throw new BusinessException(__("用户发票信息不存在！"));
        }

        if (!Objects.equals(orderInvoice.getUserId(), userId) || !Objects.equals(userInvoice.getUserId(), userId)) {
            throw new BusinessException(__("无权限修改！"));
        }
        orderInvoice.setInvoiceTitle(userInvoice.getInvoiceTitle());
        orderInvoice.setInvoiceCompanyCode(userInvoice.getInvoiceCompanyCode());
        orderInvoice.setInvoiceIsCompany(userInvoice.getInvoiceIsCompany());
        orderInvoice.setInvoiceAddress(userInvoice.getInvoiceAddress());
        orderInvoice.setInvoicePhone(userInvoice.getInvoicePhone());
        orderInvoice.setInvoiceBankname(userInvoice.getInvoiceBankname());
        orderInvoice.setInvoiceBankaccount(userInvoice.getInvoiceBankaccount());
        orderInvoice.setInvoiceType(userInvoice.getInvoiceType());
        orderInvoice.setInvoiceContactName(userInvoice.getInvoiceContactName());

        orderInvoice.setInvoiceStatus(0);
        orderInvoice.setInvoiceStatusRemark("");

        if (!orderInvoiceRepository.edit(orderInvoice)) {
            throw new BusinessException(__("修改发票数据失败!"));
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderAddOutput addCustomerOrder(CheckoutInput orderBase) {
        Integer saleId = ContextUtil.checkLoginUserId();
        UserInfo saleInfo = userInfoRepository.get(saleId);

        if (saleInfo == null || !saleInfo.getUserIsSale()) {
            throw new BusinessException(__("销售员不存在!"));
        }

        orderBase.setOrderSaleId(saleId);

        UserInfo userInfo = userInfoRepository.get(orderBase.getUserId());

        if (userInfo == null) {
            throw new BusinessException(__("买家用户不存在!"));
        }
        //用户名称
        orderBase.setUserNickname(userInfo.getUserNickname());

        return add(orderBase);
    }

    @Override
    public IPage<OrderCourseRes> purchasedCourseList(OrderInfoListReq orderInfoListReq) {
        IPage<OrderCourseRes> courseResPage = new Page<>();
        courseResPage.setCurrent(orderInfoListReq.getPage());
        courseResPage.setSize(orderInfoListReq.getSize());
        courseResPage.setTotal(0);
        courseResPage.setRecords(Collections.emptyList());
        return courseResPage;
    }

    @Override
    @Transactional
    public boolean orderAdjustFee(List<OrderItem> orderItems) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        List<Long> orderItemIds = CommonUtil.column(orderItems, OrderItem::getOrderItemId);
        Map<Long, BigDecimal> orderItemMap = orderItems.stream().collect(Collectors.toMap(OrderItem::getOrderItemId, OrderItem::getOrderItemPaymentAmount));

        List<OrderItem> orderItemList = orderItemRepository.gets(orderItemIds);

        if (CollectionUtil.isEmpty(orderItemList)) {
            throw new BusinessException(__("订单商品数据为空！"));
        }
        List<String> orderIds = CommonUtil.column(orderItemList, OrderItem::getOrderId);

        if (orderIds.size() > 1) {
            throw new BusinessException(__("不属于同一个订单！"));
        }
        String orderId = orderIds.get(0);
        OrderBase orderBase = orderBaseRepository.get(orderId);

        if (orderBase == null) {
            throw new BusinessException(__("订单详细信息不存在！"));
        }

        if (!Objects.equals(orderBase.getOrderStateId(), StateCode.ORDER_STATE_WAIT_PAY)) {
            throw new BusinessException(__("订单非待付款状态，无法修改！"));
        }

        if (!loginUser.isPlatform() && !CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        OrderInfo orderInfo = orderInfoRepository.get(orderId);

        if (orderInfo == null) {
            throw new BusinessException(__("订单信息不存在！"));
        }

        if (!Objects.equals(orderInfo.getOrderIsPaid(), StateCode.ORDER_PAID_STATE_NO)) {
            throw new BusinessException(__("订单非待付款状态，无法修改！"));
        }

        OrderData orderData = orderDataRepository.get(orderId);

        if (orderData == null) {
            throw new BusinessException(__("订单数据不存在！"));
        }

        QueryWrapper<ConsumeTrade> consumeTradeQueryWrapper = new QueryWrapper<>();
        consumeTradeQueryWrapper.eq("order_id", orderId);
        ConsumeTrade consumeTrade = consumeTradeRepository.findOne(consumeTradeQueryWrapper);

        if (consumeTrade == null) {
            throw new BusinessException(__("交易订单不存在！"));
        }

        BigDecimal orderAdjustFee = BigDecimal.ZERO;
        BigDecimal orderCommissionFee = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItemList) {
            Long orderItemId = orderItem.getOrderItemId();
            BigDecimal oldAmount = orderItem.getOrderItemPaymentAmount();

            if (orderItemMap.containsKey(orderItemId)) {
                BigDecimal newAmount = orderItemMap.get(orderItemId);
                newAmount = newAmount.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.valueOf(0.01) : newAmount;

                BigDecimal reduceAmount = orderItem.getOrderItemVoucher().add(orderItem.getOrderItemReduce()).add(orderItem.getOrderItemSalePersonDiscount());

                if (newAmount.compareTo(reduceAmount) <= 0) {
                    throw new BusinessException(__("超出可修改最低金额，操作失败！"));
                }

                if (newAmount.compareTo(oldAmount) > 0) {
                    throw new BusinessException(__("无法提高价格，操作失败！"));
                }

                BigDecimal orderItemCommissionFee = orderItem.getOrderItemCommissionFee();

                if (newAmount.compareTo(oldAmount) < 0) {
                    BigDecimal orderItemAdjustFee = oldAmount.subtract(newAmount);
                    orderItem.setOrderItemAdjustFee(orderItem.getOrderItemAdjustFee().add(orderItemAdjustFee));
                    orderItem.setOrderItemPaymentAmount(newAmount);

                    if (CheckUtil.isNotEmpty(orderItemCommissionFee)) {
                        BigDecimal orderItemCommissionRate = orderItem.getOrderItemCommissionRate();
                        BigDecimal itemPayAmount = NumberUtil.sub(newAmount, reduceAmount);
                        orderItemCommissionFee = NumberUtil.div(NumberUtil.mul(itemPayAmount, orderItemCommissionRate), 100);

                        orderItem.setOrderItemCommissionFee(orderItemCommissionFee);
                    }

                    orderAdjustFee = orderAdjustFee.add(orderItemAdjustFee);
                }
                orderCommissionFee = orderCommissionFee.add(orderItemCommissionFee);
            }
        }

        if (orderAdjustFee.compareTo(BigDecimal.ZERO) > 0) {

            if (!orderItemRepository.edit(orderItemList)) {
                throw new BusinessException(__("修改订单商品数据失败！"));
            }
            orderBase.setOrderPaymentAmount(orderBase.getOrderPaymentAmount().subtract(orderAdjustFee));

            if (!orderBaseRepository.edit(orderBase)) {
                throw new BusinessException(__("修改订单详细信息失败！"));
            }
            orderData.setOrderAdjustFee(orderData.getOrderAdjustFee().add(orderAdjustFee));

            if (CheckUtil.isNotEmpty(orderData.getOrderCommissionFee())) {
                orderData.setOrderCommissionFee(orderCommissionFee);
            }

            if (!orderDataRepository.edit(orderData)) {
                throw new BusinessException(__("修改订单数据失败！"));
            }
            consumeTrade.setOrderPaymentAmount(consumeTrade.getOrderPaymentAmount().subtract(orderAdjustFee));
            consumeTrade.setTradePaymentAmount(consumeTrade.getTradePaymentAmount().subtract(orderAdjustFee));
            consumeTrade.setTradeAdjustFee(consumeTrade.getTradeAdjustFee().add(orderAdjustFee));

            if (CheckUtil.isNotEmpty(consumeTrade.getOrderCommissionFee())) {
                consumeTrade.setOrderCommissionFee(orderCommissionFee);
            }

            if (!consumeTradeRepository.edit(consumeTrade)) {
                throw new BusinessException(__("修改交易订单数据失败！"));
            }

            OrderStateLog orderStateLog = new OrderStateLog();
            orderStateLog.setOrderId(orderId);
            orderStateLog.setOrderStateId(orderBase.getOrderStateId());
            orderStateLog.setOrderStatePreId(orderBase.getOrderStateId());
            orderStateLog.setUserId(loginUser.getUserId());
            orderStateLog.setUserAccount(loginUser.getUserAccount());
            orderStateLog.setOrderStateTime(new Date());
            orderStateLog.setOrderReduceAmount(orderAdjustFee);

            if (!orderStateLogRepository.add(orderStateLog)) {
                throw new BusinessException(__("添加操作日志失败！"));
            }
        }

        return true;
    }

    @Override
    public void autoDistributionOrder() {
        // 分销已移除，定时任务保留空实现
    }

    @Override
    public SellerDashboardRes sellerDashboard() {
        ContextUser user = getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!user.isStore()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        SellerDashboardRes dashboardRes = new SellerDashboardRes();
        Integer storeId = user.getStoreId();
        StoreBase storeBase = storeBaseRepository.get(storeId);

        if (storeBase == null) {
            throw new BusinessException(__("店铺信息不存在！"));
        }
        StoreInfoVo storeInfoVo = BeanUtil.copyProperties(storeBase, StoreInfoVo.class);
        dashboardRes.setStoreInfo(storeInfoVo);

        CompletableFuture<Void> infoFuture1 = CompletableFuture.runAsync(() -> {
            OrderDashboardVo orderDashboardVo = new OrderDashboardVo();
            TimeRange range = TimeUtil.yestoday();

            OrderNumInput orderNumInput = new OrderNumInput();
            orderNumInput.setStoreId(storeId);
            orderNumInput.setOrderStime(range.getStart());
            orderNumInput.setOrderEtime(range.getEnd());
            orderDashboardVo.setYesterdayNum(getOrderNum(orderNumInput));

            range = TimeUtil.month();
            orderNumInput.setOrderStime(range.getStart());
            orderNumInput.setOrderEtime(range.getEnd());
            orderDashboardVo.setMonthNum(getOrderNum(orderNumInput));

            CommonNumVo orderAmount = analyticsOrderDao.getOrderAmount(null, null, storeId);

            if (orderAmount != null) {
                orderDashboardVo.setPayAmount(orderAmount.getNum());
            }
            orderNumInput.setOrderStime(null);
            orderNumInput.setOrderEtime(null);
            orderDashboardVo.setTotalNum(getOrderNum(orderNumInput));

            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_WAIT_PAY);
            orderDashboardVo.setWaitPayNum(getOrderNum(orderNumInput));

            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_FINISH);
            orderDashboardVo.setFinNum(getOrderNum(orderNumInput));

            orderNumInput.setOrderStateId(StateCode.ORDER_STATE_SHIPPED);
            orderDashboardVo.setShipNum(getOrderNum(orderNumInput));

            orderNumInput.setOrderStateId(null);
            orderNumInput.setOrderStateIds(Arrays.asList(StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING));
            orderDashboardVo.setWaitShippingNum(getOrderNum(orderNumInput));

            orderNumInput.setOrderStateIds(Arrays.asList(StateCode.ORDER_STATE_RECEIVED, StateCode.ORDER_STATE_FINISH));
            orderNumInput.setOrderBuyerEvaluationStatus(0);
            orderDashboardVo.setEvaNum(getOrderNum(orderNumInput));

            dashboardRes.setOrder(orderDashboardVo);
        }, executor);

        CompletableFuture<Void> infoFuture2 = CompletableFuture.runAsync(() -> {
            ReturnOrderDashboardVo returnOrderDashboardVo = new ReturnOrderDashboardVo();

            returnOrderDashboardVo.setFinNum(orderReturnService.getReturnNum(Arrays.asList(StateCode.RETURN_PROCESS_FINISH), storeId));
            returnOrderDashboardVo.setReviewNum(orderReturnService.getReturnNum(Arrays.asList(StateCode.RETURN_PROCESS_SUBMIT, StateCode.RETURN_PROCESS_CHECK), storeId));
            returnOrderDashboardVo.setUnFinNum(orderReturnService.getReturnNum(Arrays.asList(StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND,
                    StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION), storeId));

            dashboardRes.setReturnOrder(returnOrderDashboardVo);
        }, executor);

        CompletableFuture<Void> infoFuture3 = CompletableFuture.runAsync(() -> {
            ProductDashboardVo productDashboardVo = new ProductDashboardVo();
            productDashboardVo.setNormalNum(productBaseService.getProductNum(StateCode.PRODUCT_STATE_NORMAL, null, storeId, null, null));
            productDashboardVo.setTotalNum(productBaseService.getProductNum(null, null, storeId, null, null));
            productDashboardVo.setVerifyWaitingNum(productBaseService.getProductNum(null, StateCode.PRODUCT_VERIFY_WAITING, storeId, null, null));

            dashboardRes.setProduct(productDashboardVo);
        }, executor);

        try {
            CompletableFuture.allOf(infoFuture1, infoFuture2, infoFuture3).get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return dashboardRes;
    }
}
