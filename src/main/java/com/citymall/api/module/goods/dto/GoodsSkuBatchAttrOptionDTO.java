package com.citymall.api.module.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量新建SKU属性选项入参
 *
 * @author cqkir
 */
@Data
@Schema(description = "批量新建SKU属性选项入参")
public class GoodsSkuBatchAttrOptionDTO {

    @NotBlank(message = "attrName不能为空")
    @Schema(description = "属性名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String attrName;

    @NotEmpty(message = "attrValue不能为空")
    @Schema(description = "属性值候选列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> attrValue;

    @Schema(description = "排序值")
    private Integer sortOrder;
}
