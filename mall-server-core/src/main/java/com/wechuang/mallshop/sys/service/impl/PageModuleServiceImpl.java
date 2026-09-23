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
package com.wechuang.mallshop.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.MapUtil;
import com.wechuang.mallshop.core.web.service.CloundService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.model.entity.ProductBrand;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.output.ItemOutput;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.repository.ProductBrandRepository;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.model.entity.PageModule;
import com.wechuang.mallshop.sys.model.req.PageModuleListReq;
import com.wechuang.mallshop.sys.model.vo.PageModuleVo;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import com.wechuang.mallshop.sys.repository.PageModuleRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.PageModuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 页面模块表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Service
public class PageModuleServiceImpl extends BaseServiceImpl<PageModuleRepository, PageModule, PageModuleListReq> implements PageModuleService {
    @Autowired
    private CloundService cloundService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private ProductIndexService productIndexService;

    @Autowired
    private LangMetaRepository langMetaRepository;


    @Override
    public Map getModuleTpl() {
        Map app_row = null;

        try {
            ConfigBase configBaseUserId = configBaseService.get("service_user_id");
            ConfigBase configBaseAppKey = configBaseService.get("service_app_key");
            app_row = cloundService.getModuleTpl(Convert.toInt(configBaseUserId.getConfigValue()), configBaseAppKey.getConfigValue());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        List items = (List) app_row.get("items");
        // 带登录的轮播
        Map indexShufflingMap = new HashMap<>();
        indexShufflingMap.put("module_id", 8888);
        indexShufflingMap.put("module_name", "带登录的轮播");
        String module_config = "{\"setting\":{\"showMegamenus\":0},\"banner1\":{\"name\":\"FURNITURE\",\"url\":\"//test.shopsuite.cn/\",\"picimg\":\"//static.shopsuite.cn/pagepreview/data/01_210x210.png\",\"imgSize\":\"210x210\"},\"banner2\":{\"name\":\"FURNITURE\",\"url\":\"//test.shopsuite.cn/\",\"picimg\":\"//static.shopsuite.cn/pagepreview/data/01_210x210.png\",\"imgSize\":\"210x210\"},\"items\":[{\"url\":\"//test.shopsuite.cn/index.php/Product/detail/item_id/1\",\"picimg\":\"//static.shopsuite.cn/pagepreview/data/01_1920x453.png\",\"name\":\"商品信息\",\"imgSize\":\"1920x453\",\"bgColor\":\"#fff\"},{\"url\":\"//test.shopsuite.cn/index.php/Product/detail/item_id/1\",\"picimg\":\"//static.shopsuite.cn/pagepreview/data/02_1920x453.png\",\"name\":\"商品信息\",\"imgSize\":\"1920x453\",\"bgColor\":\"#fff\"}]}";
        indexShufflingMap.put("module_config", module_config);
        indexShufflingMap.put("module_preview", "https://shopsuite.oss-accelerate.aliyuncs.com/mall/images/media/plantform/20230521/3e84c04b07fc4d4cb9e0ec4dc6d1b00f.png");
        items.add(indexShufflingMap);

        return app_row;
    }

    @Override
    public IPage<PageModuleVo> getLists(PageModuleListReq pageModuleListReq) {
        QueryWrapper<PageModule> pageModuleQueryWrapper = new QueryWrapper<>();
        pageModuleQueryWrapper.eq("page_id", pageModuleListReq.getPageId());
        IPage<PageModule> modulePage = lists(pageModuleQueryWrapper, 1, ConstantConfig.MAX_LIST_NUM);
        IPage<PageModuleVo> pageModuleVo = new Page<>();
        BeanUtils.copyProperties(modulePage, pageModuleVo);

        List<PageModuleVo> pageModuleVos = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(modulePage.getRecords())) {
            for (PageModule pageModule : modulePage.getRecords()) {
                PageModuleVo moduleVo = new PageModuleVo();
                BeanUtils.copyProperties(pageModule, moduleVo);
                JSON parse = JSONUtil.parse(pageModule.getPmJson());
                moduleVo.setPmJson(parse);
                pageModuleVos.add(moduleVo);
            }
            pageModuleVo.setRecords(pageModuleVos);
        }

        return pageModuleVo;
    }

