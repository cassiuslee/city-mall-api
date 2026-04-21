package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsSkuPrice;
import com.citymall.api.module.goods.vo.GoodsSkuPriceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cqkir
 */
@Mapper
public interface GoodsSkuPriceMapper extends BaseMapper<GoodsSkuPrice> {

    int deleteBySpuId(@Param("spuId") String spuId);

    int batchInsert(@Param("list") List<GoodsSkuPrice> list);

    int updateSalePriceBySkuIdsAndMarketTypes(@Param("skuIds") List<String> skuIds,
                                              @Param("marketTypes") List<String> marketTypes,
                                              @Param("salePrice") BigDecimal salePrice,
                                              @Param("ruleId") String ruleId);

    GoodsSkuPriceVO selectBySkuIdAndMarketType(@Param("skuId") String skuId,
                                               @Param("marketType") String marketType);
}