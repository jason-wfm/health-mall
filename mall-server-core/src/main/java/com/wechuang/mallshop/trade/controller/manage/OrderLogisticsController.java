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
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.entity.OrderLogistics;
import com.wechuang.mallshop.trade.model.req.OrderLogisticsAddReq;
import com.wechuang.mallshop.trade.model.req.OrderLogisticsEditReq;
import com.wechuang.mallshop.trade.model.req.OrderLogisticsListReq;
import com.wechuang.mallshop.trade.repository.OrderBaseRepository;
import com.wechuang.mallshop.trade.service.OrderLogisticsService;
import com.wechuang.mallshop.trade.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 订单发货物流信息表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Tag(name = "订单发货物流信息表")
@RestController
@RequestMapping("/manage/trade/orderLogistics")
public class OrderLogisticsController extends BaseController {
    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    @Autowired
    private OrderService orderService;

    @Operation(summary = "订单发货物流信息表-分页列表查询", description = "订单发货物流信息表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<OrderLogistics>> list(OrderLogisticsListReq orderLogisticsListReq) {
        IPage<OrderLogistics> pageList = orderLogisticsService.lists(orderLogisticsListReq);

        return success(pageList);
    }

    @Operation(summary = "订单发货物流信息表-通过order_logistics_id查询", description = "订单发货物流信息表-通过order_logistics_id查询")
    @RequestMapping(value = "/{orderLogisticsId}", method = RequestMethod.GET)
    public CommonRes<OrderLogistics> get(@PathVariable Long orderLogisticsId) {
        OrderLogistics orderLogistics = orderLogisticsService.get(orderLogisticsId);

        return success(orderLogistics);
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/shipping')")
    @Operation(summary = "订单发货物流信息表-添加", description = "订单发货物流信息表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(OrderLogisticsAddReq orderLogisticsAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderBase orderBase = orderBaseRepository.get(orderLogisticsAddReq.getOrderId());

        if (orderBase == null) {
            throw new BusinessException("该订单信息不存在！");
        }

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
            OrderLogistics orderLogistics = BeanUtil.copyProperties(orderLogisticsAddReq, OrderLogistics.class);
            boolean success = orderService.addLogistics(orderLogistics);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/trade/orderBase/shipping')")
    @Operation(summary = "订单发货物流信息表-编辑", description = "订单发货物流信息表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(OrderLogisticsEditReq orderLogisticsEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        OrderBase orderBase = orderBaseRepository.get(orderLogisticsEditReq.getOrderId());

        if (orderBase == null) {
            throw new BusinessException("该订单信息不存在！");
        }

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), orderBase, OrderBase::getStoreId)) {
            OrderLogistics orderLogistics = BeanUtil.copyProperties(orderLogisticsEditReq, OrderLogistics.class);
            boolean success = orderService.saveLogistics(orderLogistics);

            orderService.checkShippingComplete(orderLogistics.getOrderId());

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

