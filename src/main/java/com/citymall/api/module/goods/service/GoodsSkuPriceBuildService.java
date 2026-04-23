package com.citymall.api.module.goods.service;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * SKU价格构建服务
 * @author cqkir
 */
@Tag(name = "SKU价格构建服务")
public interface GoodsSkuPriceBuildService {

    /**
     * 按SPU重建SKU价格
     *
     * @param spuId SPU_ID
     */
    void rebuildBySpuId(String spuId);

    /**
     * 按客户类型重建SKU价格
     *
     * 适用场景：
     * 1. 新增客户类型后补价
     * 2. 单独刷新某个客户类型下的所有SKU价格
     *
     * @param marketType 客户类型编码，对应 market_types.en_code
     */
    void rebuildByMarketType(String marketType);
}