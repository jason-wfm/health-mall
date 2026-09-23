package com.wechuang.mallshop.sys.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.sys.model.entity.PageBase;
import com.wechuang.mallshop.sys.model.vo.ImConfigVo;
import com.wechuang.mallshop.sys.model.vo.PagePopUpVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "页面表")
public class PageBaseRes extends PageBase {
    @Schema(description = "IM配置")
    private ImConfigVo im;

    @Schema(description = "IM配置")
    private boolean pageLoaded;

    @Schema(description = "弹窗集合")
    private List<PagePopUpVo> popUps;
}
