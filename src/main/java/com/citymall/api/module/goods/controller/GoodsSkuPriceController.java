package com.citymall.api.module.goods.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.goods.dto.GoodsSkuPriceRebuildDTO;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * SKU价格接口
 * @author cqkir
 */
@RestController
@RequestMapping("/goods/sku-price")
@RequiredArgsConstructor
@Tag(name = "商品SKU价格接口")
public class GoodsSkuPriceController {

    private final GoodsSkuPriceBuildService buildService;

    /**
     * 按SPU重建SKU价格
     */
    @PostMapping("/rebuildBySpu")
    @Operation(summary = "按SPU重建SKU价格")
    public Result<Void> rebuild(@RequestBody GoodsSkuPriceRebuildDTO dto) {

        buildService.rebuildBySpuId(dto.getSpuId());

        return Result.success();
    }
}