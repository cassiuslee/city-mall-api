package com.citymall.api.module.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citymall.api.module.customer.entity.MarketTypes;
import com.citymall.api.module.customer.service.MarketTypesService;
import com.citymall.api.module.customer.mapper.MarketTypesMapper;
import org.springframework.stereotype.Service;

/**
* @author cqkir
* @description 针对表【market_types(经营主体类型表)】的数据库操作Service实现
* @createDate 2026-04-13 04:25:00
*/
@Service
public class MarketTypesServiceImpl extends ServiceImpl<MarketTypesMapper, MarketTypes>
    implements MarketTypesService{

}




