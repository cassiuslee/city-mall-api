package com.citymall.api.module.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SKU价格重建DTO
 * @author cqkir
 */
@Data
@Schema(description = "SKU价格重建入参")
public class GoodsSkuPriceRebuildDTO {

    @Schema(description = "SPU_ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spuId;
}