package com.citymall.api.module.home.controller;

import com.citymall.api.common.api.Result;
import com.citymall.api.module.home.dto.HomeBannerQueryDTO;
import com.citymall.api.module.home.service.HomeBannerService;
import com.citymall.api.module.home.vo.HomeBannerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cqkir
 */
@Tag(name = "首页模块")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeBannerController {

    private final HomeBannerService homeBannerService;

    @Operation(summary = "首页-轮播图")
    @GetMapping("/banner")
    public Result<List<HomeBannerVO>> banner(@ParameterObject HomeBannerQueryDTO queryDTO) {
        return Result.success(homeBannerService.listByDistributionSite(queryDTO));
    }
}