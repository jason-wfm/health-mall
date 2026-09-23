package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.vo.ActivityRuleVo;
import com.wechuang.mallshop.pt.model.output.ItemOutput;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemListRes extends BaseListRes<ItemOutput> {
    @Schema(description = "分类辅助属性")
    List<ProductAssistOutput> assists = new ArrayList<>();

    @Schema(description = "活动信息")
    private ActivityBase activityBase;

    @Schema(description = "活动规则(JSON):不检索{rule_id:{}, rule_id:{}},统一解析规则{\"requirement\":{\"buy\":{\"item\":[1,2,3],\"subtotal\":\"通过计算修正满足的条件\"}},\"rule\":[{\"total\":100,\"max_num\":1,\"item\":{\"1\":1,\"1200\":3}},{\"total\":200,\"max_num\":1,\"item\":{\"1\":1,\"1200\":3}}]}")
    private ActivityRuleVo activityRuleJson;
}
