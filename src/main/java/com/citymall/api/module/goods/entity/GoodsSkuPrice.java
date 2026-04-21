package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * SKU价格结果表
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_sku_price")
@Schema(description = "SKU价格结果表")
public class GoodsSkuPrice extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "主键ID")
    private String fId;

    @TableField("rule_id")
    @Schema(description = "规则ID")
    private String ruleId;

    @TableField("sku_id")
    @Schema(description = "SKU_ID")
    private String skuId;

    @TableField("market_type")
    @Schema(description = "客户类型（market_types.en_code）")
    private String marketType;

    @TableField("sale_price")
    @Schema(description = "最终销售价（固化价格）")
    private BigDecimal salePrice;
}