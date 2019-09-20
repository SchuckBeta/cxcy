package com.oseasy.pw.modules.pw.exception;

public class CategoryException extends RuntimeException {

    private String code;

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryException(Throwable cause) {
        super(cause);
    }

    public CategoryException(String message, String code) {
        super(message);
        this.code = code;
    }

    public CategoryException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public CategoryException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
