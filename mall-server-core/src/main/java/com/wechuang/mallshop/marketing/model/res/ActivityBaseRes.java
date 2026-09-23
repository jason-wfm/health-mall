package com.wechuang.mallshop.marketing.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.vo.ActivityRuleVo;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
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
@Schema(name = "优惠券列表")
public class ActivityBaseRes extends ActivityBase {

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "活动规则(JSON):不检索{rule_id:{}, rule_id:{}},统一解析规则{\"requirement\":{\"buy\":{\"item\":[1,2,3],\"subtotal\":\"通过计算修正满足的条件\"}},\"rule\":[{\"total\":100,\"max_num\":1,\"item\":{\"1\":1,\"1200\":3}},{\"total\":200,\"max_num\":1,\"item\":{\"1\":1,\"1200\":3}}]}")
    private ActivityRuleVo activityRuleJson;

    @Schema(description = "是否领取")
    private Boolean ifGain;

    @Schema(description = "优惠套装数量")
    private Integer itemNumber;

    @Schema(description = "商品信息集合")
    private List<ProductItemVo> item;

    @Schema(description = "会员等级")
    private String useLevel;

    @Schema(description = "折扣商品")
    private String productItemName;

    @Schema(description = "使用等级名称(DOT)")
    private String activityUseLevelName;

    @Schema(description = "剩余库存")
    private Integer remainQuantity;

    @Schema(description = "活动规则全体")
    private List<String> activityRules;

}
