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
package com.wechuang.mallshop.account.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserCard;
import com.wechuang.mallshop.account.model.req.UserCardAddReq;
import com.wechuang.mallshop.account.model.req.UserCardEditReq;
import com.wechuang.mallshop.account.model.req.UserCardListReq;
import com.wechuang.mallshop.account.service.UserCardService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 用户会员卡 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2024-07-10
 */
@Tag(name = "用户会员卡")
@RestController
@RequestMapping("/manage/account/userCard")
public class UserCardController extends BaseController {
    @Autowired
    private UserCardService userCardService;

    @PreAuthorize("hasAuthority('/manage/account/userCard/list')")
    @Operation(summary = "用户会员卡-分页列表查询", description = "用户会员卡-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserCard>> list(UserCardListReq userCardListReq) {
        IPage<UserCard> pageList = userCardService.getList(userCardListReq);

        return success(pageList);

    }

    @PreAuthorize("hasAuthority('/manage/account/userCard/detail')")
    @Operation(summary = "用户会员卡-通过card_id查询", description = "用户会员卡-通过card_id查询")
    @RequestMapping(value = "/{cardId}", method = RequestMethod.GET)
    public CommonRes<UserCard> get(@PathVariable Integer cardId) {
        UserCard userCard = userCardService.get(cardId);

        return success(userCard);

    }

    @PreAuthorize("hasAuthority('/manage/account/userCard/add')")
    @Operation(summary = "用户会员卡-添加", description = "用户会员卡-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserCardAddReq userCardAddReq) {
        UserCard userCard = BeanUtil.copyProperties(userCardAddReq, UserCard.class);
        long time = new Date().getTime();
        userCard.setCardTime(time);
        boolean success = userCardService.add(userCard);

        if (success) {
            return success();
        }

        return fail();

    }

    @PreAuthorize("hasAuthority('/manage/account/userCard/edit')")
    @Operation(summary = "用户会员卡-编辑", description = "用户会员卡-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserCardEditReq userCardEditReq) {
        UserCard userCard = BeanUtil.copyProperties(userCardEditReq, UserCard.class);

        boolean success = userCardService.edit(userCard);

        if (success) {
            return success();
        }

        return fail();

    }

    @PreAuthorize("hasAuthority('/manage/account/userCard/remove')")
    @Operation(summary = "用户会员卡-通过card_id删除", description = "用户会员卡-通过card_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("card_id") Integer cardId) {
        boolean success = userCardService.remove(cardId);

        if (success) {
            return success();
        }
        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/account/userCard/removeBatch')")
    @Operation(summary = "用户会员卡-批量删除", description = "用户会员卡-批量删除")
    @RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
    public CommonRes<?> removeBatch(@RequestParam("card_id") String cardIds) {
        boolean success = userCardService.remove(Convert.toList(Integer.class, cardIds));

        if (success) {
            return success();
        }

        return fail();
    }
}

