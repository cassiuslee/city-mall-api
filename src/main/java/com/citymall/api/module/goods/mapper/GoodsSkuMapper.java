package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsSku;
import com.citymall.api.module.goods.vo.GoodsSkuBasePriceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SKU Mapper
 *
 * @author cqkir
 */
@Mapper
public interface GoodsSkuMapper extends BaseMapper<GoodsSku> {

    /**
     * 按SPU查询SKU及当前原价
     *
     * @param spuId SPU_ID
     * @return SKU原价列表
     */
    List<GoodsSkuBasePriceVO> selectSkuWithOriginalPriceBySpuId(@Param("spuId") String spuId);

    /**
     * 按SPU + 规格ID查询SKU_ID列表
     *
     * @param spuId SPU_ID
     * @param goodsSpecId 规格ID
     * @return SKU_ID列表
     */
    List<String> selectSkuIdsBySpuIdAndSpecId(@Param("spuId") String spuId,
                                              @Param("goodsSpecId") String goodsSpecId);

    /**
     * 按SPU查询SKU列表
     *
     * @param spuId SPU_ID
     * @return SKU列表
     */
    List<GoodsSku> selectBySpuId(@Param("spuId") String spuId);

    /**
     * 按SPU + SKU规格名称统计可用SKU数量
     *
     * @param spuId SPU_ID
     * @param skuSpecName SKU规格名称
     * @return SKU数量
     */
    int countBySpuIdAndSkuSpecName(@Param("spuId") String spuId,
                                   @Param("skuSpecName") String skuSpecName);

    /**
     * 查询全部SKU及其当前原价
     *
     * 用于按客户类型批量补价
     *
     * @return SKU原价列表
     */
    List<GoodsSkuBasePriceVO> selectAllSkuWithOriginalPrice();

    /**
     * 按SKU_ID查询单个SKU及当前原价
     *
     * 原价来自 goods_spec_relation.original_price。
     * 这里用于“只重建单个SKU价格”。
     *
     * @param skuId SKU_ID
     * @return SKU基础价格信息
     */
    GoodsSkuBasePriceVO selectSkuWithOriginalPriceBySkuId(@Param("skuId") String skuId);
}
