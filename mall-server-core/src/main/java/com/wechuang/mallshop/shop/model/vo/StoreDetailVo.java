package com.wechuang.mallshop.shop.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.entity.StoreAnalytics;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.StoreCompany;
import com.wechuang.mallshop.shop.model.entity.StoreInfo;
import com.wechuang.mallshop.sys.model.vo.PagePopUpVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "")
public class StoreDetailVo {

    @Schema(description = "是否收藏")
    private Boolean isFavorite;

    @Schema(description = "店铺基础信息")
    private StoreBase storeBase;

    @Schema(description = "店铺基础信息")
    private StoreInfo storeInfo;

    @Schema(description = "店铺基础信息")
    private StoreAnalytics storeAnalytics;

    @Schema(description = "店铺公司信息")
    private StoreCompany storeCompany;

    @Schema(description = "弹窗集合")
    private List<PagePopUpVo> popUps;

}
