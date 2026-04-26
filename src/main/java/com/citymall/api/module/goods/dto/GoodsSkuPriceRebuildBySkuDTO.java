package com.citymall.api.module.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 按SKU重建价格入参
 *
 * @author cqkir
 */
@Data
@Schema(description = "按SKU重建价格入参")
public class GoodsSkuPriceRebuildBySkuDTO {

    /**
     * SKU_ID
     */
    @Schema(description = "SKU_ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String skuId;
}