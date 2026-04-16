package com.citymall.api.module.home.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cqkir
 */
@Data
@Schema(description = "首页轮播图返回对象")
public class HomeBannerVO {

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "跳转地址")
    private String hrefUrl;

    @Schema(description = "跳转类型")
    private Integer type;
}