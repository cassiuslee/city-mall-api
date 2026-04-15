package com.citymall.api.module.customer.service;

import com.citymall.api.module.customer.dto.MemberUserInfoQueryDTO;
import com.citymall.api.module.customer.vo.MemberUserInfoVO;

public interface MemberUserQueryService {

    MemberUserInfoVO getMemberUserInfoByUserId(MemberUserInfoQueryDTO queryDTO);
}