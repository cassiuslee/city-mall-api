package com.citymall.api.module.goods.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量新建SKU入参
 *
 * @author cqkir
 */
@Data
@Schema(description = "批量新建SKU入参")
public class GoodsSkuBatchCreateDTO {

    @NotBlank(message = "spuId不能为空")
    @Schema(description = "SPU_ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spuId;

    @NotBlank(message = "spuCode不能为空")
    @Schema(description = "SPU编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spuCode;

    @NotBlank(message = "fCreatorUserId不能为空")
    @Schema(description = "创建用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("fCreatorUserId")
    private String fCreatorUserId;

    @NotEmpty(message = "goodsSpecIdList不能为空")
    @Schema(description = "规格详情ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> goodsSpecIdList;

    @NotEmpty(message = "skuSpecList不能为空")
    @Schema(description = "SKU规格列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> skuSpecList;

    @Schema(description = "状态：0下架 1外部上架 2内部上架")
    private String skuStatus;

    @Schema(description = "不可见客户类型（en_code逗号分隔）")
    private String notShowTypes;

    @Schema(description = "SKU类型")
    private String skuType;

    @Schema(description = "是否自动重建SKU价格，默认true")
    private Boolean autoRebuildPrice;

    @Schema(description = "SKU属性池列表，每项表示一个属性维度，attrValue为该属性候选值列表，空或不传时仅按skuSpecList生成SKU")
    private List<GoodsSkuBatchAttrOptionDTO> attrsList;
}
