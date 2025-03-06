package com.bark.core;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理BasicError异常
     */
    @ExceptionHandler(BasicError.class)
    public BasicResponse<?> handleBasicError(BasicError e) {
        return BasicResponse.errorToClient(e.getCode(), e.getMsg(), null);
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public BasicResponse<?> handleException(Exception e) {
        return BasicResponse.errorToClient("系统异常：" + e.getMessage(), null);
    }
} 