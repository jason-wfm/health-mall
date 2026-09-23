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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.model.entity.PageBase;
import com.wechuang.mallshop.sys.model.req.*;
import com.wechuang.mallshop.sys.model.vo.PageDataItemVo;
import com.wechuang.mallshop.sys.model.vo.PageMenuVo;
import com.wechuang.mallshop.sys.model.vo.PageMobileVo;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.PageBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 页面表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-07-03
 */
@Tag(name = "页面表")
@RestController
@RequestMapping("/manage/sys/pageBase")
public class PageBaseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PageBaseController.class);
    @Autowired
    private PageBaseService pageBaseService;

    @Autowired
    private ConfigBaseService configBaseService;

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "页面表-分页列表查询", description = "页面表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<PageBase>> list(PageBaseListReq pageBaseListReq) {
        IPage<PageBase> pageList = pageBaseService.lists(pageBaseListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "手机装修", description = "手机装修")
    @RequestMapping(value = "/listMobile", method = RequestMethod.GET)
    public CommonRes<?> listMobile(PageBaseListReq pageBaseListReq) {
        pageBaseListReq.setSidx("page_id");
        pageBaseListReq.setSize(200);
        IPage<PageBase> pageList = pageBaseService.lists(pageBaseListReq);
        IPage<PageMobileVo> pageMobileVoPage = new Page<>();
        List<PageBase> records = pageList.getRecords();
        List<PageMobileVo> pageMobileVos = new ArrayList<>();

        //BeanUtils.copyProperties(pageList, pageMobileVoPage);
        pageMobileVoPage.setPages(pageList.getPages());
        pageMobileVoPage.setSize(pageList.getSize());
        pageMobileVoPage.setTotal(pageList.getTotal());
        pageMobileVoPage.setCurrent(pageList.getCurrent());

        if (CollectionUtil.isNotEmpty(records)) {
            for (PageBase pageBase : records) {
                PageMobileVo pageMobileVo = new PageMobileVo();
                BeanUtils.copyProperties(pageBase, pageMobileVo);
                pageMobileVos.add(pageMobileVo);
            }
        }

        pageMobileVoPage.setRecords(pageMobileVos);

        //用户中心菜单
        Map menus = new HashMap();
        menus = pageBaseService.getUserCenterMenu();


        PageMobileVo pageMobileVo = new PageMobileVo();
        pageMobileVo.setPageCode(JSONUtil.toJSONString(menus));
        pageMobileVo.setPageId(100l);
        pageMobileVo.setPageName(__("个人中心"));
        pageMobileVo.setAppId(8);

        Map<String, Object> res = Convert.toMap(String.class, Object.class, new BaseListRes<PageMobileVo>(pageMobileVoPage.getRecords()));
        res.put("center", JSONUtil.toJSONString(pageMobileVo));

        return success(res);
    }

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "页面表-添加", description = "页面表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(PageBaseAddReq pageBaseAddReq) {
        PageBase pageBase = BeanUtil.copyProperties(pageBaseAddReq, PageBase.class);
        boolean success = pageBaseService.add(pageBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "页面表-添加", description = "页面表-添加")
    @RequestMapping(value = "/saveMobile", method = RequestMethod.POST)
    public CommonRes<?> saveMobile(MobileEditReq mobileEditReq) {
        String pageListJson = mobileEditReq.getAppPageList();

        List<PageMobileVo> pageMobileVos = new ArrayList<>();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            pageMobileVos = mapper.readValue(pageListJson, new TypeReference<List<PageMobileVo>>() {
            });
        } catch (Exception e) {
            LogUtil.error(ConstantLog.DEFAULT, e);
        }

        for (PageMobileVo vo : pageMobileVos) {
            PageBase pageBase = new PageBase();
            BeanUtils.copyProperties(vo, pageBase);
            pageBaseService.save(pageBase);
        }

        //UsetCenterMenu处理
        //存入config  app_member_center
        ConfigBase configBase = new ConfigBase();
        configBase.setConfigKey("app_member_center");

        //处理被翻译内容

        Map menusMap = pageBaseService.getAllCenterMenu();
        List<PageMenuVo> pageMenuVoList = Convert.toList(PageMenuVo.class, menusMap.get("list"));

        PageMobileVo jsonObject = JSONUtil.parseObject(mobileEditReq.getAppMemberCenter(), PageMobileVo.class);
        String pageCode = jsonObject.getPageCode();
        Map menus = JSONUtil.parseObject(pageCode, Map.class);

        //翻译标签
        List<Map> pageMenuVoListReq = (List<Map>) menus.get("list");
        for (Map it : pageMenuVoListReq) {
            for (PageMenuVo item : pageMenuVoList) {
                if (item.getId().equals(Convert.toInt(it.get("id")))) {
                    it.put("name", item.getName());
                    break;
                }
            }
        }

        jsonObject.setPageCode(JSONUtil.toJSONString(menus));


        configBase.setConfigValue(JSONUtil.toJSONString(jsonObject));
        configBase.setConfigTypeId(0);
        configBase.setConfigDatatype("text");
        configBase.setConfigBuildin(true);
        configBaseService.save(configBase);


        return success();
    }

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "页面表-编辑", description = "页面表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(PageBaseEditReq pageBaseEditReq) {
        PageBase pageBase = BeanUtil.copyProperties(pageBaseEditReq, PageBase.class);
        boolean success = pageBaseService.edit(pageBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "页面表-修改状态", description = "页面表-修改状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(PageBaseStateEditReq pageBaseStateEditReq) {
        PageBase pageBase = BeanUtil.copyProperties(pageBaseStateEditReq, PageBase.class);
        boolean success = pageBaseService.edit(pageBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/diy/index')")
    @Operation(summary = "页面表-通过page_id删除", description = "页面表-通过page_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("page_id") Long pageId) {
        boolean success = pageBaseService.remove(pageId);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "装修数据选择", description = "装修数据选择")
    @RequestMapping(value = "/getDataInfo", method = RequestMethod.GET)
    public CommonRes<BaseListRes<PageDataItemVo>> getDataInfo(PageDataReq pageDataReq) {
        pageDataReq.setSize(8);
        IPage<PageDataItemVo> dataInfo = pageBaseService.getDataInfo(pageDataReq);

        return success(dataInfo);
    }
}

