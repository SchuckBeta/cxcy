package com.oseasy.act.modules.actyw.vo;

public class ActYwRuntimeException extends RuntimeException {
    public ActYwRuntimeException() {
    }

    public ActYwRuntimeException(String message) {
        super(message);
    }

    public ActYwRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActYwRuntimeException(Throwable cause) {
        super(cause);
    }

    public ActYwRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
