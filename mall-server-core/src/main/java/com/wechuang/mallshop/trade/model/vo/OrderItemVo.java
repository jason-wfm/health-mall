package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductCommentReply;
import com.wechuang.mallshop.trade.model.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "店铺及商品信息", description = "店铺及商品信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemVo extends OrderItem implements Serializable {

    @Schema(description = "评价编号")
    private Long commentId;

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "产品编号")
    private Long productId;

    @Schema(description = "商品编号")
    private Long itemId;

    @Schema(description = "商品规格")
    private String itemName;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "买家编号")
    private Integer userId;

    @Schema(description = "买家姓名:user_nickname")
    private String userName;

    @Schema(description = "获得积分:冗余，独立表记录")
    private BigDecimal commentPoints;

    @Schema(description = "评价星级:1-5积分")
    private Integer commentScores;

    @Schema(description = "评价内容")
    private String commentContent;

    @Schema(description = "评论上传的图片(DOT)")
    private List<String> commentImage;

    @Schema(description = "有帮助")
    private Integer commentHelpful;

    @Schema(description = "无帮助")
    private Integer commentNohelpful;

    @Schema(description = "评价时间")
    private Date commentTime;

    @Schema(description = "匿名评价")
    private Boolean commentIsAnonymous;

    @Schema(description = "评价信息的状态(BOOL): 1-正常显示; 0-禁止显示")
    private Boolean commentEnable;

    @Schema(description = "门店编号")
    private Integer chainId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "商品评价回复")
    private List<ProductCommentReply> productCommentReplyList;
}
