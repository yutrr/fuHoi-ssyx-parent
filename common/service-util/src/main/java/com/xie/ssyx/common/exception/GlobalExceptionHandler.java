package com.xie.ssyx.common.exception;

import com.xie.ssyx.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @title: GlobalExceptionHandler
 * @Author Xie
 * @Date: 2023/9/3 20:41
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null);
    }

    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(SSYXException.class)
    @ResponseBody
    public Result error(SSYXException e){
        return Result.build(null,e.getCode(),e.getMessage());
    }
}
