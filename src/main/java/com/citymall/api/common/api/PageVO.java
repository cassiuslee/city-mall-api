package com.citymall.api.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author cqkir
 */
@Data
@Schema(description = "分页返回")
public class PageVO<T> {

    @Schema(description = "总数")
    private long total;

    @Schema(description = "当前页")
    private long current;

    @Schema(description = "每页条数")
    private long size;

    @Schema(description = "数据列表")
    private List<T> records = Collections.emptyList();

    public static <T> PageVO<T> of(long total, long current, long size, List<T> records) {
        PageVO<T> vo = new PageVO<>();
        vo.setTotal(total);
        vo.setCurrent(current);
        vo.setSize(size);
        vo.setRecords(records);
        return vo;
    }
}