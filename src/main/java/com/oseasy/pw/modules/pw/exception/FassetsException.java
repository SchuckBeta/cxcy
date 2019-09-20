package com.oseasy.pw.modules.pw.exception;

public class FassetsException extends RuntimeException {

    private String code;

    public FassetsException(String message) {
        super(message);
    }

    public FassetsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FassetsException(Throwable cause) {
        super(cause);
    }

    public FassetsException(String message, String code) {
        super(message);
        this.code = code;
    }

    public FassetsException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public FassetsException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
