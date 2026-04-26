package com.citymall.api.module.goods.service.impl;

import com.citymall.api.common.exception.BizException;
import com.citymall.api.common.util.IdGenerator;
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
import com.citymall.api.module.goods.service.GoodsSkuService;
import com.citymall.api.module.goods.vo.GoodsSkuBatchCreateVO;
import com.citymall.api.module.goods.vo.GoodsSkuCreateVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * SKU服务实现
 *
 * @author cqkir
 */
@Service
@RequiredArgsConstructor
public class GoodsSkuServiceImpl implements GoodsSkuService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final GoodsSkuMapper goodsSkuMapper;
    private final GoodsSkuAttrMapper goodsSkuAttrMapper;
    private final GoodsSpuMapper goodsSpuMapper;
    private final GoodsSpecRelationMapper goodsSpecRelationMapper;
    private final GoodsSkuPriceBuildService goodsSkuPriceBuildService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsSkuBatchCreateVO batchCreate(GoodsSkuBatchCreateDTO dto) {
        validateBatchParam(dto);

        List<GoodsSkuCreateVO> items = new ArrayList<>();
        int skippedCount = 0;
        List<List<GoodsSkuAttrCreateDTO>> attrCombinations = buildAttrCombinations(dto.getAttrsList());
        for (int i = 0; i < dto.getSkuSpecList().size(); i++) {
            for (List<GoodsSkuAttrCreateDTO> attrs : attrCombinations) {
                GoodsSkuCreateDTO single = buildSingleCreateDTO(dto, i, attrs);
                if (goodsSkuMapper.countBySpuIdAndSkuSpecName(single.getSpuId(), single.getSkuSpecName()) > 0) {
                    skippedCount++;
                    continue;
                }
                items.add(create(single));
            }
        }

        GoodsSkuBatchCreateVO vo = new GoodsSkuBatchCreateVO();
        vo.setTotalCount(items.size());
        vo.setSkippedCount(skippedCount);
        vo.setItems(items);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsSkuCreateVO create(GoodsSkuCreateDTO dto) {
        GoodsSpu spu = goodsSpuMapper.selectAvailableById(dto.getSpuId());
        if (spu == null) {
            throw new BizException("商品SPU不存在");
        }
        if (!dto.getSpuCode().equals(spu.getSpuCode())) {
            throw new BizException("spuCode与spuId不匹配");
        }

        GoodsSpecRelation spec = goodsSpecRelationMapper.selectAvailableById(dto.getGoodsSpecId());
        if (spec == null) {
            throw new BizException("商品规格不存在");
        }
        if (!dto.getSpuId().equals(spec.getSpuId())) {
            throw new BizException("商品规格不属于当前SPU");
        }

        List<GoodsSkuAttrCreateDTO> attrs = normalizeAttrs(dto.getAttrs());
        String skuFullAttr = buildSkuFullAttr(attrs);
        String skuPic = normalizeJson(dto.getSkuPic(), "skuPic");

        String skuId = IdGenerator.nextIdStr();
        GoodsSku sku = new GoodsSku();
        sku.setFId(skuId);
        sku.setSpuId(dto.getSpuId());
        sku.setSpuCode(dto.getSpuCode());
        sku.setGoodsSpecId(dto.getGoodsSpecId());
        sku.setSkuSpec(dto.getSkuSpec());
        sku.setSkuSpecName(dto.getSkuSpecName());
        sku.setSkuPic(skuPic);
        sku.setSkuStatus(defaultIfBlank(dto.getSkuStatus(), "1"));
        sku.setNotShowTypes(dto.getNotShowTypes());
        sku.setSkuFullAttr(skuFullAttr);
        sku.setSkuType(dto.getSkuType());
        sku.setFCreatorUserId(dto.getFCreatorUserId());
        sku.setFDeleteMark(0);
        sku.setFVersion(0);

        goodsSkuMapper.insert(sku);
        insertAttrs(skuId, dto.getFCreatorUserId(), attrs);

        boolean autoRebuildPrice = dto.getAutoRebuildPrice() == null || dto.getAutoRebuildPrice();
        if (autoRebuildPrice) {
            goodsSkuPriceBuildService.rebuildBySkuId(skuId);
        }

        GoodsSkuCreateVO vo = new GoodsSkuCreateVO();
        vo.setSkuId(skuId);
        vo.setSpuId(dto.getSpuId());
        vo.setSpuCode(dto.getSpuCode());
        vo.setGoodsSpecId(dto.getGoodsSpecId());
        vo.setAttrCount(attrs.size());
        vo.setPriceRebuilt(autoRebuildPrice);
        return vo;
    }

    private void validateBatchParam(GoodsSkuBatchCreateDTO dto) {
        int goodsSpecSize = dto.getGoodsSpecIdList().size();
        int skuSpecSize = dto.getSkuSpecList().size();
        if (goodsSpecSize != skuSpecSize) {
            throw new BizException("规格详情ID数量与SKU规格数量不一致");
        }
    }

    private List<List<GoodsSkuAttrCreateDTO>> buildAttrCombinations(List<GoodsSkuBatchAttrOptionDTO> attrsList) {
        List<List<GoodsSkuAttrCreateDTO>> combinations = new ArrayList<>();
        combinations.add(new ArrayList<>());
        if (attrsList == null || attrsList.isEmpty()) {
            return combinations;
        }

        for (int i = 0; i < attrsList.size(); i++) {
            GoodsSkuBatchAttrOptionDTO attrOption = attrsList.get(i);
            if (attrOption == null) {
                throw new BizException("SKU属性维度不能为空");
            }
            if (attrOption.getAttrName() == null || attrOption.getAttrName().isBlank()) {
                throw new BizException("SKU属性名不能为空");
            }
            if (attrOption.getAttrValue() == null || attrOption.getAttrValue().isEmpty()) {
                throw new BizException("SKU属性值候选不能为空");
            }

            List<List<GoodsSkuAttrCreateDTO>> next = new ArrayList<>();
            for (List<GoodsSkuAttrCreateDTO> combination : combinations) {
                for (String attrValue : attrOption.getAttrValue()) {
                    GoodsSkuAttrCreateDTO attr = buildAttr(attrOption, attrValue, i);
                    List<GoodsSkuAttrCreateDTO> merged = new ArrayList<>(combination);
                    merged.add(attr);
                    next.add(merged);
                }
            }
            combinations = next;
        }
        return combinations;
    }

    private GoodsSkuAttrCreateDTO buildAttr(GoodsSkuBatchAttrOptionDTO attrOption, String attrValue, int index) {
        if (attrValue == null || attrValue.isBlank()) {
            throw new BizException("SKU属性值不能为空");
        }

        GoodsSkuAttrCreateDTO attr = new GoodsSkuAttrCreateDTO();
        attr.setAttrName(attrOption.getAttrName());
        attr.setAttrValue(attrValue);
        attr.setSortOrder(attrOption.getSortOrder() == null ? index + 1 : attrOption.getSortOrder());
        return attr;
    }

    private GoodsSkuCreateDTO buildSingleCreateDTO(GoodsSkuBatchCreateDTO dto, int index, List<GoodsSkuAttrCreateDTO> attrs) {
        GoodsSkuCreateDTO single = new GoodsSkuCreateDTO();
        String skuSpec = dto.getSkuSpecList().get(index);

        single.setSpuId(dto.getSpuId());
        single.setSpuCode(dto.getSpuCode());
        single.setGoodsSpecId(dto.getGoodsSpecIdList().get(index));
        single.setFCreatorUserId(dto.getFCreatorUserId());
        single.setSkuSpec(skuSpec);
        single.setSkuSpecName(buildSkuSpecName(skuSpec, attrs));
        single.setSkuStatus(dto.getSkuStatus());
        single.setNotShowTypes(dto.getNotShowTypes());
        single.setSkuType(dto.getSkuType());
        single.setAutoRebuildPrice(dto.getAutoRebuildPrice());
        single.setAttrs(attrs);
        return single;
    }

    private String buildSkuSpecName(String skuSpec, List<GoodsSkuAttrCreateDTO> attrs) {
        if (skuSpec == null || skuSpec.isBlank()) {
            throw new BizException("SKU规格不能为空");
        }

        StringJoiner joiner = new StringJoiner("_");
        joiner.add(skuSpec.trim());
        if (attrs != null) {
            for (GoodsSkuAttrCreateDTO attr : attrs) {
                if (attr == null) {
                    continue;
                }
                String attrValue = attr.getAttrValue();
                if (attrValue != null && !attrValue.isBlank()) {
                    joiner.add(attrValue.trim());
                }
            }
        }
        return joiner.toString();
    }

    private List<GoodsSkuAttrCreateDTO> normalizeAttrs(List<GoodsSkuAttrCreateDTO> attrs) {
        if (attrs == null || attrs.isEmpty()) {
            return List.of();
        }

        List<GoodsSkuAttrCreateDTO> result = new ArrayList<>();
        for (int i = 0; i < attrs.size(); i++) {
            GoodsSkuAttrCreateDTO attr = attrs.get(i);
            if (attr == null) {
                continue;
            }
            if (attr.getAttrName() == null || attr.getAttrName().isBlank()) {
                throw new BizException("SKU属性名不能为空");
            }
            if (attr.getAttrValue() == null || attr.getAttrValue().isBlank()) {
                throw new BizException("SKU属性值不能为空");
            }
            if (attr.getSortOrder() == null) {
                attr.setSortOrder(i + 1);
            }
            result.add(attr);
        }
        return result;
    }

    private String buildSkuFullAttr(List<GoodsSkuAttrCreateDTO> attrs) {
        if (attrs.isEmpty()) {
            return null;
        }

        StringJoiner joiner = new StringJoiner(";");
        for (GoodsSkuAttrCreateDTO attr : attrs) {
            joiner.add(attr.getAttrName().trim() + ":" + attr.getAttrValue().trim());
        }
        return joiner.toString();
    }

    private void insertAttrs(String skuId, String creatorUserId, List<GoodsSkuAttrCreateDTO> attrs) {
        if (attrs.isEmpty()) {
            return;
        }

        for (GoodsSkuAttrCreateDTO attr : attrs) {
            GoodsSkuAttr po = new GoodsSkuAttr();
            po.setFId(IdGenerator.nextIdStr());
            po.setGoodsSkuId(skuId);
            po.setAttrName(attr.getAttrName().trim());
            po.setAttrValue(attr.getAttrValue().trim());
            po.setSortOrder(attr.getSortOrder());
            po.setFCreatorUserId(creatorUserId);
            po.setFDeleteMark(0);
            po.setFVersion(0);
            goodsSkuAttrMapper.insert(po);
        }
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private String normalizeJson(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String text = value.trim();
        try {
            OBJECT_MAPPER.readTree(text);
            return text;
        } catch (Exception e) {
            throw new BizException(fieldName + "必须是合法JSON字符串");
        }
    }
}
