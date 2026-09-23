package com.wechuang.mallshop.pt.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@HeadRowHeight(35)
@ColumnWidth(value = 25)
public class ProductAssistTemp {

    @ExcelProperty(index = 0, value = "属性名称")
    private String assistName;

    @ExcelProperty(index = 1, value = "属性值(多个值用逗号分隔)")
    private String assistItem;

    @ExcelProperty(index = 2, value = "备注分类编号")
    private Integer categoryId;

    @ExcelProperty(index = 3, value = "排序")
    private Integer assistSort;

}
