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
package com.wechuang.mallshop.pay.repository.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.web.service.MessageService;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.pay.dao.UserResourceDao;
import com.wechuang.mallshop.pay.model.entity.ConsumeRecord;
import com.wechuang.mallshop.pay.model.entity.UserResource;
import com.wechuang.mallshop.pay.model.vo.MoneyVo;
import com.wechuang.mallshop.pay.repository.ConsumeRecordRepository;
import com.wechuang.mallshop.pay.repository.UserResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


/**
 * <p>
 * 用户资源表-资金账户表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Repository
public class UserResourceRepositoryImpl extends BaseRepositoryImpl<UserResourceDao, UserResource> implements UserResourceRepository {
    @Autowired
    private ConsumeRecordRepository consumeRecordRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * 余额操作入口   区分正负数，未负数则减少
     */
    @Override
    @Transactional
    public boolean money(MoneyVo vo) {
        BigDecimal money = vo.getRecordTotal();
        Integer tradeTypeDeposit = vo.getTradeTypeDeposit();

        Date date = new Date();
        DateTime cur_time = DateUtil.parse(DateUtil.format(date, "yyyy-MM-dd"));

        ConsumeRecord consume_record_row = new ConsumeRecord();

        consume_record_row.setOrderId(vo.getOrderId());
        consume_record_row.setUserId(vo.getUserId());
        UserInfo userInfo = userInfoRepository.get(vo.getUserId());
        consume_record_row.setUserNickname(userInfo != null ? userInfo.getUserNickname() : "");
        consume_record_row.setRecordTime(date.getTime());
        consume_record_row.setRecordDate(cur_time);
        consume_record_row.setRecordYear(DateUtil.year(date));
        consume_record_row.setRecordMonth(DateUtil.month(date));
        consume_record_row.setRecordDay(DateUtil.dayOfMonth(date));
        consume_record_row.setRecordDesc(vo.getRecordDesc());

        if (ObjectUtil.equal(StateCode.PAYMENT_TYPE_OFFLINE, vo.getPaymentTypeId())) {
            consume_record_row.setRecordTitle(__("线下"));
        } else {
            consume_record_row.setRecordTitle(__("线上"));
        }

        UserResource userResource = get(vo.getUserId()); // 获取用户当前资源信息
        UserResource needUpdateUserResource = new UserResource();
        needUpdateUserResource.setUserId(vo.getUserId());

        //通用判断
        String title = vo.getRecordDesc() + " " + StateCode.TRADE_TYPE_MAP.get(tradeTypeDeposit);

        switch (vo.getTradeTypeDeposit()) {
            case StateCode.TRADE_TYPE_WITHDRAW:
                BigDecimal user_money_frozen = ObjectUtil.defaultIfNull(userResource.getUserMoneyFrozen(), BigDecimal.ZERO);
                if (ObjectUtil.compare(money, BigDecimal.ZERO) > 0 && ObjectUtil.compare(user_money_frozen, money.negate()) < 0) {
                    throw new BusinessException(__("金额不足！"));
                }
                needUpdateUserResource.setUserMoneyFrozen(NumberUtil.add(user_money_frozen, money));
                break;
            case StateCode.TRADE_TYPE_SHOPPING_CARD:
            case StateCode.TRADE_TYPE_DEPOSIT_CARD:
                BigDecimal user_recharge_card = ObjectUtil.defaultIfNull(userResource.getUserRechargeCard(), BigDecimal.ZERO);
                if (ObjectUtil.compare(money, BigDecimal.ZERO) < 0 && ObjectUtil.compare(user_recharge_card, money.negate()) < 0) {
                    throw new BusinessException(__("金额不足！"));
                }
                needUpdateUserResource.setUserRechargeCard(NumberUtil.add(user_recharge_card, money));
                break;
            case StateCode.TRADE_TYPE_DEPOSIT:
            case StateCode.TRADE_TYPE_TRANSFER:
            case StateCode.TRADE_TYPE_COMMISSION:
            case StateCode.TRADE_TYPE_RETURN_GROUPBOOKING:
            default:
                BigDecimal default_user_money = ObjectUtil.defaultIfNull(userResource.getUserMoney(), BigDecimal.ZERO);
                if (ObjectUtil.compare(money, BigDecimal.ZERO) < 0 && ObjectUtil.compare(default_user_money, money.negate()) < 0) {
                    throw new BusinessException(__("金额不足！"));
                }
                needUpdateUserResource.setUserMoney(NumberUtil.add(default_user_money, money));
                break;
        }

        consume_record_row.setRecordTitle(title);

        if (ObjectUtil.equal(tradeTypeDeposit, StateCode.TRADE_TYPE_DEPOSIT_CARD) || ObjectUtil.equal(tradeTypeDeposit, StateCode.TRADE_TYPE_SHOPPING_CARD)) {
            consume_record_row.setPaymentMetId(StateCode.PAYMENT_MET_RECHARGE_CARD);
        } else {
            consume_record_row.setPaymentMetId(StateCode.PAYMENT_MET_MONEY);
        }
        consume_record_row.setRecordTotal(money);

        if (money.compareTo(BigDecimal.ZERO) > 0) {
            consume_record_row.setRecordMoney(NumberUtil.sub(money, vo.getRecordCommissionFee()));
        } else {
            consume_record_row.setRecordMoney(NumberUtil.add(money, vo.getRecordCommissionFee()));
        }

        consume_record_row.setRecordCommissionFee(vo.getRecordCommissionFee()); // 佣金
        consume_record_row.setTradeTypeId(tradeTypeDeposit);

        // 买家数据
        if (!consumeRecordRepository.add(consume_record_row)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        if (!edit(needUpdateUserResource)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        //余额改变
        String messageId = "balance-change-reminder";
        HashMap<String, Object> args = new HashMap<>();
        args.put("user_money", userResource.getUserMoney());
        args.put("change_time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        args.put("des", vo.getRecordDesc());
        args.put("user_money_freeze", 0);

        messageService.sendNoticeMsg(vo.getUserId(), messageId, args);

        return true;
    }
}
