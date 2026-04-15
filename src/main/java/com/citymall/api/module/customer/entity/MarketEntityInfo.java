package com.citymall.api.module.customer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.citymall.api.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("market_entity_info")
@Schema(description = "经营主体信息")
public class MarketEntityInfo extends BaseEntity {

    @Schema(description = "状态(1启用，0停用)")
    @TableField("c_status")
    private String cStatus;

    @Schema(description = "唛头编码")
    @TableField("mark_code")
    private String markCode;

    @Schema(description = "唛头名称")
    @TableField("mark_name")
    private String markName;

    @Schema(description = "主体类型（1代理商，2服务商等）")
    @TableField("mark_type")
    private String markType;

    @Schema(description = "等级")
    @TableField("mark_level")
    private Integer markLevel;

    @Schema(description = "企业微信部门ID")
    @TableField("qywx_dpt_id")
    private Integer qywxDptId;

    @Schema(description = "企业微信部门名称")
    @TableField("qywx_dpt_name")
    private String qywxDptName;

    @Schema(description = "销售公司主体")
    @TableField("sales_company_id")
    private String salesCompanyId;

    @Schema(description = "可销售区域(JSON)")
    @TableField("sales_area")
    private String salesArea;

    @Schema(description = "区域经理ID")
    @TableField("regional_manager_id")
    private String regionalManagerId;

    @Schema(description = "客服经理ID")
    @TableField("service_manager")
    private String serviceManager;
}