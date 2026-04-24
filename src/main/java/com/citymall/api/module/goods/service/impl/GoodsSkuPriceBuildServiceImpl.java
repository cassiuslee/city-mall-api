package com.citymall.api.module.goods.service.impl;

import com.citymall.api.common.util.IdGenerator;
import com.citymall.api.module.customer.mapper.MarketTypeMapper;
import com.citymall.api.module.goods.entity.GoodsPriceRuleItem;
import com.citymall.api.module.goods.entity.GoodsSkuPrice;
import com.citymall.api.module.goods.mapper.GoodsPriceRuleItemMapper;
import com.citymall.api.module.goods.mapper.GoodsSkuMapper;
import com.citymall.api.module.goods.mapper.GoodsSkuPriceMapper;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import com.citymall.api.module.goods.vo.GoodsSkuBasePriceVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * SKU价格构建实现
 *
 * @author cqkir
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsSkuPriceBuildServiceImpl implements GoodsSkuPriceBuildService {

    private final GoodsSkuMapper goodsSkuMapper;
    private final GoodsSkuPriceMapper goodsSkuPriceMapper;
    private final GoodsPriceRuleItemMapper goodsPriceRuleItemMapper;
    private final MarketTypeMapper marketTypeMapper;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 按SPU重建SKU价格
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildBySpuId(String spuId) {

        log.info("开始按SPU重建SKU价格, spuId={}", spuId);

        // 1. 查询 SKU + 原价
        List<GoodsSkuBasePriceVO> skuList =
                goodsSkuMapper.selectSkuWithOriginalPriceBySpuId(spuId);

        if (skuList == null || skuList.isEmpty()) {
            log.warn("未查询到SKU数据, spuId={}", spuId);
            return;
        }

        // 2. 查询全部客户类型
        List<String> allMarketTypes = marketTypeMapper.selectAllEnabledEnCode();

        if (allMarketTypes == null || allMarketTypes.isEmpty()) {
            log.warn("未查询到客户类型");
            return;
        }

        // 3. 删除旧价格
        goodsSkuPriceMapper.deleteBySpuId(spuId);

        // 4. 初始化默认价（规格价）
        List<GoodsSkuPrice> insertList = new ArrayList<>();

        for (GoodsSkuBasePriceVO sku : skuList) {
            for (String marketType : allMarketTypes) {
                GoodsSkuPrice po = new GoodsSkuPrice();
                po.setFId(IdGenerator.nextIdStr());
                po.setRuleId(null);
                po.setSkuId(sku.getSkuId());
                po.setMarketType(marketType);
                po.setSalePrice(sku.getOriginalPrice());
                po.setFDeleteMark(0);
                insertList.add(po);
            }
        }

        if (!insertList.isEmpty()) {
            goodsSkuPriceMapper.batchInsert(insertList);
        }

        // 5. 查询规则项
        List<GoodsPriceRuleItem> items =
                goodsPriceRuleItemMapper.selectBySpuId(spuId);

        if (items == null || items.isEmpty()) {
            log.info("未配置规则，仅使用原价");
            return;
        }

        // 6. 排序（控制优先级）
        items.sort(Comparator.comparingInt(this::ruleOrder));

        // 7. 规则覆盖
        for (GoodsPriceRuleItem item : items) {

            log.info("处理规则: ruleItemId={}, goodsSpecId={}, skuIds={}, marketTypes={}, salePrice={}",
                    item.getFId(),
                    item.getGoodsSpecId(),
                    item.getSkuIds(),
                    item.getMarketTypes(),
                    item.getSalePrice());

            // 必须字段校验
            if (item.getGoodsSpecId() == null || item.getGoodsSpecId().isBlank()) {
                log.warn("规则缺少 goodsSpecId, 已跳过 ruleItemId={}", item.getFId());
                continue;
            }

            // 当前规格下全部 SKU
            List<String> specSkuIds = goodsSkuMapper.selectSkuIdsBySpuIdAndSpecId(
                    item.getSpuId(),
                    item.getGoodsSpecId()
            );

            if (specSkuIds == null || specSkuIds.isEmpty()) {
                log.warn("规格下未查询到SKU, ruleItemId={}, spuId={}, goodsSpecId={}",
                        item.getFId(), item.getSpuId(), item.getGoodsSpecId());
                continue;
            }

            List<String> targetSkuIds = split(item.getSkuIds());
            List<String> targetMarketTypes = split(item.getMarketTypes());

            log.info("规则解析结果: ruleItemId={}, targetSkuIds={}, targetMarketTypes={}",
                    item.getFId(), targetSkuIds, targetMarketTypes);

            boolean skuEmpty = targetSkuIds.isEmpty();
            boolean marketEmpty = targetMarketTypes.isEmpty();

            List<String> finalSkuIds;
            List<String> finalMarketTypes;

            // 1. 规格级
            if (skuEmpty && marketEmpty) {
                finalSkuIds = specSkuIds;
                finalMarketTypes = allMarketTypes;
            }
            // 2. SKU级
            else if (!skuEmpty && marketEmpty) {
                finalSkuIds = specSkuIds.stream()
                        .filter(targetSkuIds::contains)
                        .toList();
                finalMarketTypes = allMarketTypes;
            }
            // 3. marketType级
            else if (skuEmpty) {
                finalSkuIds = specSkuIds;
                finalMarketTypes = allMarketTypes.stream()
                        .filter(targetMarketTypes::contains)
                        .toList();
            }
            // 4. SKU + marketType交集
            else {
                finalSkuIds = specSkuIds.stream()
                        .filter(targetSkuIds::contains)
                        .toList();
                finalMarketTypes = allMarketTypes.stream()
                        .filter(targetMarketTypes::contains)
                        .toList();
            }

            log.info("规则命中结果: ruleItemId={}, finalSkuIds={}, finalMarketTypes={}",
                    item.getFId(), finalSkuIds, finalMarketTypes);

            if (finalSkuIds.isEmpty() || finalMarketTypes.isEmpty()) {
                log.warn("规则未命中任何SKU或客户类型, ruleItemId={}", item.getFId());
                continue;
            }

            int updateRows = goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                    finalSkuIds,
                    finalMarketTypes,
                    item.getSalePrice(),
                    item.getGoodsRuleId()
            );

            log.info("规则覆盖完成: ruleItemId={}, spec={}, skuCount={}, marketCount={}, price={}, updateRows={}",
                    item.getFId(),
                    item.getGoodsSpecId(),
                    finalSkuIds.size(),
                    finalMarketTypes.size(),
                    item.getSalePrice(),
                    updateRows);
        }

        log.info("SKU价格重建完成, spuId={}", spuId);
    }

    /**
     * 按客户类型重建SKU价格
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildByMarketType(String marketType) {
        log.info("开始按客户类型重建SKU价格, marketType={}", marketType);

        // 1. 校验客户类型是否存在
        Integer count = marketTypeMapper.countByEnCode(marketType);
        if (count == null || count <= 0) {
            log.warn("客户类型不存在, marketType={}", marketType);
            return;
        }

        // 2. 查询全部SKU及当前原价
        List<GoodsSkuBasePriceVO> skuList = goodsSkuMapper.selectAllSkuWithOriginalPrice();
        if (skuList == null || skuList.isEmpty()) {
            log.warn("未查询到任何SKU数据, marketType={}", marketType);
            return;
        }

        // 3. 删除该客户类型已有价格结果
        goodsSkuPriceMapper.deleteByMarketType(marketType);

        // 4. 先按当前原价生成默认价格
        List<GoodsSkuPrice> insertList = new ArrayList<>();
        for (GoodsSkuBasePriceVO sku : skuList) {
            GoodsSkuPrice po = new GoodsSkuPrice();
            po.setFId(IdGenerator.nextIdStr());
            po.setRuleId(null);
            po.setSkuId(sku.getSkuId());
            po.setMarketType(marketType);
            po.setSalePrice(sku.getOriginalPrice());
            po.setFDeleteMark(0);
            insertList.add(po);
        }

        if (!insertList.isEmpty()) {
            goodsSkuPriceMapper.batchInsert(insertList);
        }

        // 5. 查询包含该客户类型的规则项
        List<GoodsPriceRuleItem> items = goodsPriceRuleItemMapper.selectByMarketType(marketType);
        if (items == null || items.isEmpty()) {
            log.info("该客户类型无规则项，仅使用原价生成, marketType={}", marketType);
            return;
        }

        // 6. 按规则项覆盖价格
        for (GoodsPriceRuleItem item : items) {
            List<String> targetMarketTypes = split(item.getMarketTypes());
            List<String> targetSkuIds = split(item.getSkuIds());

            log.info("处理客户类型规则: ruleItemId={}, targetSkuIds={}, targetMarketTypes={}, currentMarketType={}",
                    item.getFId(), targetSkuIds, targetMarketTypes, marketType);

            // 这里只处理命中当前客户类型的规则
            if (!targetMarketTypes.contains(marketType)) {
                log.warn("规则未命中当前客户类型, ruleItemId={}, currentMarketType={}",
                        item.getFId(), marketType);
                continue;
            }

            // 优先按 sku_ids 覆盖
            if (!targetSkuIds.isEmpty()) {
                int updateRows = goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                        targetSkuIds,
                        Collections.singletonList(marketType),
                        item.getSalePrice(),
                        item.getGoodsRuleId()
                );
                log.info("按SKU覆盖完成: ruleItemId={}, updateRows={}", item.getFId(), updateRows);
                continue;
            }

            // 其次按 goods_spec_id 覆盖
            if (item.getGoodsSpecId() != null && !item.getGoodsSpecId().isBlank()) {
                List<String> skuIds = goodsSkuMapper.selectSkuIdsBySpuIdAndSpecId(
                        item.getSpuId(),
                        item.getGoodsSpecId()
                );

                if (skuIds != null && !skuIds.isEmpty()) {
                    int updateRows = goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                            skuIds,
                            Collections.singletonList(marketType),
                            item.getSalePrice(),
                            item.getGoodsRuleId()
                    );
                    log.info("按规格覆盖完成: ruleItemId={}, updateRows={}", item.getFId(), updateRows);
                } else {
                    log.warn("按规格覆盖失败，未命中SKU: ruleItemId={}", item.getFId());
                }
            }
        }

        log.info("按客户类型重建SKU价格完成, marketType={}", marketType);
    }

    /**
     * 字符串转列表
     * 兼容：
     * 1. 普通逗号字符串：1,2,3
     * 2. 单值字符串：1
     * 3. JSON数组字符串：["1","2"]
     */
    private List<String> split(String str) {
        if (str == null || str.isBlank()) {
            return Collections.emptyList();
        }

        String text = str.trim();

        try {
            if (text.startsWith("[") && text.endsWith("]")) {
                return OBJECT_MAPPER.readValue(text, new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            log.warn("JSON数组解析失败，回退普通split, value={}", str, e);
        }

        return Stream.of(text.split(","))
                .map(String::trim)
                .map(s -> s.replace("\"", ""))
                .filter(s -> !s.isBlank())
                .toList();
    }

    private int ruleOrder(GoodsPriceRuleItem item) {
        boolean skuEmpty = split(item.getSkuIds()).isEmpty();
        boolean marketEmpty = split(item.getMarketTypes()).isEmpty();

        // 规格级
        if (skuEmpty && marketEmpty) {
            return 1;
        }
        // marketType级
        if (skuEmpty) {
            return 2;
        }
        // SKU级
        if (marketEmpty) {
            return 3;
        }
        // 交集级
        return 4;
    }
}