package com.citymall.api.common.api;

/**
 * 统一业务状态码
 */
public interface ResultCode {

    /**
     * 成功
     */
    String SUCCESS = "1";

    /**
     * 通用失败
     */
    String FAIL = "0";

    /**
     * 参数错误
     */
    String VALIDATE_FAILED = "400";

    /**
     * 未登录
     */
    String UNAUTHORIZED = "401";

    /**
     * 无权限
     */
    String FORBIDDEN = "403";
}