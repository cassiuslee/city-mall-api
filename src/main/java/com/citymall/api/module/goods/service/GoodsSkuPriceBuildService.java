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
     * 核心逻辑：
     * 1. 查询SKU + 原价
     * 2. 查询客户类型
     * 3. 删除旧价格
     * 4. 生成默认原价
     * 5. 按规则覆盖
     *
     * @param spuId SPU_ID
     */
    void rebuildBySpuId(String spuId);
}