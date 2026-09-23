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
package com.wechuang.mallshop.pay.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.ConsumeDeposit;
import com.wechuang.mallshop.pay.model.req.ConsumeDepositListReq;
import com.wechuang.mallshop.pay.model.req.ConsumeDepositOfflinePayReq;
import com.wechuang.mallshop.pay.model.req.ConsumeDepositReviewEditReq;
import com.wechuang.mallshop.pay.service.ConsumeDepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 支付表-支付回调callback使用-确认付款 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Tag(name = "支付表-支付回调callback使用-确认付款")
@RestController
@RequestMapping("/manage/pay/consumeDeposit")
public class ConsumeDepositController extends BaseController {
    @Autowired
    private ConsumeDepositService consumeDepositService;

    @PreAuthorize("hasAuthority('/manage/pay/consumeDeposit/list')")
    @Operation(summary = "支付表-支付回调callback使用-确认付款-分页列表查询", description = "支付表-支付回调callback使用-确认付款-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ConsumeDeposit>> list(ConsumeDepositListReq consumeDepositListReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser != null) {
            Integer chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(chainId)) {
                consumeDepositListReq.setChainId(chainId);
            }
        }
        QueryWrapper<ConsumeDeposit> wrapper = new BaseQueryWrapper<ConsumeDeposit, ConsumeDepositListReq>(consumeDepositListReq).getWrapper();

        if (CheckUtil.isNotEmpty(consumeDepositListReq.getIsOffline())) {
            if (consumeDepositListReq.getIsOffline().equals(1)) {
                wrapper.eq("deposit_payment_type", StateCode.PAYMENT_TYPE_BANK);
            } else {
                wrapper.ne("deposit_payment_type", StateCode.PAYMENT_TYPE_BANK);
            }
        }
        IPage<ConsumeDeposit> pageList = consumeDepositService.lists(wrapper, consumeDepositListReq.getPage(), consumeDepositListReq.getSize());

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pay/consumeDeposit/editReview')")
    @Operation(summary = "支付表-收款确认", description = "支付表-收款确认")
    @RequestMapping(value = "/editReview", method = RequestMethod.POST)
    public CommonRes<?> editReview(ConsumeDepositReviewEditReq consumeDepositReviewEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ConsumeDeposit deposit = consumeDepositService.get(consumeDepositReviewEditReq.getDepositId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), deposit, ConsumeDeposit::getStoreId)) {
            ConsumeDeposit consumeDeposit = BeanUtil.copyProperties(consumeDepositReviewEditReq, ConsumeDeposit.class);
            consumeDeposit.setDepositExamineTime(new Date().getTime());

            boolean success = consumeDepositService.edit(consumeDeposit);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/finance')")
    @Operation(summary = "线下支付接口", description = "线下支付接口")
    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    public CommonRes<?> offlinePay(ConsumeDepositOfflinePayReq req) {
        ConsumeDeposit consumeDeposit = BeanUtil.copyProperties(req, ConsumeDeposit.class);

        //交易号 == 流水号
        consumeDeposit.setDepositNo(req.getDepositTradeNo());
        //平台/商家支付 默认已确认
        consumeDeposit.setDepositReview(1);

        List<ConsumeDeposit> deposits = consumeDepositService.find(new QueryWrapper<ConsumeDeposit>().eq("deposit_trade_no", consumeDeposit.getDepositTradeNo()));
        if (CollUtil.isNotEmpty(deposits)) {
            return fail(__("支付凭证号已经存在！"));
        }


        Long deposit_id = consumeDepositService.offlinePay(consumeDeposit);

        if (CheckUtil.isNotEmpty(deposit_id)) {
            return success(deposit_id);
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/consumeDeposit/orderPayReview')")
    @Operation(summary = "银行打款审核", description = "银行打款审核")
    @RequestMapping(value = "/orderPayReview", method = RequestMethod.POST)
    public CommonRes<?> orderPayReview(ConsumeDepositReviewEditReq consumeDepositReviewEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ConsumeDeposit deposit = consumeDepositService.get(consumeDepositReviewEditReq.getDepositId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), deposit, ConsumeDeposit::getStoreId)) {
            ConsumeDeposit consumeDeposit = BeanUtil.copyProperties(consumeDepositReviewEditReq, ConsumeDeposit.class);

            boolean success = consumeDepositService.orderPayReview(consumeDeposit);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

