package com.citymall.api.module.goods.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author cqkir
 */
@Data
@Schema(description = "SKU基础原价信息")
public class GoodsSkuBasePriceVO {

    @Schema(description = "SKU_ID")
    private String skuId;

    @Schema(description = "SPU_ID")
    private String spuId;

    @Schema(description = "规格ID")
    private String goodsSpecId;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU规格名称")
    private String skuSpecName;

    @Schema(description = "当前原价")
    private BigDecimal originalPrice;
}