package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "电子面单对象")
public class ElectronVo implements Serializable {

    @Schema(description = "订单编号")
    private String OrderCode;

    @Schema(description = "快递公司编码表")
    private String ShipperCode;

    @Schema(description = "运费支付方式")
    private Integer PayType;

    @Schema(description = "电子面单账号/客户编码 (如果您没有此账号，请联系合作的快递网点负责人申请)")
    private String CustomerName;

    @Schema(description = "密码")
    private String CustomerPwd;

    @Schema(description = "网点编码")
    private String SendSite;

    @Schema(description = "取件员编号/网点名称")
    private String SendStaff;

    @Schema(description = "月结卡号")
    private String MonthCode;

    @Schema(description = "快递业务类型")
    private String ExpType;

    @Schema(description = "快递运费")
    private BigDecimal Cost;

    @Schema(description = "其他费用")
    private BigDecimal OtherCost;

    @Schema(description = "是否返回电子面单模板")
    private String IsReturnPrintTemplate;

    @Schema(description = "模板规格")
    private String TemplateSize;

    @Schema(description = "发件人信息")
    private SenderVo Sender;

    @Schema(description = "接收人信息")
    private ReceiverVo Receiver;

    @Schema(description = "商品信息")
    private List<CommodityVo> Commodity;

    @Schema(description = "包裹总重量")
    private BigDecimal Weight;

    @Schema(description = "包裹数")
    private Integer Quantity;

    @Schema(description = "包裹总体积")
    private BigDecimal Volume;

    @Schema(description = "备注")
    private String Remark;

}
