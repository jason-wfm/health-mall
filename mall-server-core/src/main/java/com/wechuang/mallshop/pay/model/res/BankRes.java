package com.wechuang.mallshop.pay.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pay.model.entity.BaseBank;
import com.wechuang.mallshop.pay.model.entity.UserBankCard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@Schema(name = "账号银行集合")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankRes {

    @Schema(description = "结算银行集合")
    private List<BaseBank> bankList;

    @Schema(description = "结算账户集合")
    private List<UserBankCard> userBankList;

}
