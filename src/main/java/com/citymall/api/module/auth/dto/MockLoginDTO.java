package com.citymall.api.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 模拟登录请求参数
 *
 * @author cqkir
 */
@Data
@Schema(description = "模拟登录请求参数")
public class MockLoginDTO {

    @NotBlank(message = "userId不能为空")
    @Schema(description = "企微用户ID", example = "JianZouPianFeng", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}