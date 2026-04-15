package com.citymall.api.common.exception;

import com.citymall.api.common.api.Result;
import com.citymall.api.common.api.ResultCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author cqkir
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * 业务异常
   */
  @ExceptionHandler(BizException.class)
  public Result<Void> handleBizException(BizException e) {
    log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
    return Result.fail(e.getCode(), e.getMessage());
  }

  /**
   * @RequestBody 参数校验异常
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldError() != null
            ? e.getBindingResult().getFieldError().getDefaultMessage()
            : "参数校验失败";
    return Result.fail(ResultCode.VALIDATE_FAILED, msg);
  }

  /**
   * 表单参数校验异常
   */
  @ExceptionHandler(BindException.class)
  public Result<Void> handleBindException(BindException e) {
    String msg = e.getBindingResult().getFieldError() != null
            ? e.getBindingResult().getFieldError().getDefaultMessage()
            : "参数绑定失败";
    return Result.fail(ResultCode.VALIDATE_FAILED, msg);
  }

  /**
   * 单参数校验异常
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
    return Result.fail(ResultCode.VALIDATE_FAILED, e.getMessage());
  }

  /**
   * 未知异常
   */
  @ExceptionHandler(Exception.class)
  public Result<Void> handleException(Exception e) {
    log.error("系统异常", e);
    return Result.fail(ResultCode.FAIL, "系统繁忙，请稍后重试");
  }
}