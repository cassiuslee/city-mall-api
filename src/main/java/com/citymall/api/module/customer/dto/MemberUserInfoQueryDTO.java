package com.citymall.api.module.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author cqkir
 */
@Data
@Schema(description = "根据userId查询用户及主体信息-请求参数")
public class MemberUserInfoQueryDTO {

    @Schema(description = "企微用户ID", example = "zhangsan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "userId不能为空")
    private String userId;
}