package com.citymall.api.module.home.service;

import com.citymall.api.module.home.dto.HomeBannerQueryDTO;
import com.citymall.api.module.home.vo.HomeBannerVO;

import java.util.List;

public interface HomeBannerService {

    List<HomeBannerVO> listByDistributionSite(HomeBannerQueryDTO queryDTO);
}