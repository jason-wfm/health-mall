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
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.ErrorTypeEnum;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pay.model.entity.ConsumeWithdraw;
import com.wechuang.mallshop.pay.model.req.ConsumeWithdrawEditInvoiceReq;
import com.wechuang.mallshop.pay.model.req.ConsumeWithdrawEditReq;
import com.wechuang.mallshop.pay.model.req.ConsumeWithdrawListReq;
import com.wechuang.mallshop.pay.model.req.ConsumeWithdrawReviewInvoiceReq;
import com.wechuang.mallshop.pay.service.ConsumeWithdrawService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.MessageTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 提现申请表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-09-20
 */
@Tag(name = "提现申请表")
@RestController
@RequestMapping("/manage/pay/consumeWithdraw")
public class ConsumeWithdrawController extends BaseController {
    @Autowired
    private ConsumeWithdrawService consumeWithdrawService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @PreAuthorize("hasAuthority('/manage/pay/consumeWithdraw/list')")
    @Operation(summary = "提现申请表-分页列表查询", description = "提现申请表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ConsumeWithdraw>> list(ConsumeWithdrawListReq consumeWithdrawListReq) {
        IPage<ConsumeWithdraw> pageList = consumeWithdrawService.getList(consumeWithdrawListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/pay/consumeWithdraw/edit')")
    @Operation(summary = "提现申请表-编辑", description = "提现申请表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(ConsumeWithdrawEditReq consumeWithdrawEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        ConsumeWithdraw consumeWithdraw = BeanUtil.copyProperties(consumeWithdrawEditReq, ConsumeWithdraw.class);
        Integer userId = ContextUtil.checkLoginUserId();
        consumeWithdraw.setWithdrawUserId(userId);
        boolean success = consumeWithdrawService.editWithdraw(consumeWithdraw);

        if (success) {
            //发送提现申请审核通知
            Map<String, Object> msgArgs = new HashMap<>();
            msgArgs.put("withdraw_id", consumeWithdraw.getWithdrawId());
            String verify_str = "";
            if (consumeWithdraw.getWithdrawState().equals(1)) {
                verify_str = "已审核通过";
            } else {
                verify_str = "被驳回";
            }
            msgArgs.put("verify_str", verify_str);
            msgArgs.put("verify_reason", consumeWithdraw.getWithdrawDesc());

            Date date = new Date(consumeWithdraw.getWithdrawOpertime());
            msgArgs.put("date", DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
            msgArgs.put("site_name", configBaseService.getConfig("site_name", "ModulithShop"));

            try {
                messageTemplateService.send(consumeWithdraw.getUserId(), "withdraw-verify-result-remind", msgArgs);
            } catch (Exception e) {
                LogUtil.error(ErrorTypeEnum.ERR_PSUH_MSG.getValue(), e);
            }

            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/consumeWithdraw/list')")
    @Operation(summary = "商家上传发票", description = "商家上传发票")
    @RequestMapping(value = "/uploadInvoice", method = RequestMethod.POST)
    public CommonRes<?> uploadInvoice(ConsumeWithdrawEditInvoiceReq withdrawEditInvoiceReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isStore()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        ConsumeWithdraw consumeWithdraw = BeanUtil.copyProperties(withdrawEditInvoiceReq, ConsumeWithdraw.class);
        boolean success = consumeWithdrawService.uploadInvoice(consumeWithdraw);

        if (success) {

            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/consumeWithdraw/edit')")
    @Operation(summary = "审核发票", description = "审核发票")
    @RequestMapping(value = "/reviewInvoice", method = RequestMethod.POST)
    public CommonRes<?> reviewInvoice(ConsumeWithdrawReviewInvoiceReq withdrawReviewInvoiceReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        ConsumeWithdraw consumeWithdraw = BeanUtil.copyProperties(withdrawReviewInvoiceReq, ConsumeWithdraw.class);
        consumeWithdraw.setWithdrawUserId(loginUser.getUserId());
        boolean success = consumeWithdrawService.reviewInvoice(consumeWithdraw);

        if (success) {

            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/pay/consumeWithdraw/edit')")
    @Operation(summary = "提现申请-微信余额", description = "提现申请-微信余额")
    @RequestMapping(value = "/wechatTransfer", method = RequestMethod.POST)
    public CommonRes<?> wechatTransfer(@RequestParam(name = "withdraw_id") Integer withdrawId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        boolean success = consumeWithdrawService.wechatTransfer(withdrawId, loginUser.getUserId());

        if (success) {
            return success();
        }

        return fail();
    }
}

