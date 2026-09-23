package com.wechuang.mallshop.pay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.service.IBaseService;
import com.wechuang.mallshop.pay.model.entity.StoreSettlement;
import com.wechuang.mallshop.pay.model.req.StoreSettlementListReq;

import java.util.Map;

/**
 * <p>
 * 商户结算单 服务类
 * </p>
 *
 * @author jason
 * @since 2026-09-18
 */
public interface StoreSettlementService extends IBaseService<StoreSettlement, StoreSettlementListReq> {

    /**
     * 周期结算单生成（平台态/定时任务调用，无登录上下文）：
     * 扫描"已完成+已支付+过售后期+未结算"的店铺订单，按店铺汇总生成结算单与订单级明细快照，
     * 订单置 order_is_settlemented=2；在途售后的订单排除待下期；重复执行幂等（订单置2后不再进入结算池）
     *
     * @return 是否成功
     */
    boolean generateSettlements();

    /**
     * 商家确认结算单（0→1）
     */
    boolean confirmSettlement(Long settlementId);

    /**
     * 商家驳回结算单（0→4），订单/退款单结算状态回滚为未结算，进入下一期
     */
    boolean rejectSettlement(Long settlementId);

    /**
     * 平台出金（1→2）：按结算单创建提现申请（pay_consume_withdraw, state=0），
     * 实际打款走既有提现审核/微信商家转账链路
     *
     * @param settlementId 结算单编号
     * @param userBankId   商家银行卡编号
     */
    boolean paySettlement(Long settlementId, Integer userBankId);

    /**
     * 结算单详情（头+明细），商家身份校验归属
     */
    Map<String, Object> getSettlementDetail(Long settlementId);

    /**
     * 分页查询（商家身份自动按本店过滤）
     */
    IPage<StoreSettlement> getList(StoreSettlementListReq req);
}
