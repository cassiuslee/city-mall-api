package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsSkuPrice;
import com.citymall.api.module.goods.vo.GoodsSkuPriceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * SKU价格 Mapper
 *
 * @author cqkir
 */
@Mapper
public interface GoodsSkuPriceMapper extends BaseMapper<GoodsSkuPrice> {

    /**
     * 按SPU删除历史SKU价格
     *
     * @param spuId SPU_ID
     * @return 删除条数
     */
    int deleteBySpuId(@Param("spuId") String spuId);

    /**
     * 按客户类型删除历史价格
     *
     * @param marketType 客户类型编码
     * @return 删除条数
     */
    int deleteByMarketType(@Param("marketType") String marketType);

    /**
     * 按SKU_ID删除历史价格
     *
     * 用于重建单个SKU价格前清理旧数据。
     *
     * @param skuId SKU_ID
     * @return 删除条数
     */
    int deleteBySkuId(@Param("skuId") String skuId);

    /**
     * 批量插入SKU价格
     *
     * @param list SKU价格列表
     * @return 插入条数
     */
    int batchInsert(@Param("list") List<GoodsSkuPrice> list);

    /**
     * 按SKU_ID列表 + 客户类型列表覆盖价格
     *
     * @param skuIds SKU_ID列表
     * @param marketTypes 客户类型列表
     * @param salePrice 销售价
     * @param ruleId 规则ID
     * @return 更新条数
     */
    int updateSalePriceBySkuIdsAndMarketTypes(@Param("skuIds") List<String> skuIds,
                                              @Param("marketTypes") List<String> marketTypes,
                                              @Param("salePrice") BigDecimal salePrice,
                                              @Param("ruleId") String ruleId);

    /**
     * 查询单个SKU在某客户类型下的价格
     *
     * @param skuId SKU_ID
     * @param marketType 客户类型
     * @return SKU价格
     */
    GoodsSkuPriceVO selectBySkuIdAndMarketType(@Param("skuId") String skuId,
                                               @Param("marketType") String marketType);
}