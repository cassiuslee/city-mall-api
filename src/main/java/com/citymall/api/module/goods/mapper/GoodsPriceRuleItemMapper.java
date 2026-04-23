package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsPriceRuleItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsPriceRuleItemMapper extends BaseMapper<GoodsPriceRuleItem> {

    List<GoodsPriceRuleItem> selectByRuleId(@Param("ruleId") String ruleId);

    int deleteByRuleId(@Param("ruleId") String ruleId);

    int batchInsert(@Param("list") List<GoodsPriceRuleItem> list);

    /**
     * 查询包含某个客户类型的全部规则项
     *
     * @param marketType 客户类型编码
     * @return 规则项列表
     */
    List<GoodsPriceRuleItem> selectByMarketType(@Param("marketType") String marketType);

    /**
     * 按 SPU_ID 查询规则项
     *
     * 说明：
     * 1. 直接查规则项，不依赖规则主表
     * 2. 用于价格生成逻辑
     *
     * @param spuId SPU_ID
     * @return 规则项列表
     */
    List<GoodsPriceRuleItem> selectBySpuId(@Param("spuId") String spuId);
}