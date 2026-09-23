package com.wechuang.mallshop.trade.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "海报DTO", description = "海报DTO")
public class PosterOutput {
    @Schema(description = "二维码")
    private String qrcode;

    @Schema(description = "邀请网址")
    private String inviteUrl;

    @Schema(description = "下载网址")
    private String downloadUrl;

    @Schema(description = "海报网址")
    private String posterUrl;
}
