package com.citymall.api.common.api;

/**
 * @author cqkir
 */
public interface ResultCode {
    Integer SUCCESS = 200;
    Integer FAIL = 500;
    Integer VALIDATE_FAILED = 400;
    Integer UNAUTHORIZED = 401;
    Integer FORBIDDEN = 403;
}