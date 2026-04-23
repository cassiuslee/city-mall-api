package com.citymall.api.module.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.customer.entity.MarketTypes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 经营主体类型Mapper
 *
 * @author cqkir
 */
@Mapper
public interface MarketTypeMapper extends BaseMapper<MarketTypes> {

    /**
     * 查询所有启用的客户类型编码
     *
     * @return en_code 列表
     */
    List<String> selectAllEnabledEnCode();

    /**
     * 判断某个客户类型编码是否存在
     *
     * @param enCode 类型编码
     * @return 存在数量
     */
    Integer countByEnCode(@Param("enCode") String enCode);
}