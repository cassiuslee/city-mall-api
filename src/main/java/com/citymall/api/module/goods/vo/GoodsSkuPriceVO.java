package com.citymall.api.module.goods.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author cqkir
 */
@Data
@Schema(description = "SKU最终价格")
public class GoodsSkuPriceVO {

    @Schema(description = "SKU_ID")
    private String skuId;

    @Schema(description = "客户类型（en_code）")
    private String marketType;

    @Schema(description = "最终销售价")
    private BigDecimal salePrice;
}