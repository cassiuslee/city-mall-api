package com.citymall.api.module.customer.service;


import com.citymall.api.module.customer.dto.MemberUserQueryDTO;
import com.citymall.api.module.customer.vo.MemberUserVO;

import java.util.List;

/**
 * @author cqkir
 */
public interface MemberUserService {

    List<MemberUserVO> list(MemberUserQueryDTO queryDTO);

    MemberUserVO getById(String fId);
}
