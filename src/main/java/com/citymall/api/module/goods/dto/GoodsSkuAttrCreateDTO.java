package com.citymall.api.module.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新建SKU属性入参
 *
 * @author cqkir
 */
@Data
@Schema(description = "新建SKU属性入参")
public class GoodsSkuAttrCreateDTO {

    @Schema(description = "属性名")
    private String attrName;

    @Schema(description = "属性值")
    private String attrValue;

    @Schema(description = "排序值")
    private Integer sortOrder;
}
