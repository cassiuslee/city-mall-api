package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品价格规则
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_price_rule")
@Schema(description = "商品价格规则")
public class GoodsPriceRule extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "规则ID")
    private String fId;

    @TableField("spu_id")
    @Schema(description = "SPU_ID")
    private String spuId;

    @TableField("spu_code")
    @Schema(description = "SPU编码")
    private String spuCode;

    @TableField("rules_dec")
    @Schema(description = "规则描述")
    private String rulesDec;
}