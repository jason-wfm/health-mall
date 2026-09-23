package com.wechuang.mallshop.marketing.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.entity.ActivityItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "活动信息")
public class ActivityInfoVo extends ActivityItem {

    @Schema(description = "活动信息")
    private ActivityBase activityBase;

    @Schema(description = "满返赠品活动名称")
    private String giveActivityName;

    @Schema(description = "满返赠品封面")
    private String giveImage;

    @Schema(description = "满返优惠券价格")
    private BigDecimal giveVoucherPrice;

    @Schema(description = "满返优惠券满足总额")
    private BigDecimal giveVoucherSubtotal;

    @Schema(description = "优惠券开始时间")
    private Long giveVoucherStartDate;

    @Schema(description = "优惠券失效日期")
    private Long giveVoucherEndDate;

    @Schema(description = "适用商品(ENUM):1-全部商品可用;2-指定商品可用")
    private Integer giveVoucherProductLimit;

    @Schema(description = "每人限领数量")
    private Integer voucherPreQuantity;

}
