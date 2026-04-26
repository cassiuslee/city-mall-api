package com.citymall.api.module.goods.service;

import com.citymall.api.module.goods.dto.GoodsSkuBatchCreateDTO;
import com.citymall.api.module.goods.dto.GoodsSkuCreateDTO;
import com.citymall.api.module.goods.vo.GoodsSkuBatchCreateVO;
import com.citymall.api.module.goods.vo.GoodsSkuCreateVO;

/**
 * SKU服务
 *
 * @author cqkir
 */
public interface GoodsSkuService {

    /**
     * 新建SKU
     *
     * @param dto 新建SKU入参
     * @return 新建结果
     */
    GoodsSkuCreateVO create(GoodsSkuCreateDTO dto);

    /**
     * 批量新建SKU
     *
     * @param dto 批量新建SKU入参
     * @return 批量新建结果
     */
    GoodsSkuBatchCreateVO batchCreate(GoodsSkuBatchCreateDTO dto);
}
