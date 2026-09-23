package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典项表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "字典项表分页查询")
public class DictItemListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "字典项编号")
    private String dictItemId;

    @Schema(description = "字典项名称")
    private String dictItemName;

    @Schema(description = "字典项值")
    private String dictItemCode;

    @Schema(description = "字典类型")
    private String dictId;

    @Schema(description = "是否使用(BOOL):0-未用;1-使用")
    private Boolean dictItemStatus;

    @Schema(description = "备注")
    private String dictItemNote;

    @Schema(description = "显示顺序")
    private Integer dictItemSort;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    private Boolean dictItemEnable;

    public DictItemListReq() {
        setSidx("dict_item_sort");
    }
}
