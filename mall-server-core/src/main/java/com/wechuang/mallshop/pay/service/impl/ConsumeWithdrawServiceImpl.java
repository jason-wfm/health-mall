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
package com.wechuang.mallshop.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.TransferApiEnum;
import com.wechuang.mallshop.account.model.entity.UserBindConnect;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserBindConnectRepository;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.BindConnectCode;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pay.model.entity.ConsumeWithdraw;
import com.wechuang.mallshop.pay.model.entity.UserResource;
import com.wechuang.mallshop.pay.model.input.InitiateBatchTransferInput;
import com.wechuang.mallshop.pay.model.input.TransferDetailInput;
import com.wechuang.mallshop.pay.model.req.ConsumeWithdrawListReq;
import com.wechuang.mallshop.pay.model.vo.WxPayV3Vo;
import com.wechuang.mallshop.pay.repository.ConsumeWithdrawRepository;
import com.wechuang.mallshop.pay.service.ConsumeWithdrawService;
import com.wechuang.mallshop.pay.service.UserResourceService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.service.OrderInfoService;
import com.wechuang.mallshop.trade.service.OrderReturnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 提现申请表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-09-20
 */
@Service
public class ConsumeWithdrawServiceImpl extends BaseServiceImpl<ConsumeWithdrawRepository, ConsumeWithdraw, ConsumeWithdrawListReq> implements ConsumeWithdrawService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumeWithdrawServiceImpl.class);

    @Autowired
    private UserResourceService userResourceService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderReturnService orderReturnService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserBindConnectRepository userBindConnectRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Override
    public IPage<ConsumeWithdraw> getList(ConsumeWithdrawListReq consumeWithdrawListReq) {
        IPage<ConsumeWithdraw> withdrawPage = lists(consumeWithdrawListReq);
        List<ConsumeWithdraw> records = withdrawPage.getRecords();

        if (CollectionUtil.isNotEmpty(records)) {
            Map<Integer, UserInfo> userInfoMap = userInfoRepository.getUserInfoMap(CommonUtil.column(records, ConsumeWithdraw::getUserId));

            for (ConsumeWithdraw consumeWithdraw : records) {

                if (CollUtil.isNotEmpty(userInfoMap)) {
                    UserInfo userInfo = userInfoMap.get(consumeWithdraw.getUserId());

                    if (userInfo != null) {
                        consumeWithdraw.setUserNickname(userInfo.getUserNickname());
                    }
                }
            }
        }

        return withdrawPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editWithdraw(ConsumeWithdraw consumeWithdraw) {
        Integer withdrawId = consumeWithdraw.getWithdrawId();

        ConsumeWithdraw withdraw = get(withdrawId);

        if (withdraw == null) {
            throw new BusinessException(__("该数据不存在！"));
        }

        if (CheckUtil.isEmpty(consumeWithdraw.getWithdrawState())) {
            throw new BusinessException(__("提现状态不能为空！"));
        }
        withdraw.setWithdrawState(consumeWithdraw.getWithdrawState());
        withdraw.setWithdrawOpertime(consumeWithdraw.getWithdrawOpertime());
        withdraw.setWithdrawDesc(consumeWithdraw.getWithdrawDesc());
        withdraw.setWithdrawUserId(consumeWithdraw.getWithdrawUserId());

        if (consumeWithdraw.getWithdrawState().equals(1)) {
            Integer withdrawInvoiceMethod = consumeWithdraw.getWithdrawInvoiceMethod();
            withdraw.setWithdrawInvoiceMethod(withdrawInvoiceMethod);

            if (!withdrawInvoiceMethod.equals(0) && StrUtil.isEmpty(consumeWithdraw.getWithdrawBankflow())) {
                throw new BusinessException(__("银行流水账号不能为空！"));
            }

            if (StrUtil.isNotEmpty(consumeWithdraw.getWithdrawBankflow())) {
                withdraw.setWithdrawBankflow(consumeWithdraw.getWithdrawBankflow());
            }

            if (withdrawInvoiceMethod.equals(0)) {

                if (!edit(withdraw)) {
                    throw new BusinessException(__("修改提现申请表失败！"));
                }

                return true;
            } else if (withdrawInvoiceMethod.equals(1)) {
                withdraw.setWithdrawState(3);
            } else if (withdrawInvoiceMethod.equals(2)) {
                withdraw.setWithdrawState(3);
                withdraw.setWithdrawInvoiceState(4);
            }
        }

        Integer withdrawMode = withdraw.getWithdrawMode();

        if (withdrawMode == 0) {
            //余额提现
            doWithdraw(withdraw);
        } else if (withdrawMode == 1) {
            //佣金提现
            doCommisionWithdraw(withdraw);
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void doCommisionWithdraw(ConsumeWithdraw withdraw) {
        if (!edit(withdraw)) {
            throw new BusinessException(__("修改提现申请表失败！"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doWithdraw(ConsumeWithdraw withdraw) {
        String orderId = withdraw.getOrderId();
        String returnId = withdraw.getReturnId();
        int orderIsSettlemented = 0;
        BigDecimal withdrawAmount = withdraw.getWithdrawAmount();

        if (withdraw.getWithdrawState() == 3) {
            // 执行提现审核通过操作
            userResourceService.money(withdraw.getUserId(), withdrawAmount.negate(), StateCode.TRADE_TYPE_WITHDRAW, "", StateCode.PAYMENT_TYPE_OFFLINE, withdraw.getWithdrawFee(), null);
            orderIsSettlemented = 1;
        } else {
            UserResource userResource = userResourceService.get(withdraw.getUserId());

            if (userResource == null) {
                throw new BusinessException(__("该用户资源不存在！"));
            }

            userResource.setUserMoney(userResource.getUserMoney().add(withdrawAmount));
            userResource.setUserMoneyFrozen(userResource.getUserMoneyFrozen().subtract(withdrawAmount));

            if (!userResourceService.edit(userResource)) {
                throw new BusinessException(__("修改用户资源表失败！"));
            }
        }

        if (StrUtil.isNotBlank(orderId)) {
            if (!orderInfoService.saveOrderInfo(orderId, orderIsSettlemented)) {
                throw new BusinessException(__("保存订单信息表失败！"));
            }
        }

        if (StrUtil.isNotBlank(returnId)) {
            if (!orderReturnService.saveOrderReturn(returnId, orderIsSettlemented)) {
                throw new BusinessException(__("保存退款退货表失败！"));
            }
        }

        if (!edit(withdraw)) {
            throw new BusinessException(__("修改提现申请表失败！"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean wechatTransfer(Integer withdrawId, Integer withdrawUserId) {
        ConsumeWithdraw consumeWithdraw = get(withdrawId);

        if (consumeWithdraw == null) {
            throw new BusinessException(__("提现申请信息为空！"));
        }

        Integer userId = consumeWithdraw.getUserId();
        BigDecimal withdrawAmount = consumeWithdraw.getWithdrawAmount();
        long amount = withdrawAmount.multiply(BigDecimal.valueOf(100)).longValue();

        QueryWrapper<UserBindConnect> connectQueryWrapper = new QueryWrapper<>();
        connectQueryWrapper.eq("user_id", userId).eq("bind_type", BindConnectCode.WEIXIN);
        UserBindConnect bindConnect = userBindConnectRepository.findOne(connectQueryWrapper);

        if (bindConnect == null || StrUtil.isEmpty(bindConnect.getBindOpenid())) {
            throw new BusinessException(__("当前账号非微信渠道注册！"));
        }
        String openid = bindConnect.getBindOpenid();

        WxPayV3Vo wxPayV3Vo = configBaseService.getWxPayV3Vo();
        //weixin_app_id
        String appId = configBaseService.getConfig("wechat_app_id");
        wxPayV3Vo.setAppId(appId);

        try {
            InitiateBatchTransferInput batchTransferModel = InitiateBatchTransferInput.builder()
                    .appid(appId)
                    .out_batch_no(PayKit.md5(withdrawId.toString()))
                    .batch_name("转账批次")
                    .batch_remark("平台转账微信用户零钱")
                    .notify_url(wxPayV3Vo.getDomain().concat("/front/pay/callback/wechatTransferNotify"))
                    .total_amount(amount)
                    .total_num(1)
                    .transfer_detail_list(Collections.singletonList(TransferDetailInput.builder()
                            .out_detail_no(PayKit.md5(withdrawId.toString()))
                            .transfer_amount(amount)
                            .transfer_remark("平台转账微信用户零钱")
                            .openid(openid)
                            .build()))
                    .build();

            logger.info("发起商家转账请求参数 {}", JSONUtil.toJsonStr(batchTransferModel));

            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    TransferApiEnum.TRANSFER_BATCHES.toString(),
                    wxPayV3Vo.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayV3Vo.getKeyPath(),
                    JSONUtil.toJsonStr(batchTransferModel)
            );

            logger.info("发起商家转账响应 {}", response);

            if (response.getStatus() == 200) {
                JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(response.getBody()));
                String batchStatus = jsonObject.getStr("batch_status");
                String batchId = jsonObject.getStr("batch_id");
                //处理业务
                consumeWithdraw.setWithdrawBankflow(batchId);

                if (batchStatus.equals("ACCEPTED") || batchStatus.equals("PROCESSING")) {
                    consumeWithdraw.setWithdrawState(1);
                } else if (batchStatus.equals("FINISHED")) {
                    consumeWithdraw.setWithdrawState(3);
                } else if (batchStatus.equals("CLOSED")) {
                    consumeWithdraw.setWithdrawState(4);
                }

                if (CheckUtil.isNotEmpty(withdrawUserId)) {
                    consumeWithdraw.setWithdrawUserId(withdrawUserId);
                }

                consumeWithdraw.setWithdrawOpertime(new Date().getTime());

                if (!edit(consumeWithdraw)) {
                    throw new BusinessException(__("提现申请信息修改失败！"));
                }

                UserResource userResource = userResourceService.get(consumeWithdraw.getUserId());

                if (userResource == null) {
                    throw new BusinessException(__("该用户资源不存在！"));
                }
                userResource.setUserSettledAmount(userResource.getUserSettledAmount().add(withdrawAmount));
                userResource.setUserMoneyFrozen(userResource.getUserMoneyFrozen().subtract(withdrawAmount));

                if (!userResourceService.edit(userResource)) {
                    throw new BusinessException(__("修改用户资源表失败！"));
                }

                return true;
            } else {
                LogUtil.error(ConstantLog.NETWORK, JSONUtil.toJsonStr(response));
            }

            return false;
        } catch (Exception e) {
            LogUtil.error(ConstantLog.NETWORK, e);

            return false;
        }
    }

    @Override
    public boolean uploadInvoice(ConsumeWithdraw consumeWithdraw) {
        ConsumeWithdraw withdraw = get(consumeWithdraw.getWithdrawId());

        if (withdraw == null) {
            throw new BusinessException(__("该提现申请信息不存在！"));
        }
        Integer withdrawState = withdraw.getWithdrawState();

        if (!withdrawState.equals(3)) {

            if (!withdrawState.equals(1) && !withdrawState.equals(7)) {
                throw new BusinessException(__("当前提现状态异常！"));
            }
            consumeWithdraw.setWithdrawState(6);
        }
        consumeWithdraw.setWithdrawInvoiceState(2);
        consumeWithdraw.setWithdrawInvoiceRemark("");

        if (!edit(consumeWithdraw)) {
            throw new BusinessException(__("修改提现申请表失败！"));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean reviewInvoice(ConsumeWithdraw consumeWithdraw) {
        ConsumeWithdraw withdraw = get(consumeWithdraw.getWithdrawId());

        if (withdraw == null) {
            throw new BusinessException(__("该数据不存在！"));
        }

        if (!withdraw.getWithdrawInvoiceState().equals(2)) {
            throw new BusinessException(__("当前发票状态异常！"));
        }
        Integer withdrawState = withdraw.getWithdrawState();

        if (consumeWithdraw.getWithdrawState().equals(3)) {

            if (!withdrawState.equals(3)) {

                if (StrUtil.isEmpty(consumeWithdraw.getWithdrawBankflow())) {
                    throw new BusinessException(__("银行流水账号不能为空！"));
                }
                withdraw.setWithdrawState(3);
                withdraw.setWithdrawInvoiceState(1);
                withdraw.setWithdrawBankflow(consumeWithdraw.getWithdrawBankflow());
                withdraw.setWithdrawOpertime(consumeWithdraw.getWithdrawOpertime());
                withdraw.setWithdrawDesc(consumeWithdraw.getWithdrawDesc());
                withdraw.setWithdrawUserId(consumeWithdraw.getWithdrawUserId());

                Integer withdrawMode = withdraw.getWithdrawMode();

                if (withdrawMode == 0) {
                    //余额提现
                    doWithdraw(withdraw);
                } else if (withdrawMode == 1) {
                    //佣金提现
                    doCommisionWithdraw(withdraw);
                }
            } else {
                withdraw.setWithdrawInvoiceState(1);

                if (!edit(withdraw)) {
                    throw new BusinessException(__("修改提现申请表失败！"));
                }
            }

        } else {

            if (withdrawState.equals(3)) {
                consumeWithdraw.setWithdrawState(3);
            }

            consumeWithdraw.setWithdrawInvoiceState(3);

            if (!edit(consumeWithdraw)) {
                throw new BusinessException(__("修改提现申请表失败！"));
            }
        }

        return true;
    }

    /**
     * 获取证书序列号
     */
    private String getSerialNumber() {
        //证书序列号
        String serialNo = configBaseService.getConfig("wechat_pay_serial_no");

        if (StrUtil.isEmpty(serialNo)) {
            //这个是证书文件，后续要调整成读取证书文件的服务器存放地址  微信支付证书
            String cert = configBaseService.getConfig("wechat_pay_apiclient_cert");

            if (cert != null) {
                // 获取证书序列号
                X509Certificate certificate = PayKit.getCertificate(cert);
                if (certificate != null) {
                    serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
                }
            }
        }

        return serialNo;
    }
}
