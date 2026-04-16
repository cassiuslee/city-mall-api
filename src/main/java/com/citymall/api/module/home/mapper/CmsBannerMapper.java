package com.citymall.api.module.home.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.home.entity.CmsBanner;
import com.citymall.api.module.home.vo.HomeBannerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cqkir
 */
@Mapper
public interface CmsBannerMapper extends BaseMapper<CmsBanner> {

    List<HomeBannerVO> selectBannerList(@Param("distributionSite") Integer distributionSite);
}