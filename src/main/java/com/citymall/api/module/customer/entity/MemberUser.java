package com.citymall.api.module.customer.entity;


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
@TableName("member_user")
@Schema(description = "客户人员信息")
public class MemberUser extends BaseEntity {

    @Schema(description = "客户状态")
    @TableField("c_status")
    private String cStatus;

    @Schema(description = "企微帐号")
    @TableField("user_id")
    private String userId;

    @Schema(description = "手机号")
    @TableField("mobile")
    private String mobile;

    @Schema(description = "昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "性别(1男，2女，3保密)")
    @TableField("gender")
    private String gender;

    @Schema(description = "微信OpenID")
    @TableField("openid")
    private String openid;

    @Schema(description = "微信UnionID")
    @TableField("union_id")
    private String unionId;
}