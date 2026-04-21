package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品价格规则项
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_price_rule_item")
@Schema(description = "商品价格规则项")
public class GoodsPriceRuleItem extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "规则项ID")
    private String fId;

    @TableField("goods_rule_id")
    @Schema(description = "规则主表ID")
    private String goodsRuleId;

    @TableField("spu_id")
    @Schema(description = "SPU_ID")
    private String spuId;

    @TableField("goods_spec_id")
    @Schema(description = "规格ID")
    private String goodsSpecId;

    @TableField("sku_ids")
    @Schema(description = "SKU集合（逗号分隔）")
    private String skuIds;

    @TableField("market_types")
    @Schema(description = "客户类型集合（en_code，逗号分隔）")
    private String marketTypes;

    @TableField("sale_price")
    @Schema(description = "规则销售价（覆盖价）")
    private BigDecimal salePrice;

    @TableField("rule_dec")
    @Schema(description = "规则说明")
    private String ruleDec;
}