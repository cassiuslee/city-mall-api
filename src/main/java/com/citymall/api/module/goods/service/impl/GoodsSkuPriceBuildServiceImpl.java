package com.citymall.api.module.goods.service.impl;

import com.citymall.api.common.util.IdGenerator;
import com.citymall.api.module.customer.mapper.MarketTypeMapper;
import com.citymall.api.module.goods.entity.GoodsPriceRule;
import com.citymall.api.module.goods.entity.GoodsPriceRuleItem;
import com.citymall.api.module.goods.entity.GoodsSkuPrice;
import com.citymall.api.module.goods.mapper.GoodsPriceRuleItemMapper;
import com.citymall.api.module.goods.mapper.GoodsPriceRuleMapper;
import com.citymall.api.module.goods.mapper.GoodsSkuMapper;
import com.citymall.api.module.goods.mapper.GoodsSkuPriceMapper;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import com.citymall.api.module.goods.vo.GoodsSkuBasePriceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final GoodsPriceRuleMapper goodsPriceRuleMapper;
    private final GoodsPriceRuleItemMapper goodsPriceRuleItemMapper;
    private final MarketTypeMapper marketTypeMapper;

    /**
     * 按SPU重建SKU价格
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildBySpuId(String spuId) {

        log.info("开始按SPU重建SKU价格, spuId={}", spuId);

        // ========================
        // 1. 查询 SKU + 原价（按 spuId + goods_spec_id）
        // ========================
        List<GoodsSkuBasePriceVO> skuList =
                goodsSkuMapper.selectSkuWithOriginalPriceBySpuId(spuId);

        if (skuList == null || skuList.isEmpty()) {
            log.warn("未查询到SKU数据, spuId={}", spuId);
            return;
        }

        // ========================
        // 2. 查询全部客户类型
        // ========================
        List<String> allMarketTypes = marketTypeMapper.selectAllEnabledEnCode();

        if (allMarketTypes == null || allMarketTypes.isEmpty()) {
            log.warn("未查询到客户类型");
            return;
        }

        // ========================
        // 3. 删除旧价格
        // ========================
        goodsSkuPriceMapper.deleteBySpuId(spuId);

        // ========================
        // 4. 初始化默认价（规格价）
        // ========================
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

        // ========================
        // 5. 查询规则项
        // ========================
        List<GoodsPriceRuleItem> items =
                goodsPriceRuleItemMapper.selectBySpuId(spuId);

        if (items == null || items.isEmpty()) {
            log.info("未配置规则，仅使用原价");
            return;
        }

        // ========================
        // 6. 排序（控制优先级）
        // ========================
        items.sort(Comparator.comparingInt(this::ruleOrder));

        // ========================
        // 7. 规则覆盖
        // ========================
        for (GoodsPriceRuleItem item : items) {

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
                continue;
            }

            List<String> targetSkuIds = split(item.getSkuIds());
            List<String> targetMarketTypes = split(item.getMarketTypes());

            boolean skuEmpty = targetSkuIds.isEmpty();
            boolean marketEmpty = targetMarketTypes.isEmpty();

            List<String> finalSkuIds;
            List<String> finalMarketTypes;

            // ========================
            // 1. 规格级
            // ========================
            if (skuEmpty && marketEmpty) {
                finalSkuIds = specSkuIds;
                finalMarketTypes = allMarketTypes;
            }
            // ========================
            // 2. SKU级
            // ========================
            else if (!skuEmpty && marketEmpty) {
                finalSkuIds = specSkuIds.stream()
                        .filter(targetSkuIds::contains)
                        .toList();
                finalMarketTypes = allMarketTypes;
            }
            // ========================
            // 3. marketType级
            // ========================
            else if (skuEmpty) {
                finalSkuIds = specSkuIds;
                finalMarketTypes = allMarketTypes.stream()
                        .filter(targetMarketTypes::contains)
                        .toList();
            }
            // ========================
            // 4. SKU + marketType交集
            // ========================
            else {
                finalSkuIds = specSkuIds.stream()
                        .filter(targetSkuIds::contains)
                        .toList();
                finalMarketTypes = allMarketTypes.stream()
                        .filter(targetMarketTypes::contains)
                        .toList();
            }

            if (finalSkuIds.isEmpty() || finalMarketTypes.isEmpty()) {
                continue;
            }

            goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                    finalSkuIds,
                    finalMarketTypes,
                    item.getSalePrice(),
                    item.getGoodsRuleId()
            );

            log.info("规则覆盖完成: ruleId={}, spec={}, skuCount={}, marketCount={}, price={}",
                    item.getFId(),
                    item.getGoodsSpecId(),
                    finalSkuIds.size(),
                    finalMarketTypes.size(),
                    item.getSalePrice());
        }

        log.info("SKU价格重建完成, spuId={}", spuId);
    }

    /**
     * 按客户类型重建SKU价格
     * <p>
     * 实现思路：
     * 1. 先校验客户类型是否存在
     * 2. 删除该客户类型已有价格结果
     * 3. 以所有SKU当前原价生成默认价格
     * 4. 再读取命中该客户类型的规则项进行覆盖
     * <p>
     * 注意：
     * 1. sale_price 为固化价格
     * 2. 本方法只刷新一个客户类型，不影响其他客户类型
     *
     * @param marketType 客户类型编码
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

        // 批量插入默认价格
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

            // 这里只处理命中当前客户类型的规则
            if (!targetMarketTypes.contains(marketType)) {
                continue;
            }

            // 优先按 sku_ids 覆盖
            if (!targetSkuIds.isEmpty()) {
                goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                        targetSkuIds,
                        Collections.singletonList(marketType),
                        item.getSalePrice(),
                        item.getGoodsRuleId()
                );
                continue;
            }

            // 其次按 goods_spec_id 覆盖
            if (item.getGoodsSpecId() != null && !item.getGoodsSpecId().isBlank()) {
                List<String> skuIds = goodsSkuMapper.selectSkuIdsBySpuIdAndSpecId(
                        item.getSpuId(),
                        item.getGoodsSpecId()
                );

                if (skuIds != null && !skuIds.isEmpty()) {
                    goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                            skuIds,
                            Collections.singletonList(marketType),
                            item.getSalePrice(),
                            item.getGoodsRuleId()
                    );
                }
            }
        }

        log.info("按客户类型重建SKU价格完成, marketType={}", marketType);
    }

    /**
     * 逗号分隔字符串转列表
     *
     * @param str 原始字符串
     * @return 拆分后的列表
     */
    private List<String> split(String str) {
        if (str == null || str.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(","))
                .map(String::trim)
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