package com.zwn.trainserverspringboot.exception;

import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 异常处理类
 * controller层异常无法捕获处理，需要自己处理
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {
	/**
     * 处理所有自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public Result handleCustomException(CustomException e){
        log.error(e.getResult().getMessage());
        return e.getResult();
    }
    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getField() + e.getBindingResult().getFieldError().getDefaultMessage());
        return Result.getResult(ResultCodeEnum.BAD_REQUEST, e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
