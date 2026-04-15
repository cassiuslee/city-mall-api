package com.citymall.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回结构
 * @author cqkir
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 业务码（前端要求 string）
     */
    private String code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据（前端字段名必须是 result）
     */
    private T result;

    /**
     * 成功（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, "success", null);
    }

    /**
     * 成功（带数据）
     */
    public static <T> Result<T> success(T result) {
        return new Result<>(ResultCode.SUCCESS, "success", result);
    }

    /**
     * 成功（自定义消息）
     */
    public static <T> Result<T> success(String msg, T result) {
        return new Result<>(ResultCode.SUCCESS, msg, result);
    }

    /**
     * 失败
     */
    public static <T> Result<T> fail(String msg) {
        return new Result<>(ResultCode.FAIL, msg, null);
    }

    /**
     * 失败（自定义code）
     */
    public static <T> Result<T> fail(String code, String msg) {
        return new Result<>(code, msg, null);
    }
}