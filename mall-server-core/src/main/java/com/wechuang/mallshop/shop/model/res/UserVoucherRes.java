package com.wechuang.mallshop.shop.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.vo.ActivityRuleVo;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
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
public class UserVoucherRes extends UserVoucher {

    @Schema(description = "代金券编号")
    private Integer id;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "活动规则(JSON):不检索{rule_id:{}, rule_id:{}},统一解析规则{\"requirement\":{\"buy\":{\"item\":[1,2,3],\"subtotal\":\"通过计算修正满足的条件\"}},\"rule\":[{\"total\":100,\"max_num\":1,\"item\":{\"1\":1,\"1200\":3}},{\"total\":200,\"max_num\":1,\"item\":{\"1\":1,\"1200\":3}}]}")
    private ActivityRuleVo activityRuleJson;

    @Schema(description = "单品优惠商品编号(DOT)")
    private List<String> itemIds;

    @Schema(description = "活动状态(ENUM):0-未开启;1-正常;2-已结束;3-管理员关闭;4-商家关闭")
    private Integer activityState;

    @Schema(description = "优惠券是否生效(BOOL): false-未生效;true-生效")
    private Boolean voucherEffect = true;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "优惠券图片")
    private String voucherImage;

    @Schema(description = "适用商品(ENUM):1-全部商品可用;2-指定商品可用")
    private Integer voucherProductLimit;

    @Schema(description = "优惠券指定商品")
    private String voucherItemIds;

    @Schema(description = "每人限领数量")
    private Integer voucherPreQuantity;

    @Schema(description = "用户是否已领取")
    private Boolean hasReceived;

    @Schema(description = "用户可领取")
    private Boolean canReceived;

    @Schema(description = "用户已领取次数")
    private Integer receivedCount;

    @Schema(description = "发放类型(manual/monthly/open)")
    private String grantType;

    @Schema(description = "有效期（天）")
    private Integer validDays;

    @Schema(description = "发放数量")
    private Integer grantValue;

}
