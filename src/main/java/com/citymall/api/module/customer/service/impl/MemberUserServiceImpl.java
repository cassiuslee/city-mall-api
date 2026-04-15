package com.citymall.api.module.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.citymall.api.common.exception.BizException;
import com.citymall.api.module.customer.dto.MemberUserQueryDTO;
import com.citymall.api.module.customer.entity.MemberUser;
import com.citymall.api.module.customer.mapper.MemberUserMapper;
import com.citymall.api.module.customer.service.MemberUserService;
import com.citymall.api.module.customer.vo.MemberUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberUserServiceImpl implements MemberUserService {

    private final MemberUserMapper memberUserMapper;

    @Override
    public List<MemberUserVO> list(MemberUserQueryDTO queryDTO) {
        LambdaQueryWrapper<MemberUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(queryDTO.getMobile() != null && !queryDTO.getMobile().isBlank(),
                MemberUser::getMobile, queryDTO.getMobile());

        wrapper.like(queryDTO.getNickname() != null && !queryDTO.getNickname().isBlank(),
                MemberUser::getNickname, queryDTO.getNickname());

        wrapper.eq(queryDTO.getUserId() != null && !queryDTO.getUserId().isBlank(),
                MemberUser::getUserId, queryDTO.getUserId());

        wrapper.eq(queryDTO.getCStatus() != null && !queryDTO.getCStatus().isBlank(),
                MemberUser::getCStatus, queryDTO.getCStatus());

        wrapper.orderByDesc(MemberUser::getFCreatorTime);

        List<MemberUser> list = memberUserMapper.selectList(wrapper);

        return list.stream().map(item -> {
            MemberUserVO vo = new MemberUserVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
    }

    @Override
    public MemberUserVO getById(String fId) {
        MemberUser entity = memberUserMapper.selectById(fId);
        if (entity == null) {
            throw new BizException("用户不存在");
        }
        MemberUserVO vo = new MemberUserVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}