package com.bark.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 参数验证异常处理器
 * 统一处理参数验证失败的情况，返回标准化的错误响应
 */
@Order(1)
@Slf4j
@RestControllerAdvice
public class ValidateHandler {

    /**
     * 处理POST请求参数校验异常
     * 当使用@Valid或@Validated注解验证参数失败时抛出此异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BasicResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errorList = bindingResult.getAllErrors();

        // 获取第一个验证错误信息
        String errorMessage = errorList.get(0).getDefaultMessage();

        log.warn("参数验证失败: {}", errorMessage);
        return BasicResponse.errorToClient(ErrorCode.SYSTEM_ERROR, errorMessage);
    }

    /**
     * 处理GET请求参数缺失异常
     * 当required=true的请求参数缺失时抛出此异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BasicResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String errorMessage = String.format("缺少必填参数: %s", e.getParameterName());
        log.warn("参数缺失: {}", errorMessage);
        return BasicResponse.errorToClient(ErrorCode.SYSTEM_ERROR, errorMessage);
    }

    /**
     * 处理HTTP消息不可读异常
     * 当请求体格式错误或无法解析时抛出此异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BasicResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String errorMessage = "请求参数格式错误，请检查JSON格式或参数类型";
        log.warn("请求体解析失败: {}", e.getMessage());
        return BasicResponse.errorToClient(ErrorCode.SYSTEM_ERROR, errorMessage);
    }

}
