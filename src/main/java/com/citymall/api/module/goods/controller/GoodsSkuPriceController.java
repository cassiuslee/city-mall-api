package com.citymall.api.module.goods.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.goods.dto.GoodsSkuPriceRebuildByMarketTypeDTO;
import com.citymall.api.module.goods.dto.GoodsSkuPriceRebuildDTO;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商品SKU价格接口
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
    public Result<Void> rebuildBySpu(@RequestBody GoodsSkuPriceRebuildDTO dto) {
        buildService.rebuildBySpuId(dto.getSpuId());
        return Result.success();
    }

    /**
     * 按客户类型重建SKU价格
     *
     * 适用场景：
     * 1. 新建客户类型后调用
     * 2. 某客户类型需要单独刷新时调用
     */
    @PostMapping("/rebuildByMarketType")
    @Operation(summary = "按客户类型重建SKU价格")
    public Result<Void> rebuildByMarketType(@RequestBody GoodsSkuPriceRebuildByMarketTypeDTO dto) {
        buildService.rebuildByMarketType(dto.getMarketType());
        return Result.success();
    }
}