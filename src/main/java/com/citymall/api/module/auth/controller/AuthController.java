package com.citymall.api.module.auth.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.auth.dto.MockLoginDTO;
import com.citymall.api.module.auth.service.AuthService;
import com.citymall.api.module.auth.vo.MockLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 *
 * @author cqkir
 */
@Tag(name = "认证接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "模拟登录", description = "输入企微userId进行模拟登录，返回Authorization token")
    @PostMapping("/mock-login")
    public Result<MockLoginVO> mockLogin(@Valid @RequestBody MockLoginDTO dto) {
        return Result.success(authService.mockLogin(dto));
    }
}