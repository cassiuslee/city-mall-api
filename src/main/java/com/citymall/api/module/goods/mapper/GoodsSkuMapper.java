package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsSku;
import com.citymall.api.module.goods.vo.GoodsSkuBasePriceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cqkir
 */
@Mapper
public interface GoodsSkuMapper extends BaseMapper<GoodsSku> {

    List<GoodsSkuBasePriceVO> selectSkuWithOriginalPriceBySpuId(@Param("spuId") String spuId);

    List<String> selectSkuIdsBySpuIdAndSpecId(@Param("spuId") String spuId,
                                              @Param("goodsSpecId") String goodsSpecId);

    List<GoodsSku> selectBySpuId(@Param("spuId") String spuId);

    /**
     * 查询全部SKU及其当前原价
     *
     * 用于按客户类型批量补价
     *
     * @return SKU原价列表
     */
    List<GoodsSkuBasePriceVO> selectAllSkuWithOriginalPrice();
}