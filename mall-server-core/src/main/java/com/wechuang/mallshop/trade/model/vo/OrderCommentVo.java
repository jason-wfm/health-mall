package com.wechuang.mallshop.trade.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wechuang.mallshop.trade.model.entity.OrderComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCommentVo extends OrderComment implements Serializable {

    @Schema(description = "评论上传的图片：|分割多张图片")
    @TableField(value = "comment_images")
    private Object commentImages;

}
