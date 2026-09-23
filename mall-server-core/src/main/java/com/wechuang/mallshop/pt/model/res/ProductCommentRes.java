package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductCommentRes extends BaseListRes implements Serializable {

    private long good;

    private long satisfied;

    private long bad;

}
