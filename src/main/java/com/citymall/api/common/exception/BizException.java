package com.citymall.api.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * @author cqkir
 */
@Getter
public class BizException extends RuntimeException {

  private final String code;

  public BizException(String message) {
    super(message);
    this.code = "0";
  }

  public BizException(String code, String message) {
    super(message);
    this.code = code;
  }
}