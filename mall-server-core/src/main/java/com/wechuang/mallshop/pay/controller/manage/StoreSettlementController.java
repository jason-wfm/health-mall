package com.wechuang.mallshop.pay.controller.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.StoreSettlement;
import com.wechuang.mallshop.pay.model.req.StoreSettlementListReq;
import com.wechuang.mallshop.pay.service.StoreSettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 商户结算单 前端控制器
 * </p>
 * 权限点为 /manage/pay/settlement/* (V0_005 菜单数据初始化,1005商家角色含 list/get/confirm/reject);
 * 商家数据范围由Service层归属校验+store_id拦截器双重保证
 *
 * @author jason
 * @since 2026-09-18
 */
@Tag(name = "商户结算单")
@RestController
@RequestMapping("/manage/pay/settlement")
public class StoreSettlementController extends BaseController {

    @Autowired
    private StoreSettlementService storeSettlementService;

    @PreAuthorize("hasAuthority('/manage/pay/settlement/list')")
    @Operation(summary = "结算单-分页列表", description = "商家只看本店;平台可按店铺/状态筛选")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StoreSettlement>> list(StoreSettlementListReq req) {
        return success(storeSettlementService.getList(req));
    }

    @PreAuthorize("hasAuthority('/manage/pay/settlement/get')")
    @Operation(summary = "结算单-详情(头+明细)", description = "商家校验归属")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<Map<String, Object>> get(@RequestParam("settlement_id") Long settlementId) {
        return success(storeSettlementService.getSettlementDetail(settlementId));
    }

    @PreAuthorize("hasAuthority('/manage/pay/settlement/confirm')")
    @Operation(summary = "结算单-商家确认", description = "待确认→已确认待出金")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public CommonRes<?> confirm(@RequestParam("settlement_id") Long settlementId) {
        if (storeSettlementService.confirmSettlement(settlementId)) {
            return success();
        }
        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/settlement/reject')")
    @Operation(summary = "结算单-商家驳回", description = "待确认→已驳回,订单回滚进入下一期")
    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public CommonRes<?> reject(@RequestParam("settlement_id") Long settlementId) {
        if (storeSettlementService.rejectSettlement(settlementId)) {
            return success();
        }
        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/settlement/pay')")
    @Operation(summary = "结算单-平台出金", description = "已确认→出金中;创建提现申请走既有提现审核/微信商家转账链路")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public CommonRes<?> pay(@RequestParam("settlement_id") Long settlementId,
                            @RequestParam("user_bank_id") Integer userBankId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (storeSettlementService.paySettlement(settlementId, userBankId)) {
            return success();
        }
        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/settlement/generate')")
    @Operation(summary = "结算单-手动触发生成", description = "平台专属;与每周定时任务同一入口,幂等")
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public CommonRes<?> generate() {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (storeSettlementService.generateSettlements()) {
            return success();
        }
        return fail();
    }
}
