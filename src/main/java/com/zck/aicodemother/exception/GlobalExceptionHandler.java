package com.zck.aicodemother.exception;


import com.zck.aicodemother.common.BaseResponse;
import com.zck.aicodemother.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

/**
 * 处理业务异常的方法
 * 当系统中抛出BusinessException异常时，此方法会被自动调用
 *
 * @param e 捕获到的业务异常对象
 * @return 返回一个包含错误信息的BaseResponse对象
 */
    @ExceptionHandler(BusinessException.class)  // 标识此方法用于处理BusinessException类型的异常
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);  // 记录异常信息到日志中
        return ResultUtils.error(e.getCode(), e.getMessage());  // 返回包含错误码和错误信息的响应
    }

/**
 * 全局异常处理类
 * 处理运行时异常(RuntimeException)的方法
 * 当系统中出现运行时异常时，会被此方法捕获并返回统一的错误响应
 *
 * @param e 运行时异常对象
 * @return 返回一个统一的错误响应对象，包含系统错误码和错误信息
 */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
    // 记录错误日志，包含异常堆栈信息
        log.error("RuntimeException", e);
    // 返回统一的错误响应，使用系统错误码和预设的错误信息
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}


