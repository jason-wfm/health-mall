package com.wechuang.mallshop.sys.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Mq发送消息Vo", description = "Mq发送消息Vo")
public class MqMessageVo implements Serializable {

    private String exchange;

    private String routing_key;

    private Object data;

}
