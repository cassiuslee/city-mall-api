package com.citymall.api.module.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cqkir
 */
@Data
@Schema(description = "用户及关联主体信息")
public class MemberUserInfoVO {
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
//
//    @Schema(description = "租户ID")
//    private String fTenantId;
//
//    @Schema(description = "创建时间")
//    private LocalDateTime fCreatorTime;
//
//    @Schema(description = "修改时间")
//    private LocalDateTime fLastModifyTime;

    @Schema(description = "关联主体列表")
    private List<MarketEntityVO> markets = new ArrayList<>();
}