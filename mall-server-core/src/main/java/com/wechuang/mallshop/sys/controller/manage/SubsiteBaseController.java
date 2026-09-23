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
package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.SubsiteBase;
import com.wechuang.mallshop.sys.model.req.SubsiteBaseAddReq;
import com.wechuang.mallshop.sys.model.req.SubsiteBaseEditReq;
import com.wechuang.mallshop.sys.model.req.SubsiteBaseListReq;
import com.wechuang.mallshop.sys.model.res.SubsiteBaseRes;
import com.wechuang.mallshop.sys.service.SubsiteBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 城市分站表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2025-01-06
 */
@Tag(name = "城市分站表")
@RestController
@RequestMapping("/manage/sys/subsiteBase")
public class SubsiteBaseController extends BaseController {
    @Autowired
    private SubsiteBaseService subsiteBaseService;

    @PreAuthorize("hasAuthority('/manage/sys/subsiteBase/list')")
    @Operation(summary = "城市分站表-分页列表查询", description = "城市分站表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<SubsiteBaseRes>> list(SubsiteBaseListReq subsiteBaseListReq) {
        IPage<SubsiteBaseRes> pageList = subsiteBaseService.getList(subsiteBaseListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/subsiteBase/detail')")
    @Operation(summary = "城市分站表-通过subsite_id查询", description = "城市分站表-通过subsite_id查询")
    @RequestMapping(value = "/{subsiteId}", method = RequestMethod.GET)
    public CommonRes<SubsiteBase> get(@PathVariable Integer subsiteId) {
        SubsiteBase subsiteBase = subsiteBaseService.get(subsiteId);

        return success(subsiteBase);
    }

    @PreAuthorize("hasAuthority('/manage/sys/subsiteBase/add')")
    @Operation(summary = "城市分站表-添加", description = "城市分站表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(SubsiteBaseAddReq subsiteBaseAddReq) {
        SubsiteBase subsiteBase = BeanUtil.copyProperties(subsiteBaseAddReq, SubsiteBase.class);
        boolean success = subsiteBaseService.add(subsiteBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/subsiteBase/edit')")
    @Operation(summary = "城市分站表-编辑", description = "城市分站表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(SubsiteBaseEditReq subsiteBaseEditReq) {
        SubsiteBase subsiteBase = BeanUtil.copyProperties(subsiteBaseEditReq, SubsiteBase.class);
        boolean success = subsiteBaseService.edit(subsiteBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/subsiteBase/remove')")
    @Operation(summary = "城市分站表-通过subsite_id删除", description = "城市分站表-通过subsite_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("subsite_id") Integer subsiteId) {
        boolean success = subsiteBaseService.remove(subsiteId);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/subsiteBase/removeBatch')")
    @Operation(summary = "城市分站表-批量删除", description = "城市分站表-批量删除")
    @RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
    public CommonRes<?> removeBatch(@RequestParam("subsite_id") String subsiteIds) {
        boolean success = subsiteBaseService.remove(Convert.toList(Integer.class, subsiteIds));

        if (success) {
            return success();
        }

        return fail();
    }
}

