package com.zck.aicodemother.common;



import com.zck.aicodemother.exception.ErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应类，用于封装API返回结果
 * @param <T> 泛型，表示返回数据的类型
 */
@Data
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码，表示请求处理结果的状态
     */
    private int code;

    /**
     * 响应数据，泛型类型，可以是任意类型的数据
     */
    private T data;

    /**
     * 响应消息，对处理结果的描述信息
     */
    private String message;

    /**
     * 全参数构造方法
     * @param code 状态码
     * @param data 响应数据
     * @param message 响应消息
     */
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 不带消息的构造方法
     * @param code 状态码
     * @param data 响应数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 使用错误码构造响应对象
     * @param errorCode 错误码枚举，包含状态码和错误信息
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}


