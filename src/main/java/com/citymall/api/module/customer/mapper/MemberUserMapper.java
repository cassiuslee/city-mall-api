package com.citymall.api.module.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citymall.api.module.customer.dto.MemberUserInfoQueryDTO;
import com.citymall.api.module.customer.entity.MemberUser;
import com.citymall.api.module.customer.vo.MemberUserInfoRowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cqkir
 */
@Mapper
public interface MemberUserMapper extends BaseMapper<MemberUser> {

    List<MemberUserInfoRowVO> selectMemberUserInfoByUserId(@Param("query") MemberUserInfoQueryDTO queryDTO);
}