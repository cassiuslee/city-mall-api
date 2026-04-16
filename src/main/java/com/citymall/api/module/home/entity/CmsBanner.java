package com.citymall.api.module.home.entity;

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
@TableName("cms_banner")
@Schema(description = "轮播配置表")
public class CmsBanner extends BaseEntity {

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "投放位置：1-首页，2-分类页")
    @TableField("distribution_site")
    private String distributionSite;

    @Schema(description = "图片地址")
    @TableField("img_url")
    private String imgUrl;

    @Schema(description = "跳转地址")
    @TableField("href_url")
    private String hrefUrl;

    @Schema(description = "跳转类型：1-商品，2-分类，3-自定义链接")
    @TableField("c_type")
    private String cType;

    @Schema(description = "关联商品ID")
    @TableField("product_id")
    private String productId;

    @Schema(description = "关联分类ID")
    @TableField("category_id")
    private String categoryId;

    @Schema(description = "是否展示：1-是，0-否")
    @TableField("is_show")
    private String isShow;

    @Schema(description = "排序码（越小越靠前）")
    @TableField("f_sort_code")
    private Long fSortCode;
}