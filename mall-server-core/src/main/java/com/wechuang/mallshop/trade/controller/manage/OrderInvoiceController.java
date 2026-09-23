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
package com.wechuang.mallshop.trade.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.trade.model.entity.OrderInvoice;
import com.wechuang.mallshop.trade.model.req.*;
import com.wechuang.mallshop.trade.model.res.OrderInvoiceRes;
import com.wechuang.mallshop.trade.service.OrderInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 订单发票管理表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-05-09
 */
@Tag(name = "订单发票管理表")
@RestController
@RequestMapping("/manage/trade/orderInvoice")
public class OrderInvoiceController extends BaseController {
    @Autowired
    private OrderInvoiceService orderInvoiceService;

    @PreAuthorize("hasAuthority('/manage/trade/orderInvoice/list')")
    @Operation(summary = "订单发票管理表-分页列表查询", description = "订单发票管理表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderInvoiceRes>> list(OrderInvoiceListReq orderInvoiceListReq) {
        ContextUser user = ContextUtil.checkLoginUser();

        if (user.getRoleId().intValue() == ConstantRole.ROLE_SELLER) {
            orderInvoiceListReq.setStoreId(user.getStoreId());
        }

        if (user.getRoleId().intValue() == ConstantRole.ROLE_CHAIN) {
            orderInvoiceListReq.setChainId(user.getChainId());
        }

        IPage<OrderInvoiceRes> pageList = orderInvoiceService.getList(orderInvoiceListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderInvoice/add')")
    @Operation(summary = "订单发票管理表-添加", description = "订单发票管理表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(OrderInvoiceAddReq orderInvoiceAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
            orderInvoiceAddReq.setStoreId(loginUser.getStoreId());
        }

        OrderInvoice orderInvoice = BeanUtil.copyProperties(orderInvoiceAddReq, OrderInvoice.class);
        boolean success = orderInvoiceService.add(orderInvoice);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderInvoice/edit')")
    @Operation(summary = "订单发票管理表-编辑", description = "订单发票管理表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(OrderInvoiceEditReq orderInvoiceEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderInvoice invoice = orderInvoiceService.get(orderInvoiceEditReq.getOrderInvoiceId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), invoice, OrderInvoice::getStoreId)) {
            OrderInvoice orderInvoice = BeanUtil.copyProperties(orderInvoiceEditReq, OrderInvoice.class);
            boolean success = orderInvoiceService.edit(orderInvoice);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderInvoice/editState')")
    @Operation(summary = "订单发票管理表-状态", description = "订单发票管理表-状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(OrderInvoiceStateEditReq orderInvoiceStateEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderInvoice invoice = orderInvoiceService.get(orderInvoiceStateEditReq.getOrderInvoiceId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), invoice, OrderInvoice::getStoreId)) {
            OrderInvoice orderInvoice = BeanUtil.copyProperties(orderInvoiceStateEditReq, OrderInvoice.class);
            boolean success = orderInvoiceService.edit(orderInvoice);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderInvoice/editStatus')")
    @Operation(summary = "订单发票管理表-开票", description = "订单发票管理表-开票")
    @RequestMapping(value = "/editStatus", method = RequestMethod.POST)
    public CommonRes<?> editStatus(OrderInvoiceStatusEditReq orderInvoiceStatusEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderInvoice invoice = orderInvoiceService.get(orderInvoiceStatusEditReq.getOrderInvoiceId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), invoice, OrderInvoice::getStoreId)) {
            OrderInvoice orderInvoice = BeanUtil.copyProperties(orderInvoiceStatusEditReq, OrderInvoice.class);

            if (orderInvoice.getInvoiceStatus() == null) {
                throw new BusinessException("开票状态不能为空！");
            }

            if (orderInvoice.getInvoiceStatus().equals(0)) {
                orderInvoice.setInvoiceImg("");
                orderInvoice.setInvoiceStatusRemark("");
                orderInvoice.setInvoiceDatetime(0L);
            } else if (orderInvoice.getInvoiceStatus().equals(1)) {
                orderInvoice.setInvoiceIsElectronic(1);
                orderInvoice.setInvoiceDatetime(new Date().getTime());
                orderInvoice.setInvoiceStatusRemark("");
            } else if (orderInvoice.getInvoiceStatus().equals(2)) {
                orderInvoice.setInvoiceImg("");
                orderInvoice.setInvoiceDatetime(0L);
            }

            boolean success = orderInvoiceService.edit(orderInvoice);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderInvoice/remove')")
    @Operation(summary = "订单发票管理表-通过order_invoice_id删除", description = "订单发票管理表-通过order_invoice_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("order_invoice_id") Integer orderInvoiceId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        OrderInvoice invoice = orderInvoiceService.get(orderInvoiceId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), invoice, OrderInvoice::getStoreId)) {
            boolean success = orderInvoiceService.remove(orderInvoiceId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

