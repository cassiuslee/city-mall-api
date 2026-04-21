package com.citymall.api.module.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 模拟登录返回
 *
 * @author cqkir
 */
@Data
@Schema(description = "模拟登录返回")
public class MockLoginVO {

    @Schema(description = "登录Token")
    private String token;

    @Schema(description = "用户主键")
    private String fId;

    @Schema(description = "企微用户ID")
    private String userId;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号")
    private String mobile;
}