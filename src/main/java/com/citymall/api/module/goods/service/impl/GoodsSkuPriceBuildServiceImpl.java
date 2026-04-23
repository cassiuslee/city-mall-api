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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SKU价格构建实现
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildBySpuId(String spuId) {
        // 你原来的按SPU逻辑保留
    }

    /**
     * 按客户类型重建SKU价格
     *
     * 实现思路：
     * 1. 先校验客户类型是否存在
     * 2. 删除该客户类型已有价格结果
     * 3. 以所有SKU当前原价生成默认价格
     * 4. 再读取命中该客户类型的规则项进行覆盖
     *
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
}