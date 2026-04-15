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
@TableName("member_relation")
@Schema(description = "用户与经营主体关系")
public class MemberRelation extends BaseEntity {

    @Schema(description = "用户ID")
    @TableField("member_fid")
    private String memberFid;

    @Schema(description = "经营主体ID")
    @TableField("market_fid")
    private String marketFid;

    @Schema(description = "用户身份（角色）")
    @TableField("member_identity")
    private String memberIdentity;
}