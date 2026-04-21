package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品SKU
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_sku")
@Schema(description = "商品SKU")
public class GoodsSku extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "SKU主键ID")
    private String fId;

    @TableField("spu_id")
    @Schema(description = "SPU_ID")
    private String spuId;

    @TableField("spu_code")
    @Schema(description = "SPU编码")
    private String spuCode;

    @TableField("sku_code")
    @Schema(description = "SKU编码")
    private String skuCode;

    @TableField("goods_spec_id")
    @Schema(description = "规格关联ID")
    private String goodsSpecId;

    @TableField("sku_spec")
    @Schema(description = "SKU规格（JSON）")
    private String skuSpec;

    @TableField("sku_spec_name")
    @Schema(description = "SKU规格名称")
    private String skuSpecName;

    @TableField("sku_status")
    @Schema(description = "SKU状态")
    private String skuStatus;

    @TableField("not_show_types")
    @Schema(description = "不可见客户类型（en_code逗号分隔）")
    private String notShowTypes;

    @TableField("sku_full_name")
    @Schema(description = "SKU全名")
    private String skuFullName;

    @TableField("sku_type")
    @Schema(description = "SKU类型")
    private String skuType;
}