package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsPriceRuleItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cqkir
 */
@Mapper
public interface GoodsPriceRuleItemMapper extends BaseMapper<GoodsPriceRuleItem> {

    List<GoodsPriceRuleItem> selectByRuleId(@Param("ruleId") String ruleId);

    int deleteByRuleId(@Param("ruleId") String ruleId);

    int batchInsert(@Param("list") List<GoodsPriceRuleItem> list);
}