    @Override
    public List<Map> fixPcPageModuleData(List<PageModule> page_data) {
        String to = ContextUtil.getToLang();

        List<Map> data = new ArrayList<>();
        if (CollUtil.isNotEmpty(page_data)) {
            for (PageModule module_row : page_data) {
                Map moduleDefault = Convert.toMap(String.class, Object.class, module_row);
                Map<String, Object> module = MapUtil.keyToUnderline(moduleDefault);
                data.add(module);

                String module_id = module_row.getModuleId();
                JSONObject pm_json = null;
                try {
                    pm_json = JSONUtil.parseObj(module_row.getPmJson());
                } catch (Exception e) {
                    // json 解析错误可以忽略（脏数据）
                }

                if (pm_json == null) continue;
                module.put("pm_json", pm_json);

                //系统启用自动翻译功能
                if (to != null) {
                    for (String bannerName : Arrays.asList("banner", "banner1", "banner2", "banner3", "floor")) {
                        JSONObject bannerObj = (JSONObject) pm_json.get(bannerName);
                        if (bannerObj != null) {
                            String name = Convert.toStr(bannerObj.get("name"));
                            if (CheckUtil.isNotEmpty(name)) {
                                bannerObj.set("name", langMetaRepository.getTranslate(name, to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0));
                            }

                            String desc = Convert.toStr(bannerObj.get("desc"));
                            if (CheckUtil.isNotEmpty(desc)) {
                                bannerObj.set("desc", langMetaRepository.getTranslate(desc, to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0));
                            }
                        }
                    }

                    //links items
                    for (String items : Arrays.asList("items", "links")) {
                        Object linksObj = pm_json.get(items);
                        if (linksObj != null) {
                            if (linksObj instanceof JSONObject) {
                            } else if (linksObj instanceof JSONArray) {
                                JSONArray linksArray = (JSONArray) linksObj;

                                for (Object item : linksArray) {
                                    Object nameObj = ((JSONObject) item).get("name");
                                    if (nameObj != null) {
                                        if (!StringUtils.isEmpty(nameObj.toString())) {
                                            String name = nameObj.toString();
                                            ((JSONObject) item).set("name", langMetaRepository.getTranslate(name, to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0));
                                        }
                                    }
                                }
                            }
                        }
                    }


                    JSONObject bannerObj = (JSONObject) pm_json.get("banner4");
                    if (bannerObj != null) {
                        Object itemsObj = bannerObj.get("items");
                        if (itemsObj != null) {
                            if (itemsObj instanceof JSONObject) {
                            } else if (itemsObj instanceof JSONArray) {
                                JSONArray linksArray = (JSONArray) itemsObj;

                                for (Object item : linksArray) {
                                    Object nameObj = ((JSONObject) item).get("desc");
                                    if (nameObj != null) {
                                        if (!StringUtils.isEmpty(nameObj.toString())) {
                                            String name = nameObj.toString();
                                            ((JSONObject) item).set("desc", langMetaRepository.getTranslate(name, to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // "tabs"
                    JSONArray tabsArray = (JSONArray) pm_json.get("tabs");
                    if (tabsArray != null) {
                        for (Object tabObj : tabsArray) {
                            Object itemsObj = ((JSONObject) tabObj).get("items");

                            if (itemsObj != null) {
                                JSONArray linksArray = (JSONArray) itemsObj;

                                for (Object item : linksArray) {
                                    Object nameObj = ((JSONObject) item).get("name");
                                    if (nameObj != null) {
                                        if (!StringUtils.isEmpty(nameObj.toString())) {
                                            String name = nameObj.toString();
                                            ((JSONObject) item).set("name", langMetaRepository.getTranslate(name, to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0));
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                if (Arrays.asList("1001", "1004", "1005", "1006", "3002").contains(module_id)) {
                    // 读取商品
                    JSONArray tabs = (JSONArray) pm_json.get("tabs");
                    if (CollUtil.isNotEmpty(tabs)) {
                        List<Long> item_ids = new ArrayList<>();
                        for (Object tab : tabs) {
                            JSONArray items = (JSONArray) ((JSONObject) tab).get("items");
                            if (CollUtil.isNotEmpty(items)) {
                                List<Long> itemIds = items.stream().map(s -> Convert.toLong(((JSONObject) s).get("item_id"))).distinct().collect(Collectors.toList());
                                item_ids.addAll(itemIds);
                            }
                        }

                        ProductItemInput input = new ProductItemInput();
                        input.setItemId(item_ids);
                        ItemListRes itemListRes = productIndexService.listItem(input);
                        List<ItemOutput> item_rows = itemListRes.getItems();

                        for (Object tab : tabs) {
                            JSONArray items = ObjectUtil.defaultIfNull((JSONArray) ((JSONObject) tab).get("items"), new JSONArray());
                            for (Object item : items) {

                                Long item_id = Convert.toLong(((JSONObject) item).get("item_id"));
                                if (CollUtil.isNotEmpty(item_rows)) {
                                    Optional<ItemOutput> item_row_opl = item_rows.stream().filter(s -> ObjectUtil.equal(item_id, s.getItemId())).findFirst();
                                    ItemOutput item_row = item_row_opl.orElseGet(ItemOutput::new);

                                    ((JSONObject) item).put("item_unit_price", item_row.getItemUnitPrice() == null ? BigDecimal.ZERO : item_row.getItemUnitPrice());
                                    ((JSONObject) item).put("item_market_price", item_row.getItemMarketPrice() == null ? BigDecimal.ZERO : item_row.getItemMarketPrice());
                                }

                                ((JSONObject) item).put("activity_type_id", 0);
                                ((JSONObject) item).put("activity_type_name", 0);
                            }

                        }
                    }
                } else if (StrUtil.equals(module_id, "1104")) {
                    // 读取推荐品牌
                    QueryWrapper<ProductBrand> brandQueryWrapper = new QueryWrapper<>();
                    brandQueryWrapper.eq("brand_recommend", 1)
                            .eq("brand_enable", 1);
                    Page<ProductBrand> brandPage = productBrandRepository.lists(brandQueryWrapper, 1, 30);
                    List<ProductBrand> productBrands = brandPage.getRecords();
                    pm_json.put("brand_rows", productBrands);
                }
            }
        }

        return data;
    }
}
