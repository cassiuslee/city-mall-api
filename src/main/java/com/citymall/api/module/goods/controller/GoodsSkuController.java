package com.citymall.api.module.goods.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.goods.dto.GoodsSkuBatchCreateDTO;
import com.citymall.api.module.goods.dto.GoodsSkuCreateDTO;
import com.citymall.api.module.goods.service.GoodsSkuService;
import com.citymall.api.module.goods.vo.GoodsSkuBatchCreateVO;
import com.citymall.api.module.goods.vo.GoodsSkuCreateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品SKU接口
 *
 * @author cqkir
 */
@RestController
@RequestMapping("/goods/sku")
@RequiredArgsConstructor
@Tag(name = "商品SKU接口")
public class GoodsSkuController {

    private final GoodsSkuService goodsSkuService;

    @PostMapping
    @Operation(summary = "新建SKU")
    public Result<GoodsSkuCreateVO> create(@Valid @RequestBody GoodsSkuCreateDTO dto) {
        return Result.success(goodsSkuService.create(dto));
    }

    @PostMapping("/batch")
    @Operation(summary = "批量新建SKU")
    public Result<GoodsSkuBatchCreateVO> batchCreate(@Valid @RequestBody GoodsSkuBatchCreateDTO dto) {
        return Result.success(goodsSkuService.batchCreate(dto));
    }
}
