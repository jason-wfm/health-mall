package com.wechuang.mallshop.pt.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@HeadRowHeight(35)
@ColumnWidth(value = 25)
public class ProductCategoryTemp {

    @ExcelProperty(index = 0, value = "分类父编号")
    private Integer categoryParentId;

    @ExcelProperty(index = 1, value = "*分类名称")
    private String categoryName;

    @ExcelProperty(index = 2, value = "分类图片")
    private String categoryImage;

    @ExcelProperty(index = 3, value = "类型编号")
    private Integer typeId;

    @ExcelProperty(index = 4, value = "分佣比例")
    private BigDecimal categoryCommissionRate;

    @ExcelProperty(index = 5, value = "排序")
    private Integer categorySort;

    @ExcelProperty(index = 6, value = "是否启用(BOOL):0-不显示;1-显示")
    private Boolean categoryIsEnable;

    @ExcelProperty(index = 7, value = "行业编号")
    private Integer industryId;

}
