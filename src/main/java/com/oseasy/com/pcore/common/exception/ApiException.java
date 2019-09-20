package com.oseasy.com.pcore.common.exception;

/**
 * Api接口异常处理.
 * Created by Administrator on 2019/7/16 0016.
 */
public class ApiException extends RuntimeException {
    public ApiException() {
        super("接口请求失败！");
    }

    public ApiException(String message) {
        super(message);
    }
}
