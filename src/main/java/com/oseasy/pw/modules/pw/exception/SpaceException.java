package com.oseasy.pw.modules.pw.exception;

public class SpaceException extends RuntimeException {

    private String code;

    public SpaceException(String message) {
        super(message);
    }

    public SpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpaceException(Throwable cause) {
        super(cause);
    }

    public SpaceException(String message, String code) {
        super(message);
        this.code = code;
    }

    public SpaceException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public SpaceException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
