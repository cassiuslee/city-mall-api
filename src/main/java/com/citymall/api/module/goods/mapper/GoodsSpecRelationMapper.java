package com.citymall.api.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.goods.entity.GoodsSpecRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author cqkir
 */
@Mapper
public interface GoodsSpecRelationMapper extends BaseMapper<GoodsSpecRelation> {

    /**
     * 按主键查询未删除规格，兼容历史数据 f_delete_mark 为 NULL 的情况。
     *
     * @param fId 规格ID
     * @return 规格
     */
    @Select("""
            select *
            from goods_spec_relation
            where f_id = #{fId}
              and (f_delete_mark = 0 or f_delete_mark is null)
            limit 1
            """)
    GoodsSpecRelation selectAvailableById(@Param("fId") String fId);
}
