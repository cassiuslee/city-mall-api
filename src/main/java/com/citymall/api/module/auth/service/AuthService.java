package com.citymall.api.module.auth.service;

import com.citymall.api.module.auth.dto.MockLoginDTO;
import com.citymall.api.module.auth.vo.MockLoginVO;

/**
 * 认证服务
 *
 * @author cqkir
 */
public interface AuthService {

    /**
     * 模拟登录
     *
     * @param dto 请求参数
     * @return 登录结果
     */
    MockLoginVO mockLogin(MockLoginDTO dto);
}