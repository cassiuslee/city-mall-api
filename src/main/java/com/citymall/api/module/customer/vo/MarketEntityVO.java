package com.citymall.api.module.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cqkir
 */
@Data
@Schema(description = "经营主体信息")
public class MarketEntityVO {

    @Schema(description = "关系ID")
    private String relationId;

    @Schema(description = "主体ID")
    private String marketFid;

    @Schema(description = "用户身份")
    private String memberIdentity;

    @Schema(description = "主体状态")
    private String cStatus;

    @Schema(description = "唛头编码")
    private String markCode;

    @Schema(description = "唛头名称")
    private String markName;

    @Schema(description = "主体类型编码")
    private String markType;

    @Schema(description = "主体类型名称")
    private String marketTypeName;

    @Schema(description = "企业微信部门ID")
    private Integer weworkDptId;

    @Schema(description = "企业微信部门名称")
    private String weworkDptName;

    @Schema(description = "销售公司主体")
    private String salesCompanyId;

    @Schema(description = "区域经理ID")
    private String regionalManagerId;

    @Schema(description = "客服经理ID")
    private String serviceManager;
}