package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreCreditVo implements Serializable {

    private StoreDeliverycreditVo storeDesccredit;

    private StoreDeliverycreditVo storeServicecredit;

    private StoreDeliverycreditVo storeDeliverycredit;

}
