package com.citymall.api.module.customer.mapper;

import com.citymall.api.module.customer.entity.MarketTypes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author cqkir
 * @description 针对表【market_types(经营主体类型表)】的数据库操作Mapper
 * @createDate 2026-04-13 04:25:00
 * @Entity com.citymall.api.module.customer.entity.MarketTypes
 */
public interface MarketTypesMapper extends BaseMapper<MarketTypes> {
    /**
     * 查询所有启用的客户类型 en_code
     * <p>
     * 用于商品价格生成
     *
     * @return 客户类型编码列表（en_code）
     */
    List<String> selectAllEnabledEnCode();
}




