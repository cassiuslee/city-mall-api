package com.citymall.api.module.customer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("market_types")
@Schema(description = "经营主体类型")
public class MarketTypes extends BaseEntity {

    @Schema(description = "类型名称")
    @TableField("market_type_name")
    private String marketTypeName;

    @Schema(description = "类型编码")
    @TableField("en_code")
    private String enCode;

    @Schema(description = "类型描述")
    @TableField("type_dec")
    private String typeDec;
}