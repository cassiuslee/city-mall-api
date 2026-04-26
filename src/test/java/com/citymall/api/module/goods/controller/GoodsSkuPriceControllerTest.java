package com.citymall.api.module.goods.controller;

import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GoodsSkuPriceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoodsSkuPriceBuildService buildService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new GoodsSkuPriceController(buildService))
                .build();
    }

    @Test
    void should_rebuild_sku_price_by_sku_id() throws Exception {
        mockMvc.perform(post("/goods/sku-price/rebuildBySku")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"skuId\":\"SKU001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("1"))
                .andExpect(jsonPath("$.msg").value("success"));

        verify(buildService).rebuildBySkuId("SKU001");
    }
}
