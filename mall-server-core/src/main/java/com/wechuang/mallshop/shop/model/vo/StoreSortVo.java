package com.wechuang.mallshop.shop.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "店铺综合排序")
public class StoreSortVo implements Serializable {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "值")
    private String value;

    @Schema(description = "排序")
    private String index;



    public StoreSortVo(String name, String value, String index) {
        this.name = name;
        this.value = value;
        this.index = index;
    }
}
