package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "电子面单对象")
public class SenderVo implements Serializable {

    @Schema(description = "发件人公司")
    private String Company;

    @Schema(description = "发件人")
    private String Name;

    @Schema(description = "手机号")
    private String Mobile;

    @Schema(description = "省")
    private String ProvinceName;

    @Schema(description = "市")
    private String CityName;

    @Schema(description = "县")
    private String ExpAreaName;

    @Schema(description = "详细地址")
    private String Address;

}
