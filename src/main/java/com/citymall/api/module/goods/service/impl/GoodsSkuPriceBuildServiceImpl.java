package com.citymall.api.module.goods.service.impl;

import com.citymall.api.common.util.IdGenerator;
import com.citymall.api.module.customer.mapper.MarketTypesMapper;
import com.citymall.api.module.goods.entity.GoodsPriceRule;
import com.citymall.api.module.goods.entity.GoodsPriceRuleItem;
import com.citymall.api.module.goods.entity.GoodsSkuPrice;
import com.citymall.api.module.goods.mapper.*;
import com.citymall.api.module.goods.service.GoodsSkuPriceBuildService;
import com.citymall.api.module.goods.vo.GoodsSkuBasePriceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * SKU价格构建实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsSkuPriceBuildServiceImpl implements GoodsSkuPriceBuildService {

    private final GoodsSkuMapper goodsSkuMapper;
    private final GoodsSkuPriceMapper goodsSkuPriceMapper;
    private final GoodsPriceRuleMapper goodsPriceRuleMapper;
    private final GoodsPriceRuleItemMapper goodsPriceRuleItemMapper;
    private final MarketTypesMapper marketTypeMapper;

    /**
     * 按SPU重建SKU价格
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildBySpuId(String spuId) {

        log.info("开始重建SKU价格, spuId={}", spuId);

        // ========================
        // 1. 查询 SKU + 原价
        // ========================
        List<GoodsSkuBasePriceVO> skuList =
                goodsSkuMapper.selectSkuWithOriginalPriceBySpuId(spuId);

        if (skuList.isEmpty()) {
            log.warn("未找到SKU数据, spuId={}", spuId);
            return;
        }

        // ========================
        // 2. 查询客户类型（en_code）
        // ========================
        List<String> marketTypes = marketTypeMapper.selectAllEnabledEnCode();

        if (marketTypes.isEmpty()) {
            log.warn("未找到客户类型");
            return;
        }

        // ========================
        // 3. 删除旧价格
        // ========================
        goodsSkuPriceMapper.deleteBySpuId(spuId);

        // ========================
        // 4. 生成默认价格（原价）
        // ========================
        List<GoodsSkuPrice> insertList = new ArrayList<>();

        for (GoodsSkuBasePriceVO sku : skuList) {
            for (String marketType : marketTypes) {

                GoodsSkuPrice po = new GoodsSkuPrice();

                po.setFId(IdGenerator.nextIdStr());
                po.setSkuId(sku.getSkuId());
                po.setMarketType(marketType);
                po.setSalePrice(sku.getOriginalPrice());

                insertList.add(po);
            }
        }

        // 批量插入（可后续优化分批）
        if (!insertList.isEmpty()) {
            goodsSkuPriceMapper.batchInsert(insertList);
        }

        // ========================
        // 5. 查询规则主表
        // ========================
        GoodsPriceRule rule = goodsPriceRuleMapper.selectBySpuId(spuId);

        if (rule == null) {
            log.info("未配置价格规则，仅使用原价");
            return;
        }

        // ========================
        // 6. 查询规则项
        // ========================
        List<GoodsPriceRuleItem> items =
                goodsPriceRuleItemMapper.selectByRuleId(rule.getFId());

        if (items == null || items.isEmpty()) {
            log.info("规则项为空，仅使用原价");
            return;
        }

        // ========================
        // 7. 规则覆盖
        // ========================
        for (GoodsPriceRuleItem item : items) {

            List<String> targetMarketTypes = split(item.getMarketTypes());
            List<String> targetSkuIds = split(item.getSkuIds());

            // 优先级1：sku_ids
            if (!targetSkuIds.isEmpty()) {
                goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                        targetSkuIds,
                        targetMarketTypes,
                        item.getSalePrice(),
                        rule.getFId()
                );
                continue;
            }

            // 优先级2：goods_spec_id
            if (item.getGoodsSpecId() != null) {
                List<String> skuIds =
                        goodsSkuMapper.selectSkuIdsBySpuIdAndSpecId(
                                spuId,
                                item.getGoodsSpecId()
                        );

                if (!skuIds.isEmpty()) {
                    goodsSkuPriceMapper.updateSalePriceBySkuIdsAndMarketTypes(
                            skuIds,
                            targetMarketTypes,
                            item.getSalePrice(),
                            rule.getFId()
                    );
                }
            }
        }

        log.info("SKU价格重建完成, spuId={}", spuId);
    }

    /**
     * 工具方法：字符串转List
     */
    private List<String> split(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(","));
    }
}