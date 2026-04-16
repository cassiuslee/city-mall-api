package com.citymall.api.module.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cqkir
 */
@Data
@Schema(description = "首页轮播图查询参数")
public class HomeBannerQueryDTO {

    @Schema(description = "投放位置：1-首页，2-分类页", example = "1")
    private Integer distributionSite = 1;
}