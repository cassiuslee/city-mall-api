package com.citymall.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页统一返回
 * @author cqkir
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> list;

    private Long total;

    private Long pageNum;

    private Long pageSize;

    public static <T> PageResult<T> of(List<T> list, Long total, Long pageNum, Long pageSize) {
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}