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
public class ProductCommentTemp {

    @ExcelProperty(index = 0, value = "*产品编号")
    private Long productId;

    @ExcelProperty(index = 1, value = "*买家姓名")
    private String userName;

    @ExcelProperty(index = 2, value = "*评价内容")
    private String commentContent;

}
