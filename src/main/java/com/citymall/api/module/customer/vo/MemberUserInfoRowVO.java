package com.citymall.api.module.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cqkir
 */
@Data
@Schema(description = "用户及关联主体扁平结果")
public class MemberUserInfoRowVO {

    @Schema(description = "用户主键")
    private String fId;

    @Schema(description = "用户状态")
    private String cStatus;

    @Schema(description = "企微账号")
    private String userId;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "OpenID")
    private String openid;

    @Schema(description = "UnionID")
    private String unionId;

    @Schema(description = "关系ID")
    private String relationId;

    @Schema(description = "主体ID")
    private String marketFid;

    @Schema(description = "用户身份")
    private String memberIdentity;

    @Schema(description = "身份权限JSON")
    private String identityPermissions;

    @Schema(description = "主体状态")
    private String marketCStatus;

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
