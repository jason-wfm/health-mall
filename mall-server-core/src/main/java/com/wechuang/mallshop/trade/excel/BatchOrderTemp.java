package com.wechuang.mallshop.trade.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@HeadRowHeight(35)
@ColumnWidth(value = 25)
public class BatchOrderTemp {

    @ExcelProperty(index = 0, value = "*商品编码")
    private String itemNumber;

    @ExcelProperty(index = 1, value = "*商品数量")
    private Integer cartQuantity;


}
