package com.citymall.api.module.goods.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.goods.dto.GoodsSkuPriceRebuildByMarketTypeDTO;
import com.citymall.api.module.goods.dto.GoodsSkuPriceRebuildBySkuDTO;
import com.citymall.api.module.goods.dto.GoodsSkuPriceRebuildDTO;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商品SKU价格接口
 *
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
     * <p>
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

    /**
     * 按SKU重建SKU价格
     * <p>
     * 适用场景：
     * 1. 修改单个SKU后调用
     * 2. 修改单个SKU所属规格后调用
     * 3. 只想刷新一个SKU价格，不影响整个SPU
     */
    @PostMapping("/rebuildBySku")
    @Operation(summary = "按SKU重建SKU价格")
    public Result<Void> rebuildBySku(@RequestBody GoodsSkuPriceRebuildBySkuDTO dto) {
        buildService.rebuildBySkuId(dto.getSkuId());
        return Result.success();
    }
}