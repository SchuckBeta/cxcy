package com.oseasy.com.pcore.common.exception;

/**
 * Created by Administrator on 2019/7/16 0016.
 */
public class PageException extends RuntimeException {
    public PageException() {
        super("页面未找到，请联系管理员");
    }

    public PageException(String message) {
        super(message);
    }
}
