package com.xie.ssyx.common.exception;

import com.xie.ssyx.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @title: FhxgException
 * @Author Xie
 * @Date: 2023/9/3 20:42
 * @Version 1.0
 */

@Data
public class MinioException extends RuntimeException{
    //异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public MinioException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public MinioException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "MinioException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
