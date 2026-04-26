package com.citymall.api.module.goods.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新建SKU入参
 *
 * @author cqkir
 */
@Data
@Schema(description = "新建SKU入参")
public class GoodsSkuCreateDTO {

    @NotBlank(message = "spuId不能为空")
    @Schema(description = "SPU_ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spuId;

    @NotBlank(message = "spuCode不能为空")
    @Schema(description = "SPU编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spuCode;

    @NotBlank(message = "goodsSpecId不能为空")
    @Schema(description = "规格详情ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsSpecId;

    @NotBlank(message = "fCreatorUserId不能为空")
    @Schema(description = "创建用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("fCreatorUserId")
    private String fCreatorUserId;

    @Schema(description = "SKU规格（普通字符串）")
    private String skuSpec;

    @Schema(description = "规格型号")
    private String skuSpecName;

    @Schema(description = "SKU图片（JSON）")
    private String skuPic;

    @Schema(description = "状态：0下架 1外部上架 2内部上架")
    private String skuStatus;

    @Schema(description = "不可见客户类型（en_code逗号分隔）")
    private String notShowTypes;

    @Schema(description = "SKU类型")
    private String skuType;

    @Schema(description = "是否自动重建SKU价格，默认true")
    private Boolean autoRebuildPrice;

    @Schema(description = "SKU属性列表，空或不传时不创建属性明细")
    private List<GoodsSkuAttrCreateDTO> attrs;
}
