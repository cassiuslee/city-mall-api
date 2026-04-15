package com.citymall.api.module.customer.controller;

import com.citymall.api.module.customer.service.MemberUserQueryService;
import com.citymall.api.module.customer.vo.MemberUserInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberUserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberUserQueryService memberUserQueryService;

    @Test
    void should_return_member_user_info() throws Exception {
        MemberUserInfoVO vo = new MemberUserInfoVO();
        vo.setFId("U001");
        vo.setUserId("zhangsan");
        vo.setMobile("13800000000");
        vo.setNickname("张三");
        vo.setMarkets(new ArrayList<>());

        given(memberUserQueryService.getMemberUserInfoByUserId(any())).willReturn(vo);

        mockMvc.perform(get("/customer/member-user-info")
                        .param("userId", "zhangsan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.data.fid").value("U001"))
                .andExpect(jsonPath("$.data.userId").value("zhangsan"))
                .andExpect(jsonPath("$.data.nickname").value("张三"));
    }

    @Test
    void should_fail_when_userId_is_missing() throws Exception {
        mockMvc.perform(get("/customer/member-user-info"))
                .andExpect(status().isOk());
    }
}