package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsSpu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author cqkir
 */
@Mapper
public interface GoodsSpuMapper extends BaseMapper<GoodsSpu> {

    /**
     * 按主键查询未删除SPU，兼容历史数据 f_delete_mark 为 NULL 的情况。
     *
     * @param fId SPU_ID
     * @return SPU
     */
    @Select("""
            select *
            from goods_spu
            where f_id = #{fId}
              and (f_delete_mark = 0 or f_delete_mark is null)
            limit 1
            """)
    GoodsSpu selectAvailableById(@Param("fId") String fId);
}
