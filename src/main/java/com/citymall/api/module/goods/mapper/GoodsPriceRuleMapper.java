package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsPriceRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cqkir
 */
@Mapper
public interface GoodsPriceRuleMapper extends BaseMapper<GoodsPriceRule> {

    GoodsPriceRule selectBySpuId(@Param("spuId") String spuId);
}