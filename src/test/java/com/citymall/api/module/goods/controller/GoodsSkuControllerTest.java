package com.citymall.api.module.goods.controller;

import com.citymall.api.module.goods.service.GoodsSkuService;
import com.citymall.api.module.goods.vo.GoodsSkuBatchCreateVO;
import com.citymall.api.module.goods.vo.GoodsSkuCreateVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GoodsSkuControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoodsSkuService goodsSkuService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new GoodsSkuController(goodsSkuService))
                .build();
    }

    @Test
    void should_create_sku() throws Exception {
        GoodsSkuCreateVO vo = new GoodsSkuCreateVO();
        vo.setSkuId("SKU_ID_001");
        vo.setSpuId("SPU_ID_001");
        vo.setSpuCode("SPU_CODE_001");
        vo.setGoodsSpecId("SPEC_ID_001");
        vo.setPriceRebuilt(true);

        given(goodsSkuService.create(any())).willReturn(vo);

        mockMvc.perform(post("/goods/sku")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "spuId": "SPU_ID_001",
                                  "spuCode": "SPU_CODE_001",
                                  "goodsSpecId": "SPEC_ID_001",
                                  "fCreatorUserId": "USER_001",
                                  "skuSpecName": "500g"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("1"))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.result.skuId").value("SKU_ID_001"))
                .andExpect(jsonPath("$.result.spuCode").value("SPU_CODE_001"))
                .andExpect(jsonPath("$.result.priceRebuilt").value(true));
    }

    @Test
    void should_batch_create_sku() throws Exception {
        GoodsSkuCreateVO item = new GoodsSkuCreateVO();
        item.setSkuId("SKU_ID_001");
        item.setSpuId("SPU_ID_001");
        item.setSpuCode("SPU_CODE_001");
        item.setGoodsSpecId("SPEC_ID_001");
        item.setPriceRebuilt(true);

        GoodsSkuBatchCreateVO vo = new GoodsSkuBatchCreateVO();
        vo.setTotalCount(1);
        vo.setSkippedCount(0);
        vo.setItems(List.of(item));

        given(goodsSkuService.batchCreate(any())).willReturn(vo);

        mockMvc.perform(post("/goods/sku/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "spuId": "SPU_ID_001",
                                  "spuCode": "SPU_CODE_001",
                                  "fCreatorUserId": "USER_001",
                                  "goodsSpecIdList": ["SPEC_ID_001"],
                                  "skuSpecList": ["20kg"],
                                  "skuStatus": "1",
                                  "skuType": "1",
                                  "autoRebuildPrice": true,
                                  "attrsList": [
                                    {
                                      "attrName": "颜色|色号",
                                      "attrValue": ["0005", "0001"],
                                      "sortOrder": 0
                                    },
                                    {
                                      "attrName": "型号",
                                      "attrValue": ["B型", "A型"],
                                      "sortOrder": 0
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("1"))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.result.totalCount").value(1))
                .andExpect(jsonPath("$.result.skippedCount").value(0))
                .andExpect(jsonPath("$.result.items[0].skuId").value("SKU_ID_001"))
                .andExpect(jsonPath("$.result.items[0].priceRebuilt").value(true));
    }

    @Test
    void should_batch_create_sku_without_attrs_list() throws Exception {
        GoodsSkuBatchCreateVO vo = new GoodsSkuBatchCreateVO();
        vo.setTotalCount(1);
        vo.setSkippedCount(0);
        vo.setItems(List.of(new GoodsSkuCreateVO()));

        given(goodsSkuService.batchCreate(any())).willReturn(vo);

        mockMvc.perform(post("/goods/sku/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "spuId": "SPU_ID_001",
                                  "spuCode": "SPU_CODE_001",
                                  "fCreatorUserId": "USER_001",
                                  "goodsSpecIdList": ["SPEC_ID_001"],
                                  "skuSpecList": ["20kg"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("1"))
                .andExpect(jsonPath("$.result.totalCount").value(1));
    }
}
