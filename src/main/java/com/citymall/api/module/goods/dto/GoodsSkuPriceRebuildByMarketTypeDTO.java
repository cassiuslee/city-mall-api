package com.citymall.api.module.goods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 按客户类型重建SKU价格入参
 *
 * 适用场景：
 * 1. 新增客户类型后，补齐该类型下所有SKU价格
 * 2. 某个客户类型规则调整后，按类型重建价格
 * @author cqkir
 */
@Data
@Schema(description = "按客户类型重建SKU价格入参")
public class GoodsSkuPriceRebuildByMarketTypeDTO {

    /**
     * 客户类型编码，对应 market_types.en_code
     */
    @Schema(description = "客户类型编码，对应 market_types.en_code", requiredMode = Schema.RequiredMode.REQUIRED)
    private String marketType;
}