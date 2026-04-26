package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SKU属性值
 *
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_sku_attr")
@Schema(description = "SKU属性值")
public class GoodsSkuAttr extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "主键ID")
    private String fId;

    @TableField("goods_sku_id")
    @Schema(description = "SKU_ID")
    private String goodsSkuId;

    @TableField("attr_name")
    @Schema(description = "属性名")
    private String attrName;

    @TableField("attr_value")
    @Schema(description = "属性值")
    private String attrValue;

    @TableField("sort_order")
    @Schema(description = "排序值")
    private Integer sortOrder;
}
