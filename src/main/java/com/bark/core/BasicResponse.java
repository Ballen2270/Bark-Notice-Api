package com.bark.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author Ballen  2023/2/21 14:47
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicResponse<T> implements Serializable {
    private static String SUCCESS_CODE = "000000";
    private static String ERROR_CODE = "999999";
    private String code;
    private String msg;
    private T data;

    public static <T>BasicResponse successToClient(String msg, T data){
        return new BasicResponse<>(SUCCESS_CODE, msg, data);
    }

    public static <T>BasicResponse errorToClient(String code, String msg, T data){
        return new BasicResponse<>(code, msg, data);
    }

    public static <T>BasicResponse errorToClient(String msg, T data){
        return new BasicResponse<>(ERROR_CODE, msg, data);
    }

}
