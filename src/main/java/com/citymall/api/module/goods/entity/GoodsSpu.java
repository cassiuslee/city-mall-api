package com.citymall.api.module.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品SPU
 * @author cqkir
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_spu")
@Schema(description = "商品SPU")
public class GoodsSpu extends BaseEntity {

    @TableId("f_id")
    @Schema(description = "主键ID")
    private String fId;

    @TableField("spu_code")
    @Schema(description = "商品编码")
    private String spuCode;

    @TableField("spu_name")
    @Schema(description = "商品名称")
    private String spuName;

    @TableField("en_name")
    @Schema(description = "英文名称")
    private String enName;

    @TableField("spu_desc")
    @Schema(description = "商品描述")
    private String spuDesc;

    @TableField("spu_pics")
    @Schema(description = "商品图片（JSON数组）")
    private String spuPics;

    @TableField("spu_status")
    @Schema(description = "状态：0下架 1外部上架 2内部上架")
    private String spuStatus;

    @TableField("brand_id")
    @Schema(description = "品牌ID")
    private String brandId;
}