package com.citymall.api.module.customer.service.impl;

import com.citymall.api.common.exception.BizException;
import com.citymall.api.module.customer.dto.MemberUserInfoQueryDTO;
import com.citymall.api.module.customer.mapper.MemberUserMapper;
import com.citymall.api.module.customer.vo.MemberUserInfoRowVO;
import com.citymall.api.module.customer.vo.MemberUserInfoVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberUserQueryServiceImplTest {

    @Mock
    private MemberUserMapper memberUserMapper;

    @InjectMocks
    private MemberUserQueryServiceImpl memberUserQueryService;

    private MemberUserInfoQueryDTO queryDTO;

    @BeforeEach
    void setUp() {
        queryDTO = new MemberUserInfoQueryDTO();
        queryDTO.setUserId("zhangsan");
    }

    @Test
    void should_return_user_info_with_markets() {
        MemberUserInfoRowVO row1 = new MemberUserInfoRowVO();
        row1.setFId("U001");
        row1.setCStatus("1");
        row1.setUserId("zhangsan");
        row1.setMobile("13800000000");
        row1.setNickname("张三");
        row1.setAvatar("");
        row1.setGender("男");
        row1.setRelationId("R001");
        row1.setMarketFid("M001");
        row1.setMemberIdentity("负责人");
        row1.setMarketCStatus("1");
        row1.setMarkCode("CQ001");
        row1.setMarkName("重庆经营主体");
        row1.setMarkType("1");
        row1.setMarketTypeName("城市代理商");
        row1.setWeworkDptId(1001);
        row1.setWeworkDptName("重庆一区");
        row1.setSalesCompanyId("SC001");
        row1.setRegionalManagerId("RM001");
        row1.setServiceManager("SM001");
        row1.setIdentityPermissions("[\"1\",\"2\"]");

        MemberUserInfoRowVO row2 = new MemberUserInfoRowVO();
        row2.setFId("U001");
        row2.setCStatus("1");
        row2.setUserId("zhangsan");
        row2.setMobile("13800000000");
        row2.setNickname("张三");
        row2.setAvatar("");
        row2.setGender("男");
        row2.setRelationId("R002");
        row2.setMarketFid("M002");
        row2.setMemberIdentity("成员");
        row2.setMarketCStatus("1");
        row2.setMarkCode("CD001");
        row2.setMarkName("成都经营主体");
        row2.setMarkType("2");
        row2.setMarketTypeName("城市服务商");
        row2.setWeworkDptId(1002);
        row2.setWeworkDptName("成都一区");
        row2.setSalesCompanyId("SC002");
        row2.setRegionalManagerId("RM002");
        row2.setServiceManager("SM002");
        row2.setIdentityPermissions("[\"4\",\"6\"]");

        when(memberUserMapper.selectMemberUserInfoByUserId(queryDTO))
                .thenReturn(List.of(row1, row2));

        MemberUserInfoVO result = memberUserQueryService.getMemberUserInfoByUserId(queryDTO);

        assertNotNull(result);
        assertEquals("U001", result.getFId());
        assertEquals("zhangsan", result.getUserId());
        assertEquals("张三", result.getNickname());
        assertNotNull(result.getMarkets());
        assertEquals(2, result.getMarkets().size());
        assertEquals("重庆经营主体", result.getMarkets().get(0).getMarkName());
        assertEquals("成都经营主体", result.getMarkets().get(1).getMarkName());
        assertEquals(List.of("1", "2"), result.getMarkets().get(0).getPermissions());
        assertEquals(List.of("4", "6"), result.getMarkets().get(1).getPermissions());
    }

    @Test
    void should_deduplicate_markets() {
        MemberUserInfoRowVO row1 = new MemberUserInfoRowVO();
        row1.setFId("U001");
        row1.setUserId("zhangsan");
        row1.setRelationId("R001");
        row1.setMarketFid("M001");
        row1.setMarkName("重庆经营主体");

        MemberUserInfoRowVO row2 = new MemberUserInfoRowVO();
        row2.setFId("U001");
        row2.setUserId("zhangsan");
        row2.setRelationId("R001");
        row2.setMarketFid("M001");
        row2.setMarkName("重庆经营主体");

        when(memberUserMapper.selectMemberUserInfoByUserId(queryDTO))
                .thenReturn(List.of(row1, row2));

        MemberUserInfoVO result = memberUserQueryService.getMemberUserInfoByUserId(queryDTO);

        assertEquals(1, result.getMarkets().size());
    }

    @Test
    void should_return_empty_permissions_when_identity_permissions_is_blank() {
        MemberUserInfoRowVO row = new MemberUserInfoRowVO();
        row.setFId("U001");
        row.setUserId("zhangsan");
        row.setRelationId("R001");
        row.setMarketFid("M001");
        row.setMarkName("重庆经营主体");
        row.setIdentityPermissions(" ");

        when(memberUserMapper.selectMemberUserInfoByUserId(queryDTO))
                .thenReturn(List.of(row));

        MemberUserInfoVO result = memberUserQueryService.getMemberUserInfoByUserId(queryDTO);

        assertNotNull(result.getMarkets().get(0).getPermissions());
        assertTrue(result.getMarkets().get(0).getPermissions().isEmpty());
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        when(memberUserMapper.selectMemberUserInfoByUserId(queryDTO))
                .thenReturn(List.of());

        BizException exception = assertThrows(
                BizException.class,
                () -> memberUserQueryService.getMemberUserInfoByUserId(queryDTO)
        );

        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_userId_is_blank() {
        MemberUserInfoQueryDTO badQuery = new MemberUserInfoQueryDTO();
        badQuery.setUserId(" ");

        BizException exception = assertThrows(
                BizException.class,
                () -> memberUserQueryService.getMemberUserInfoByUserId(badQuery)
        );

        assertEquals("userId不能为空", exception.getMessage());
    }
}
