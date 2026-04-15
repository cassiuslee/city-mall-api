package com.citymall.api.module.customer.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author cqkir
 */
@Data
@Schema(description = "用户查询参数")
public class MemberUserQueryDTO {

    private String mobile;

    private String nickname;

    private String userId;

    private String cStatus;
}
