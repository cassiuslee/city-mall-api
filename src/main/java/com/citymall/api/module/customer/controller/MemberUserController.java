package com.citymall.api.module.customer.controller;


import com.citymall.api.common.api.Result;
import com.citymall.api.module.customer.dto.MemberUserQueryDTO;
import com.citymall.api.module.customer.service.MemberUserService;
import com.citymall.api.module.customer.vo.MemberUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "会员用户")
@RestController
@RequestMapping("/member-user")
@RequiredArgsConstructor
public class MemberUserController {

    private final MemberUserService memberUserService;

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    public Result<List<MemberUserVO>> list(MemberUserQueryDTO queryDTO) {
        return Result.success(memberUserService.list(queryDTO));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{fId}")
    public Result<MemberUserVO> detail(@PathVariable String fId) {
        return Result.success(memberUserService.getById(fId));
    }
}