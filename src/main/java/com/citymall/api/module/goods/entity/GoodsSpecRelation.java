package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品规格详情
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_spec_relation")
@Schema(description = "商品规格详情")
public class GoodsSpecRelation extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "规格ID")
    private String fId;

    @TableField("spu_id")
    @Schema(description = "SPU_ID")
    private String spuId;

    @TableField("spec_name")
    @Schema(description = "规格名称")
    private String specName;

    @TableField("spec_unit")
    @Schema(description = "单位")
    private String specUnit;

    @TableField("net_weight")
    @Schema(description = "净重")
    private BigDecimal netWeight;

    @TableField("original_price")
    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @TableField("goods_package")
    @Schema(description = "包装")
    private String goodsPackage;

    @TableField("not_show_types")
    @Schema(description = "不可见客户类型（en_code）")
    private String notShowTypes;
}