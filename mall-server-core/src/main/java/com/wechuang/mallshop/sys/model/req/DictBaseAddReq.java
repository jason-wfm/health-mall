package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "字典类型表参数")
public class DictBaseAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键编号")
    private String dictId;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "显示顺序:从小到大")
    private Integer dictSort;

    @Schema(description = "字典备注")
    private String dictNote;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    private Boolean dictEnable;


}
