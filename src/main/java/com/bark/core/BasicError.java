package com.bark.core;

import lombok.Getter;

/**
 * 基础异常类
 */
@Getter
public class BasicError extends RuntimeException {
    private final String code;
    private final String msg;

    /**
     * 使用默认错误码构造异常
     * @param msg 错误信息
     */
    public BasicError(String msg) {
        super(msg);
        this.code = BasicResponse.ERROR_CODE;
        this.msg = msg;
    }

    /**
     * 使用自定义错误码构造异常
     * @param code 错误码
     * @param msg 错误信息
     */
    public BasicError(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
} 