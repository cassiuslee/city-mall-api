package com.citymall.api.module.goods.service.impl;

import com.citymall.api.common.exception.BizException;
import com.citymall.api.module.goods.dto.GoodsSkuAttrCreateDTO;
import com.citymall.api.module.goods.dto.GoodsSkuBatchAttrOptionDTO;
import com.citymall.api.module.goods.dto.GoodsSkuBatchCreateDTO;
import com.citymall.api.module.goods.dto.GoodsSkuCreateDTO;
import com.citymall.api.module.goods.entity.GoodsSkuAttr;
import com.citymall.api.module.goods.entity.GoodsSku;
import com.citymall.api.module.goods.entity.GoodsSpecRelation;
import com.citymall.api.module.goods.entity.GoodsSpu;
import com.citymall.api.module.goods.mapper.GoodsSkuAttrMapper;
import com.citymall.api.module.goods.mapper.GoodsSkuMapper;
import com.citymall.api.module.goods.mapper.GoodsSpecRelationMapper;
import com.citymall.api.module.goods.mapper.GoodsSpuMapper;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import com.citymall.api.module.goods.vo.GoodsSkuBatchCreateVO;
import com.citymall.api.module.goods.vo.GoodsSkuCreateVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GoodsSkuServiceImplTest {

    @Mock
    private GoodsSkuMapper goodsSkuMapper;

    @Mock
    private GoodsSkuAttrMapper goodsSkuAttrMapper;

    @Mock
    private GoodsSpuMapper goodsSpuMapper;

    @Mock
    private GoodsSpecRelationMapper goodsSpecRelationMapper;

    @Mock
    private GoodsSkuPriceBuildService goodsSkuPriceBuildService;

    @InjectMocks
    private GoodsSkuServiceImpl goodsSkuService;

    private GoodsSkuCreateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new GoodsSkuCreateDTO();
        dto.setSpuId("SPU_ID_001");
        dto.setSpuCode("SPU_CODE_001");
        dto.setGoodsSpecId("SPEC_ID_001");
        dto.setFCreatorUserId("USER_001");
        dto.setSkuSpec("红色 500g");
        dto.setSkuSpecName("500g");
        dto.setSkuPic("[\"https://example.com/sku.png\"]");
        dto.setSkuType("1");
    }

    @Test
    void should_create_sku_attrs_and_rebuild_price() {
        GoodsSpu spu = buildSpu("SPU_ID_001", "SPU_CODE_001");
        GoodsSpecRelation spec = buildSpec("SPEC_ID_001", "SPU_ID_001");
        dto.setAttrs(List.of(
                buildAttr("颜色", "红色", 1),
                buildAttr("规格", "500g", 2)
        ));

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001")).willReturn(spu);
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001")).willReturn(spec);
        given(goodsSkuMapper.insert(any(GoodsSku.class))).willReturn(1);
        given(goodsSkuAttrMapper.insert(any(GoodsSkuAttr.class))).willReturn(1);

        GoodsSkuCreateVO result = goodsSkuService.create(dto);

        assertNotNull(result.getSkuId());
        assertEquals("SPU_ID_001", result.getSpuId());
        assertEquals("SPU_CODE_001", result.getSpuCode());
        assertEquals("SPEC_ID_001", result.getGoodsSpecId());
        assertEquals(2, result.getAttrCount());
        assertTrue(result.getPriceRebuilt());

        ArgumentCaptor<GoodsSku> skuCaptor = ArgumentCaptor.forClass(GoodsSku.class);
        verify(goodsSkuMapper).insert(skuCaptor.capture());

        GoodsSku inserted = skuCaptor.getValue();
        assertEquals(result.getSkuId(), inserted.getFId());
        assertEquals("SPU_ID_001", inserted.getSpuId());
        assertEquals("SPU_CODE_001", inserted.getSpuCode());
        assertNull(inserted.getSkuCode());
        assertEquals("SPEC_ID_001", inserted.getGoodsSpecId());
        assertEquals("红色 500g", inserted.getSkuSpec());
        assertEquals("[\"https://example.com/sku.png\"]", inserted.getSkuPic());
        assertEquals("颜色:红色;规格:500g", inserted.getSkuFullAttr());
        assertEquals("1", inserted.getSkuStatus());
        assertEquals("USER_001", inserted.getFCreatorUserId());
        assertEquals(0, inserted.getFDeleteMark());
        assertEquals(0, inserted.getFVersion());

        ArgumentCaptor<GoodsSkuAttr> attrCaptor = ArgumentCaptor.forClass(GoodsSkuAttr.class);
        verify(goodsSkuAttrMapper, times(2)).insert(attrCaptor.capture());
        List<GoodsSkuAttr> insertedAttrs = attrCaptor.getAllValues();
        assertEquals(result.getSkuId(), insertedAttrs.get(0).getGoodsSkuId());
        assertEquals("颜色", insertedAttrs.get(0).getAttrName());
        assertEquals("红色", insertedAttrs.get(0).getAttrValue());
        assertEquals(1, insertedAttrs.get(0).getSortOrder());
        assertEquals("USER_001", insertedAttrs.get(0).getFCreatorUserId());
        assertEquals(result.getSkuId(), insertedAttrs.get(1).getGoodsSkuId());
        assertEquals("规格", insertedAttrs.get(1).getAttrName());
        assertEquals("500g", insertedAttrs.get(1).getAttrValue());
        assertEquals(2, insertedAttrs.get(1).getSortOrder());
        assertEquals("USER_001", insertedAttrs.get(1).getFCreatorUserId());

        verify(goodsSkuPriceBuildService).rebuildBySkuId(result.getSkuId());
    }

    @Test
    void should_batch_create_sku_and_build_sku_spec_name() {
        GoodsSkuBatchCreateDTO batchDto = buildBatchDto();

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_002"))
                .willReturn(buildSpec("SPEC_ID_002", "SPU_ID_001"));
        given(goodsSkuMapper.countBySpuIdAndSkuSpecName(eq("SPU_ID_001"), any())).willReturn(0);
        given(goodsSkuMapper.insert(any(GoodsSku.class))).willReturn(1);
        given(goodsSkuAttrMapper.insert(any(GoodsSkuAttr.class))).willReturn(1);

        GoodsSkuBatchCreateVO result = goodsSkuService.batchCreate(batchDto);

        assertEquals(8, result.getTotalCount());
        assertEquals(0, result.getSkippedCount());
        assertEquals(8, result.getItems().size());
        assertEquals("SPEC_ID_001", result.getItems().get(0).getGoodsSpecId());
        assertEquals("SPEC_ID_001", result.getItems().get(3).getGoodsSpecId());
        assertEquals("SPEC_ID_002", result.getItems().get(4).getGoodsSpecId());

        ArgumentCaptor<GoodsSku> skuCaptor = ArgumentCaptor.forClass(GoodsSku.class);
        verify(goodsSkuMapper, times(8)).insert(skuCaptor.capture());
        List<GoodsSku> insertedSkus = skuCaptor.getAllValues();
        assertEquals("20kg_0005_B型", insertedSkus.get(0).getSkuSpecName());
        assertEquals("20kg_0005_A型", insertedSkus.get(1).getSkuSpecName());
        assertEquals("20kg_0001_B型", insertedSkus.get(2).getSkuSpecName());
        assertEquals("20kg_0001_A型", insertedSkus.get(3).getSkuSpecName());
        assertEquals("10kg_0005_B型", insertedSkus.get(4).getSkuSpecName());
        assertEquals("10kg_0005_A型", insertedSkus.get(5).getSkuSpecName());
        assertEquals("10kg_0001_B型", insertedSkus.get(6).getSkuSpecName());
        assertEquals("10kg_0001_A型", insertedSkus.get(7).getSkuSpecName());
        assertEquals("20kg", insertedSkus.get(0).getSkuSpec());
        assertEquals("10kg", insertedSkus.get(4).getSkuSpec());
        assertEquals("SPEC_ID_001", insertedSkus.get(0).getGoodsSpecId());
        assertEquals("SPEC_ID_002", insertedSkus.get(4).getGoodsSpecId());

        verify(goodsSkuAttrMapper, times(16)).insert(any(GoodsSkuAttr.class));
        verify(goodsSkuPriceBuildService, times(8)).rebuildBySkuId(any());
    }

    @Test
    void should_skip_existing_sku_when_batch_create() {
        GoodsSkuBatchCreateDTO batchDto = buildBatchDto();

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_002"))
                .willReturn(buildSpec("SPEC_ID_002", "SPU_ID_001"));
        given(goodsSkuMapper.countBySpuIdAndSkuSpecName(eq("SPU_ID_001"), any())).willReturn(0);
        given(goodsSkuMapper.countBySpuIdAndSkuSpecName("SPU_ID_001", "20kg_0005_B型")).willReturn(1);
        given(goodsSkuMapper.insert(any(GoodsSku.class))).willReturn(1);
        given(goodsSkuAttrMapper.insert(any(GoodsSkuAttr.class))).willReturn(1);

        GoodsSkuBatchCreateVO result = goodsSkuService.batchCreate(batchDto);

        assertEquals(7, result.getTotalCount());
        assertEquals(1, result.getSkippedCount());

        ArgumentCaptor<GoodsSku> skuCaptor = ArgumentCaptor.forClass(GoodsSku.class);
        verify(goodsSkuMapper, times(7)).insert(skuCaptor.capture());
        List<String> skuSpecNames = skuCaptor.getAllValues().stream()
                .map(GoodsSku::getSkuSpecName)
                .toList();
        assertFalse(skuSpecNames.contains("20kg_0005_B型"));
        assertTrue(skuSpecNames.contains("20kg_0005_A型"));
        verify(goodsSkuAttrMapper, times(14)).insert(any(GoodsSkuAttr.class));
        verify(goodsSkuPriceBuildService, times(7)).rebuildBySkuId(any());
    }

    @Test
    void should_batch_create_sku_when_attrs_list_is_empty() {
        GoodsSkuBatchCreateDTO batchDto = buildBatchDto();
        batchDto.setAttrsList(List.of());

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_002"))
                .willReturn(buildSpec("SPEC_ID_002", "SPU_ID_001"));
        given(goodsSkuMapper.countBySpuIdAndSkuSpecName(eq("SPU_ID_001"), any())).willReturn(0);
        given(goodsSkuMapper.insert(any(GoodsSku.class))).willReturn(1);

        GoodsSkuBatchCreateVO result = goodsSkuService.batchCreate(batchDto);

        assertEquals(2, result.getTotalCount());
        assertEquals(0, result.getSkippedCount());

        ArgumentCaptor<GoodsSku> skuCaptor = ArgumentCaptor.forClass(GoodsSku.class);
        verify(goodsSkuMapper, times(2)).insert(skuCaptor.capture());
        List<GoodsSku> insertedSkus = skuCaptor.getAllValues();
        assertEquals("20kg", insertedSkus.get(0).getSkuSpecName());
        assertEquals("10kg", insertedSkus.get(1).getSkuSpecName());
        assertNull(insertedSkus.get(0).getSkuFullAttr());
        assertNull(insertedSkus.get(1).getSkuFullAttr());
        verify(goodsSkuAttrMapper, never()).insert(any());
        verify(goodsSkuPriceBuildService, times(2)).rebuildBySkuId(any());
    }

    @Test
    void should_throw_exception_when_batch_size_mismatch() {
        GoodsSkuBatchCreateDTO batchDto = buildBatchDto();
        batchDto.setSkuSpecList(List.of("20kg"));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.batchCreate(batchDto));

        assertEquals("规格详情ID数量与SKU规格数量不一致", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_batch_attr_group_is_empty() {
        GoodsSkuBatchCreateDTO batchDto = buildBatchDto();
        batchDto.setAttrsList(List.of(buildBatchAttrOption("颜色|色号", List.of("0005"), 0), buildBatchAttrOption("型号", List.of(), 0)));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.batchCreate(batchDto));

        assertEquals("SKU属性值候选不能为空", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_batch_attr_value_is_blank() {
        GoodsSkuBatchCreateDTO batchDto = buildBatchDto();
        batchDto.setAttrsList(List.of(buildBatchAttrOption("颜色|色号", List.of("0005", " "), 0)));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.batchCreate(batchDto));

        assertEquals("SKU属性值不能为空", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_not_create_sku_attrs_when_attrs_is_empty() {
        dto.setSkuPic("");

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));
        given(goodsSkuMapper.insert(any(GoodsSku.class))).willReturn(1);

        GoodsSkuCreateVO result = goodsSkuService.create(dto);

        assertEquals(0, result.getAttrCount());

        ArgumentCaptor<GoodsSku> skuCaptor = ArgumentCaptor.forClass(GoodsSku.class);
        verify(goodsSkuMapper).insert(skuCaptor.capture());
        assertNull(skuCaptor.getValue().getSkuFullAttr());
        assertNull(skuCaptor.getValue().getSkuPic());
        verify(goodsSkuAttrMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_sku_pic_is_not_json() {
        dto.setSkuPic("not-json");

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("skuPic必须是合法JSON字符串", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_not_rebuild_price_when_auto_rebuild_price_is_false() {
        dto.setAutoRebuildPrice(false);

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));
        given(goodsSkuMapper.insert(any(GoodsSku.class))).willReturn(1);

        GoodsSkuCreateVO result = goodsSkuService.create(dto);

        assertFalse(result.getPriceRebuilt());
        verify(goodsSkuPriceBuildService, never()).rebuildBySkuId(any());
    }

    @Test
    void should_throw_exception_when_attr_name_is_blank() {
        dto.setAttrs(List.of(buildAttr(" ", "红色", 1)));

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("SKU属性名不能为空", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_attr_value_is_blank() {
        dto.setAttrs(List.of(buildAttr("颜色", " ", 1)));

        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "SPU_ID_001"));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("SKU属性值不能为空", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_spu_not_found() {
        given(goodsSpuMapper.selectAvailableById("SPU_ID_001")).willReturn(null);

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("商品SPU不存在", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_spu_code_mismatch() {
        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "OTHER_SPU_CODE"));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("spuCode与spuId不匹配", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_spec_not_found() {
        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001")).willReturn(null);

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("商品规格不存在", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    @Test
    void should_throw_exception_when_spec_not_belong_to_spu() {
        given(goodsSpuMapper.selectAvailableById("SPU_ID_001"))
                .willReturn(buildSpu("SPU_ID_001", "SPU_CODE_001"));
        given(goodsSpecRelationMapper.selectAvailableById("SPEC_ID_001"))
                .willReturn(buildSpec("SPEC_ID_001", "OTHER_SPU_ID"));

        BizException exception = assertThrows(BizException.class, () -> goodsSkuService.create(dto));

        assertEquals("商品规格不属于当前SPU", exception.getMessage());
        verify(goodsSkuMapper, never()).insert(any());
    }

    private GoodsSpu buildSpu(String spuId, String spuCode) {
        GoodsSpu spu = new GoodsSpu();
        spu.setFId(spuId);
        spu.setSpuCode(spuCode);
        return spu;
    }

    private GoodsSpecRelation buildSpec(String specId, String spuId) {
        GoodsSpecRelation spec = new GoodsSpecRelation();
        spec.setFId(specId);
        spec.setSpuId(spuId);
        return spec;
    }

    private GoodsSkuAttrCreateDTO buildAttr(String attrName, String attrValue, Integer sortOrder) {
        GoodsSkuAttrCreateDTO attr = new GoodsSkuAttrCreateDTO();
        attr.setAttrName(attrName);
        attr.setAttrValue(attrValue);
        attr.setSortOrder(sortOrder);
        return attr;
    }

    private GoodsSkuBatchCreateDTO buildBatchDto() {
        GoodsSkuBatchCreateDTO batchDto = new GoodsSkuBatchCreateDTO();
        batchDto.setSpuId("SPU_ID_001");
        batchDto.setSpuCode("SPU_CODE_001");
        batchDto.setFCreatorUserId("USER_001");
        batchDto.setGoodsSpecIdList(List.of("SPEC_ID_001", "SPEC_ID_002"));
        batchDto.setSkuSpecList(List.of("20kg", "10kg"));
        batchDto.setSkuStatus("1");
        batchDto.setSkuType("1");
        batchDto.setAutoRebuildPrice(true);
        batchDto.setAttrsList(List.of(
                buildBatchAttrOption("颜色|色号", List.of("0005", "0001"), 0),
                buildBatchAttrOption("型号", List.of("B型", "A型"), 0)
        ));
        return batchDto;
    }

    private GoodsSkuBatchAttrOptionDTO buildBatchAttrOption(String attrName, List<String> attrValue, Integer sortOrder) {
        GoodsSkuBatchAttrOptionDTO option = new GoodsSkuBatchAttrOptionDTO();
        option.setAttrName(attrName);
        option.setAttrValue(attrValue);
        option.setSortOrder(sortOrder);
        return option;
    }
}
