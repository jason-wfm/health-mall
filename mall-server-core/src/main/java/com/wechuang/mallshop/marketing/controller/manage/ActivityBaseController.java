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
package com.wechuang.mallshop.marketing.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.marketing.model.entity.*;
import com.wechuang.mallshop.marketing.model.req.*;
import com.wechuang.mallshop.marketing.model.res.ActivityBaseRes;
import com.wechuang.mallshop.marketing.model.res.ActivityItemRes;
import com.wechuang.mallshop.marketing.model.res.ActivityRuleItemRes;
import com.wechuang.mallshop.marketing.model.res.ActivityRuleRes;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 活动表-通过插件实现	当为拼团是activity_rule中的group_remain_quantity用于标识拼团剩余需要人数，如果用户登录了，需要查询出activity_groupbooking中参与该团的剩余情况 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-06-29
 */
@Tag(name = "活动表-通过插件实现	当为拼团是activity_rule中的group_remain_quantity用于标识拼团剩余需要人数，如果用户登录了，需要查询出activity_groupbooking中参与该团的剩余情况")
@RestController
@RequestMapping("/manage/marketing/activityBase")
public class ActivityBaseController extends BaseController {
    @Autowired
    private ActivityBaseService activityBaseService;

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/list')")
    @Operation(summary = "活动分页列表查询", description = "活动分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ActivityBaseRes>> list(ActivityBaseListReq activityBaseListReq) {
        IPage<ActivityBaseRes> pageList = activityBaseService.getList(activityBaseListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/detail')")
    @Operation(summary = "活动通过activity_id查询", description = "活动通过activity_id查询")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<ActivityBase> get(@RequestParam("activity_id") Integer activityId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ActivityBase activityBase = activityBaseService.get(activityId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {

            return success(activityBase);
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/add')")
    @Operation(summary = "活动添加", description = "活动添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(ActivityBaseAddReq activityBaseAddReq) {
        ActivityBase activityBase = BeanUtil.copyProperties(activityBaseAddReq, ActivityBase.class);
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        activityBase.setUserId(user.getUserId());
        activityBase.setStoreId(user.getStoreId());
        activityBase.setSubsiteId(user.getSiteId());
        boolean success = activityBaseService.addActivityBase(activityBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/edit')")
    @Operation(summary = "活动表-编辑", description = "活动表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(ActivityBaseEditReq activityBaseEditReq) {
        ActivityBase activityBase = BeanUtil.copyProperties(activityBaseEditReq, ActivityBase.class);
        boolean success = activityBaseService.updateActivityBase(activityBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/edit')")
    @Operation(summary = "活动编辑", description = "活动编辑")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(ActivityBaseEditReq activityBaseEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ActivityBase activity = activityBaseService.get(activityBaseEditReq.getActivityId());

        if (activity == null) {
            throw new BusinessException(__("该活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activity.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activity, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        activity.setActivityState(activityBaseEditReq.getActivityState());
        boolean success = activityBaseService.editActivityBase(activityBaseEditReq.getActivityId(), activity);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/remove')")
    @Operation(summary = "活动通过activity_id删除", description = "活动通过activity_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("activity_id") Integer activityId) {
        boolean success = activityBaseService.removeActivity(activityId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/list')")
    @Operation(summary = "活动商品列表", description = "活动商品列表")
    @RequestMapping(value = "/getActivityBuyItems", method = RequestMethod.GET)
    public CommonRes<List<ActivityItemRes>> getActivityBuyItems(@RequestParam("activity_id") Integer activityId) {
        List<ActivityItemRes> activityItemResList = activityBaseService.getActivityBuyItems(activityId);

        return success(activityItemResList);
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/list')")
    @Operation(summary = "活动规则列表", description = "活动规则列表")
    @RequestMapping(value = "/getActivityRuleList", method = RequestMethod.GET)
    public CommonRes<List<ActivityRuleRes>> getActivityRuleList(@RequestParam("activity_id") Integer activityId) {
        List<ActivityRuleRes> list = activityBaseService.getActivityRuleList(activityId);

        return success(list);
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/add')")
    @Operation(summary = "活动-添加规则", description = "活动-添加规则")
    @RequestMapping(value = "/addActivityRule", method = RequestMethod.POST)
    public CommonRes<?> addActivityRule(@RequestParam("activity_id") Integer activityId) {
        boolean success = activityBaseService.addActivityRule(activityId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/edit')")
    @Operation(summary = "活动-修改规则", description = "活动-修改规则")
    @RequestMapping(value = "/editActivityRule", method = RequestMethod.POST)
    public CommonRes<?> editActivityRule(ActivityRuleReq activityRuleReq) {
        boolean success = activityBaseService.editActivityRule(activityRuleReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/remove')")
    @Operation(summary = "活动-删除规则", description = "活动-删除规则")
    @RequestMapping(value = "/removeActivityRule", method = RequestMethod.POST)
    public CommonRes<?> removeActivityRule(ActivityRuleReq activityRuleReq) {
        boolean success = activityBaseService.removeActivityRule(activityRuleReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/list')")
    @Operation(summary = "活动规则商品列表", description = "活动规则商品列表")
    @RequestMapping(value = "/getActivityRuleItemList", method = RequestMethod.GET)
    public CommonRes<List<ActivityRuleItemRes>> getActivityRuleItemList(ActivityRuleItemReq activityRuleItemReq) {
        List<ActivityRuleItemRes> list = activityBaseService.getActivityRuleItemList(activityRuleItemReq);

        return success(list);
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/add')")
    @Operation(summary = "活动-添加规则商品", description = "活动-添加规则商品")
    @RequestMapping(value = "/addActivityRuleItem", method = RequestMethod.POST)
    public CommonRes<?> addActivityRuleItem(ActivityRuleItemReq activityRuleItemReq) {
        boolean success = activityBaseService.addActivityRuleItem(activityRuleItemReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/remove')")
    @Operation(summary = "活动-删除规则商品", description = "活动-删除规则商品")
    @RequestMapping(value = "/removeActivityRuleItem", method = RequestMethod.POST)
    public CommonRes<?> removeActivityRuleItem(ActivityRuleItemReq activityRuleItemReq) {
        boolean success = activityBaseService.removeActivityRuleItem(activityRuleItemReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @RequestMapping(value = "/cellEditReplace", method = RequestMethod.POST)
    public CommonRes<?> cellEditRule(@RequestParam(name = "activity_id") Integer activityId,
                                     @RequestParam(name = "total_id") int totalId,
                                     @RequestParam(name = "item_id") Long itemId,
                                     @RequestParam(name = "item_replace_price") BigDecimal itemReplacePrice) {
        Boolean success = activityBaseService.cellEditReplace(activityId, totalId, itemId, itemReplacePrice);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/add')")
    @Operation(summary = "活动-添加商品", description = "活动-添加商品")
    @RequestMapping(value = "/addActivityBuyItems", method = RequestMethod.POST)
    public CommonRes<?> addActivityBuyItems(ActivityBaseEditReq activityBaseEditReq) {
        boolean success = activityBaseService.addActivityBuyItems(activityBaseEditReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/remove')")
    @Operation(summary = "活动-删除商品", description = "活动-删除商品")
    @RequestMapping(value = "/removeActivityBuyItems", method = RequestMethod.POST)
    public CommonRes<?> removeActivityBuyItems(ActivityBaseEditReq activityBaseEditReq) {
        boolean success = activityBaseService.removeActivityBuyItems(activityBaseEditReq);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/edit')")
    @Operation(summary = "活动-修改商品活动价格", description = "活动-修改商品活动价格")
    @RequestMapping(value = "/editActivityItem", method = RequestMethod.POST)
    public CommonRes<?> editActivityItem(ActivityItemEditReq activityItemEditReq) {
        ActivityItem activityItem = BeanUtil.copyProperties(activityItemEditReq, ActivityItem.class);
        boolean success = activityBaseService.editActivityItem(activityItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/marketing/activityBase/edit')")
    @Operation(summary = "活动-统一折扣修改价格", description = "活动-统一折扣修改价格")
    @RequestMapping(value = "/editBatchPrice", method = RequestMethod.POST)
    public CommonRes<?> editBatchPrice(ActivityItemBatchPriceEditReq activityItemBatchPriceEditReq) {
        boolean success = activityBaseService.editBatchPrice(activityItemBatchPriceEditReq);

        if (success) {
            return success();
        }

        return fail();
    }
}

