package com.citymall.api.module.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.citymall.api.common.exception.BizException;
import com.citymall.api.module.auth.dto.MockLoginDTO;
import com.citymall.api.module.auth.service.AuthService;
import com.citymall.api.module.auth.vo.MockLoginVO;
import com.citymall.api.module.customer.entity.MemberUser;
import com.citymall.api.module.customer.mapper.MemberUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 *
 * @author cqkir
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberUserMapper memberUserMapper;

    @Override
    public MockLoginVO mockLogin(MockLoginDTO dto) {
        MemberUser user = memberUserMapper.selectOne(
                new LambdaQueryWrapper<MemberUser>()
                        .eq(MemberUser::getUserId, dto.getUserId())
                        .last("limit 1")
        );

        if (user == null) {
            throw new BizException("用户不存在");
        }

        // Sa-Token 登录，loginId 建议使用 fId
        StpUtil.login(user.getFId());
        String token = StpUtil.getTokenValue();

        MockLoginVO vo = new MockLoginVO();
        vo.setToken(token);
        vo.setFId(user.getFId());
        vo.setUserId(user.getUserId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setMobile(user.getMobile());
        return vo;
    }
}