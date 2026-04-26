package com.citymall.api.module.goods.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 新建SKU返回
 *
 * @author cqkir
 */
@Data
@Schema(description = "新建SKU返回")
public class GoodsSkuCreateVO {

    @Schema(description = "SKU_ID")
    private String skuId;

    @Schema(description = "SPU_ID")
    private String spuId;

    @Schema(description = "SPU编码")
    private String spuCode;

    @Schema(description = "规格详情ID")
    private String goodsSpecId;

    @Schema(description = "创建的SKU属性数量")
    private Integer attrCount;

    @Schema(description = "是否已触发价格重建")
    private Boolean priceRebuilt;
}
