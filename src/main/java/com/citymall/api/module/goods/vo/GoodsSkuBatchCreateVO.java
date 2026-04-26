package com.citymall.api.module.goods.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量新建SKU返回
 *
 * @author cqkir
 */
@Data
@Schema(description = "批量新建SKU返回")
public class GoodsSkuBatchCreateVO {

    @Schema(description = "创建数量")
    private Integer totalCount;

    @Schema(description = "跳过数量")
    private Integer skippedCount;

    @Schema(description = "创建结果列表")
    private List<GoodsSkuCreateVO> items;
}
