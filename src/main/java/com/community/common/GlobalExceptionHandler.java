package com.community.common;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 * @RestControllerAdvice — Spring 的全局拦截器，所有 Controller 抛出的异常都会经过这里
 * MethodArgumentNotValidException — @Valid 校验不通过时 Spring 自动抛这个异常，我们提取第一条错误消息返回给前端，而不是返回 500
 * 未知异常打印堆栈方便排查，但给前端只返回通用提示，不泄露内部信息
 */
public class GlobalExceptionHandler {

    // 业务异常：RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        return Result.error(400, e.getMessage());
    }

    // 参数校验失败：@Valid 触发的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(400, msg);
    }

    // 兜底：未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器内部错误");
    }
}
