package com.citymall.api.module.customer.service.impl;

import com.citymall.api.common.exception.BizException;
import com.citymall.api.module.customer.dto.MemberUserInfoQueryDTO;
import com.citymall.api.module.customer.mapper.MemberUserMapper;
import com.citymall.api.module.customer.service.MemberUserQueryService;
import com.citymall.api.module.customer.vo.MarketEntityVO;
import com.citymall.api.module.customer.vo.MemberUserInfoRowVO;
import com.citymall.api.module.customer.vo.MemberUserInfoVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author cqkir
 */
@Service
@RequiredArgsConstructor
public class MemberUserQueryServiceImpl implements MemberUserQueryService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };

    private final MemberUserMapper memberUserMapper;

    @Override
    public MemberUserInfoVO getMemberUserInfoByUserId(MemberUserInfoQueryDTO queryDTO) {
        if (queryDTO == null || queryDTO.getUserId() == null || queryDTO.getUserId().isBlank()) {
            throw new BizException("userId不能为空");
        }

        List<MemberUserInfoRowVO> rows = memberUserMapper.selectMemberUserInfoByUserId(queryDTO);

        if (rows == null || rows.isEmpty()) {
            throw new BizException("用户不存在");
        }

        MemberUserInfoRowVO first = rows.get(0);

        MemberUserInfoVO result = new MemberUserInfoVO();
        result.setFId(first.getFId());
        result.setCStatus(first.getCStatus());
        result.setUserId(first.getUserId());
        result.setMobile(first.getMobile());
        result.setNickname(first.getNickname());
        result.setAvatar(first.getAvatar());
        result.setGender(first.getGender());
        result.setOpenid(first.getOpenid());
        result.setUnionId(first.getUnionId());

        Set<String> marketKeys = new HashSet<>();

        for (MemberUserInfoRowVO row : rows) {
            if (row.getMarketFid() == null || row.getMarketFid().isBlank()) {
                continue;
            }

            String deduplicateKey = buildMarketDeduplicateKey(row);
            if (!marketKeys.add(deduplicateKey)) {
                continue;
            }

            MarketEntityVO market = new MarketEntityVO();
            market.setRelationId(row.getRelationId());
            market.setMarketFid(row.getMarketFid());
            market.setMemberIdentity(row.getMemberIdentity());
            market.setCStatus(row.getMarketCStatus());
            market.setMarkCode(row.getMarkCode());
            market.setMarkName(row.getMarkName());
            market.setMarkType(row.getMarkType());
            market.setMarketTypeName(row.getMarketTypeName());
            market.setWeworkDptId(row.getWeworkDptId());
            market.setWeworkDptName(row.getWeworkDptName());
            market.setSalesCompanyId(row.getSalesCompanyId());
            market.setRegionalManagerId(row.getRegionalManagerId());
            market.setServiceManager(row.getServiceManager());
            market.setPermissions(parsePermissions(row.getIdentityPermissions()));

            result.getMarkets().add(market);
        }

        return result;
    }

    private String buildMarketDeduplicateKey(MemberUserInfoRowVO row) {
        String relationId = row.getRelationId() == null ? "" : row.getRelationId();
        String marketFid = row.getMarketFid() == null ? "" : row.getMarketFid();
        return relationId + "_" + marketFid;
    }

    private List<String> parsePermissions(String identityPermissions) {
        if (identityPermissions == null || identityPermissions.isBlank()) {
            return new ArrayList<>();
        }

        try {
            List<String> permissions = OBJECT_MAPPER.readValue(identityPermissions, STRING_LIST_TYPE);
            if (permissions == null || permissions.isEmpty()) {
                return new ArrayList<>();
            }
            return new ArrayList<>(new LinkedHashSet<>(permissions));
        } catch (JsonProcessingException ex) {
            return new ArrayList<>();
        }
    }
}
