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
package com.wechuang.mallshop.pt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.excel.EasyExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.excel.ProductCategoryListener;
import com.wechuang.mallshop.pt.excel.ProductCategoryTemp;
import com.wechuang.mallshop.pt.model.entity.ProductBrand;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.model.entity.ProductType;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import com.wechuang.mallshop.pt.model.req.ProductCategoryListReq;
import com.wechuang.mallshop.pt.model.res.ProductCategoryFilterRes;
import com.wechuang.mallshop.pt.model.res.ProductCategoryRes;
import com.wechuang.mallshop.pt.repository.*;
import com.wechuang.mallshop.pt.service.ProductCategoryService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryRepository, ProductCategory, ProductCategoryListReq> implements ProductCategoryService {
    @Autowired
    private ProductAssistRepository productAssistRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductBrandRepository productBrandRepository;
    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Cacheable(value = {"productCategoryList"})
    @Override
    public Page<ProductCategory> lists(ProductCategoryListReq a) {
        return super.lists(a);
    }

    /**
     * 获取商品分类树形数据
     *
     * @return
     */
    @Cacheable(value = {"productCategoryTree"})
    @Override
    public List<ProductCategoryRes> getTree(Integer pid, Boolean onlyEnable, String categoryName, String sourceLang, Integer siteId) {
        QueryWrapper<ProductCategory> objectQueryWrapper = new QueryWrapper<>();

        if (CheckUtil.isNotEmpty(categoryName)) {
            objectQueryWrapper.like("category_name", categoryName);
        }

        List<ProductCategory> dataList = repository.find(objectQueryWrapper);

        List<ProductCategoryRes> categoryRes = dataList.stream().filter(s -> {
            if (onlyEnable) {
                return s.getCategoryIsEnable();
            } else {
                return true;
            }
        }).map(data -> {
            ProductCategoryRes productCategoryRes = new ProductCategoryRes();
            BeanUtils.copyProperties(data, productCategoryRes);
            productCategoryRes.setId(data.getCategoryId());
            productCategoryRes.setName(data.getCategoryName());

            return productCategoryRes;
        }).collect(Collectors.toList());

        List<ProductCategoryRes> productCategoryRes = CommonUtil.toTreeData(categoryRes, pid,
                ProductCategoryRes::getCategoryParentId,
                ProductCategoryRes::getCategoryId,
                ProductCategoryRes::setChildren
        );

        if (CheckUtil.isNotEmpty(categoryName)) {
            //或者无上级数据，加入列表。 -- 用户树形搜索展示
            List<Integer> columnIds = CommonUtil.column(categoryRes, ProductCategoryRes::getCategoryId);
            for (ProductCategoryRes categoryItem : categoryRes) {
                if (categoryItem.getCategoryParentId().intValue() != 0 && !columnIds.contains(categoryItem.getCategoryParentId())) {

                    ProductCategoryRes router = BeanUtil.copyProperties(categoryItem, ProductCategoryRes.class);

                    List<ProductCategoryRes> c = CommonUtil.toTreeData(categoryRes, categoryItem.getCategoryId(),
                            ProductCategoryRes::getCategoryParentId,
                            ProductCategoryRes::getCategoryId,
                            ProductCategoryRes::setChildren
                    );

                    router.setChildren(c);

                    productCategoryRes.add(router);
                }
            }
        }


        return productCategoryRes;
    }

    /**
     * 修改商品分类启用状态
     *
     * @param categoryId
     * @param categoryIsEnable
     * @return
     */
    @CacheEvict(value = {"productCategoryTree", "productCategoryIdLeafs", "productCategoryList", "pcLayoutData"}, allEntries = true)
    @Override
    public boolean editState(Integer categoryId, Boolean categoryIsEnable) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(categoryId);
        productCategory.setCategoryIsEnable(categoryIsEnable);
        return edit(productCategory);
    }

    @Cacheable(value = {"productCategoryIdLeafs"})
    @Override
    public List<Integer> getCategoryLeafs(Integer pid, String sourceLang, Integer siteId) {
        List<Integer> ids = new ArrayList<>();

        List<ProductCategoryRes> tree = ((ProductCategoryService) AopContext.currentProxy()).getTree(pid, false, "", sourceLang, siteId);
        //List<ProductCategoryRes> tree = getTree(pid, false, "", sourceLang, siteId);

        CommonUtil.eachTreeData(tree, productCategoryRes -> {
            if (productCategoryRes.getChildren().size() == 0) {
                ids.add(productCategoryRes.getCategoryId());
            }
        }, ProductCategoryRes::getChildren);

        return ids;
    }

    @Override
    public ProductCategoryFilterRes getCategoryFilter(Integer categoryId) {
        ProductCategoryFilterRes output = new ProductCategoryFilterRes();

        //判断是否固定分类读取数据
        if (CheckUtil.isNotEmpty(categoryId)) {
            if (CheckUtil.isNotEmpty(categoryId)) {
                ProductCategory productCategory = repository.get(categoryId);
                output.setInfo(productCategory);

                if (ObjectUtil.isNotEmpty(productCategory)) {
                    //上级分类
                    List<ProductCategory> parentCategoryListById = repository.getParentCategory(categoryId);
                    output.setParent(parentCategoryListById);

                    //下级分类
                    List<ProductCategory> childCategorys = repository.find(new QueryWrapper<ProductCategory>().eq("category_parent_id", categoryId).eq("category_is_enable", true));
                    output.setChildren(childCategorys);

                    //辅助属性
                    ProductType productType = productTypeRepository.get(productCategory.getTypeId());

                    if (ObjectUtil.isNotEmpty(productType)) {
                        List<ProductAssistOutput> assists = productAssistRepository.getAssists(productType.getAssistIds());
                        output.setAssists(assists);

                        //品牌
                        if (CheckUtil.isNotEmpty(productType.getBrandIds())) {
                            List<Integer> brandIds = Convert.toList(Integer.class, productType.getBrandIds());
                            List<ProductBrand> brandList = productBrandRepository.gets(brandIds);
                            output.setBrands(brandList);
                        }
                    }
                }
            }
        }

        return output;
    }

    @CacheEvict(value = {"productCategoryTree", "productCategoryIdLeafs", "productCategoryList", "pcLayoutData"}, allEntries = true)
    @Override
    public boolean add(ProductCategory a) {
        return super.add(a);
    }

    @CacheEvict(value = {"productCategoryTree", "productCategoryIdLeafs", "productCategoryList", "pcLayoutData"}, allEntries = true)
    @Override
    public boolean edit(ProductCategory a) {
        return super.edit(a);
    }

    @CacheEvict(value = {"productCategoryTree", "productCategoryIdLeafs", "productCategoryList", "pcLayoutData"}, allEntries = true)
    @Override
    public boolean remove(Serializable a) {

        //验证当前分类下是否有商品存在
        QueryWrapper<ProductCategory> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("category_parent_id", a);
        List<ProductCategory> childrenList = repository.find(objectQueryWrapper);

        if (childrenList.size() > 0) {
            throw new BusinessException(__("有子级，不允许删除"));
        } else {
            //查询当前所属当前分类的商品
            QueryWrapper<ProductIndex> productIndexQueryWrapper = new QueryWrapper<>();
            productIndexQueryWrapper.eq("category_id", a);

            List<Serializable> productIds = productIndexRepository.findKey(productIndexQueryWrapper);

            if (CollUtil.isNotEmpty(productIds)) {
                throw new BusinessException(String.format(__("商品编号 【%s】使用此分类，不可以删除！"), CollUtil.join(productIds, ",")));
            }
        }

        return super.remove(a);
    }


    @Override
    @Transactional
    public boolean editCategory(ProductCategory productCategory) {
        ProductCategory category = get(productCategory.getCategoryId());

        if (category == null) {
            throw new BusinessException(__("商品分类不存在！"));
        }

        if (!edit(productCategory)) {
            throw new BusinessException(__("修改商品分类失败！"));
        }

        if (configBaseService.ifIndustry()) {

            if (!productCategory.getIndustryIds().equals(category.getIndustryIds())) {
                QueryWrapper<ProductIndex> indexQueryWrapper = new QueryWrapper<>();
                indexQueryWrapper.eq("category_id", productCategory.getCategoryId());
                long count = productIndexRepository.count(indexQueryWrapper);

                if (count > 0) {
                    ProductIndex productIndex = new ProductIndex();
                    productIndex.setIndustryIds(productCategory.getIndustryIds());

                    if (!productIndexRepository.edit(productIndex, indexQueryWrapper)) {
                        throw new BusinessException(__("修改商品行业失败！"));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void exportTemp(HttpServletResponse response) {
        //编码问题
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(__("" +
                    "" +
                    "") + "-" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet writeSheet = EasyExcelUtil.writeSelectedSheet(ProductCategoryTemp.class, 0, "导入商品分类模版");
            excelWriter.write(new ArrayList<ProductCategoryTemp>(), writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(__("导出Excel编码异常"));
        } catch (IOException e) {
            throw new BusinessException(__("导出Excel文件异常"));
        }
    }

    @CacheEvict(value = {"productCategoryTree", "productCategoryIdLeafs", "productCategoryList", "pcLayoutData"}, allEntries = true)
    @Override
    public void importTemp(MultipartFile file) throws Exception {
        AnalysisEventListener productCategoryListener = new ProductCategoryListener();
        Class<?> tempClass = ProductCategoryTemp.class;

        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream)
                // 注册监听器，可以在这里校验字段
                .registerReadListener(productCategoryListener)
                .head(tempClass)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                .doReadSync();
    }
}
