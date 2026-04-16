package com.citymall.api.module.home.service.impl;

import com.citymall.api.common.util.FileUrlUtils;
import com.citymall.api.module.home.dto.HomeBannerQueryDTO;
import com.citymall.api.module.home.mapper.CmsBannerMapper;
import com.citymall.api.module.home.service.HomeBannerService;
import com.citymall.api.module.home.vo.HomeBannerVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeBannerServiceImpl implements HomeBannerService {

    private final CmsBannerMapper cmsBannerMapper;
    private final FileUrlUtils fileUrlUtils;

    @Override
    public List<HomeBannerVO> listByDistributionSite(HomeBannerQueryDTO queryDTO) {
        Integer distributionSite = queryDTO == null || queryDTO.getDistributionSite() == null
                ? Integer.valueOf("1")
                : queryDTO.getDistributionSite();

        List<HomeBannerVO> list = cmsBannerMapper.selectBannerList(String.valueOf(distributionSite));
        list.forEach(item -> item.setImgUrl(fileUrlUtils.parseFirstUrl(item.getImgUrl())));
        return list;
    }
}