package com.citymall.api.common.exception;



import com.citymall.api.common.api.Result;
import com.citymall.api.common.api.ResultCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author cqkir
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BizException.class)
  public Result<Void> handleBizException(BizException e) {
    return Result.fail(e.getCode(), e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldError() != null
            ? e.getBindingResult().getFieldError().getDefaultMessage()
            : "参数校验失败";
    return Result.fail(ResultCode.VALIDATE_FAILED, msg);
  }

  @ExceptionHandler(BindException.class)
  public Result<Void> handleBindException(BindException e) {
    String msg = e.getBindingResult().getFieldError() != null
            ? e.getBindingResult().getFieldError().getDefaultMessage()
            : "参数绑定失败";
    return Result.fail(ResultCode.VALIDATE_FAILED, msg);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
    return Result.fail(ResultCode.VALIDATE_FAILED, e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public Result<Void> handleException(Exception e) {
    return Result.fail(ResultCode.FAIL, e.getMessage());
  }
}