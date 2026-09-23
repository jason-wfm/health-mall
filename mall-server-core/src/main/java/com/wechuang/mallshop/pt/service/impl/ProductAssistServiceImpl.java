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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.excel.EasyExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.excel.ProductAssistListener;
import com.wechuang.mallshop.pt.excel.ProductAssistTemp;
import com.wechuang.mallshop.pt.model.entity.ProductAssist;
import com.wechuang.mallshop.pt.model.entity.ProductAssistItem;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.model.req.ProductAssistListReq;
import com.wechuang.mallshop.pt.model.res.ProductAssistRes;
import com.wechuang.mallshop.pt.repository.ProductAssistItemRepository;
import com.wechuang.mallshop.pt.repository.ProductAssistRepository;
import com.wechuang.mallshop.pt.repository.ProductTypeRepository;
import com.wechuang.mallshop.pt.service.ProductAssistService;
import com.wechuang.mallshop.pt.service.ProductCategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商品辅助属性表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Service
public class ProductAssistServiceImpl extends BaseServiceImpl<ProductAssistRepository, ProductAssist, ProductAssistListReq> implements ProductAssistService {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductAssistItemRepository productAssistItemRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Override
    public boolean remove(Serializable id) {
        ProductAssist productAssist = get(id);

        long count = productAssistItemRepository.count(new LambdaQueryWrapper<ProductAssistItem>().eq(ProductAssistItem::getAssistId, id));
        if (count > 0) {
            throw new BusinessException(String.format(__("有 %d 个属性选项，不可删除！"), count));
        }

        boolean flag = super.remove(id);

        //更新product_type assistsIds
        //productTypeRepository.updateAssistIds(productAssist.getTypeId());
        productTypeRepository.removeAssistIds(productAssist.getAssistId());


        return flag;
    }

    @Override
    public boolean add(ProductAssist a) {
        boolean flag = super.add(a);
        //boolean b = productTypeRepository.updateAssistIds(a.getTypeId());

        return flag;
    }

    /**
     * 获取属性树形数据
     *
     * @return
     */
    @Override
    public List<ProductAssistRes> getTree() {
        List<ProductAssist> assistLists = repository.find(new QueryWrapper<ProductAssist>().orderByAsc("assist_id"));

        List<Integer> categoryIds = CommonUtil.column(assistLists, ProductAssist::getCategoryId);
        List<ProductCategory> categories = productCategoryService.gets(categoryIds);

        List<ProductAssistRes> assistRes = new ArrayList<>();

        for (ProductCategory category : categories) {
            List<ProductAssist> assists = new ArrayList<>();

            for (ProductAssist assist : assistLists) {
                if (assist.getCategoryId().equals(category.getCategoryId())) {
                    assists.add(assist);
                }
            }

            if (assists.size() > 0) {
                ProductAssistRes assistRe = new ProductAssistRes();
                assistRe.setAssistId(category.getCategoryId());
                assistRe.setAssistName(category.getCategoryName());

                assistRe.setChildren(CollUtil.isEmpty(assists) ? null : assists);
                assistRes.add(assistRe);
            }
        }

        return assistRes;
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
            WriteSheet writeSheet = EasyExcelUtil.writeSelectedSheet(ProductAssistTemp.class, 0, "导入商品辅助属性模版");
            excelWriter.write(new ArrayList<ProductAssistTemp>(), writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(__("导出Excel编码异常"));
        } catch (IOException e) {
            throw new BusinessException(__("导出Excel文件异常"));
        }
    }

    @Override
    public void importTemp(MultipartFile file) throws Exception {
        AnalysisEventListener productAssistListener = new ProductAssistListener();
        Class<?> tempClass = ProductAssistTemp.class;

        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream)
                // 注册监听器，可以在这里校验字段
                .registerReadListener(productAssistListener)
                .head(tempClass)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                .doReadSync();
    }
}
