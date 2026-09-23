package com.wechuang.mallshop.sys.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.sys.model.entity.DistrictBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@Schema(name = "地区树形")
public class DistrictBaseRes extends DistrictBase {
    @Schema(description = "地区树形")
    private List<DistrictBaseRes> children;
}
