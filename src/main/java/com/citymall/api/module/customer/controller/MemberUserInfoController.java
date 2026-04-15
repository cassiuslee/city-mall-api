package com.citymall.api.module.customer.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.customer.dto.MemberUserInfoQueryDTO;
import com.citymall.api.module.customer.service.MemberUserQueryService;
import com.citymall.api.module.customer.vo.MemberUserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cqkir
 */
@Tag(name = "客户用户信息查询")
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class MemberUserInfoController {

    private final MemberUserQueryService memberUserQueryService;

    @Operation(summary = "根据userId查询用户及关联主体")
    @GetMapping("/member-user-info")
    public Result<MemberUserInfoVO> getMemberUserInfo(@Valid @ParameterObject MemberUserInfoQueryDTO queryDTO) {
        return Result.success(memberUserQueryService.getMemberUserInfoByUserId(queryDTO));
    }
}