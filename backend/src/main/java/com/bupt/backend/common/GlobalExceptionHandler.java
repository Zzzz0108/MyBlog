package com.bupt.backend.common;// GlobalExceptionHandler.java
import org.springframework.validation.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j  // 添加日志注解
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 修正参数校验异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "参数错误"
                ));
        return Result.error(400, "参数校验失败").data(errors);
    }

    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException ex) {
        return Result.error(ex.getCode(), ex.getMessage());
    }

    // 系统异常处理
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(500, "系统繁忙，请稍后再试");
    }
}