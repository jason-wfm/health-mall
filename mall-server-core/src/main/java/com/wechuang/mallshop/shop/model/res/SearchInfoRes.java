package com.wechuang.mallshop.shop.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.vo.SearchWordVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "搜索记录前端对象")
public class SearchInfoRes implements Serializable {

    @Schema(description = "搜索历史")
    private List<String> searchHistoryWords;

    @Schema(description = "热门搜索词")
    private List<String> searchHotWords;

    @Schema(description = "建议搜索词")
    private SearchWordVo suggestSearchWords;

}