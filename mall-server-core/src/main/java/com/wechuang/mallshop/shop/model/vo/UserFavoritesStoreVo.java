package com.wechuang.mallshop.shop.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.entity.UserFavoritesStore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "收藏的店铺")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserFavoritesStoreVo extends UserFavoritesStore {

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺Logo")
    private String storeLogo;

}
