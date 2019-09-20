package com.oseasy.pw.modules.pw.exception;

public class AppointmentException extends RuntimeException {

    private String code;

    public AppointmentException(String message) {
        super(message);
    }

    public AppointmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppointmentException(Throwable cause) {
        super(cause);
    }

    public AppointmentException(String message, String code) {
        super(message);
        this.code = code;
    }

    public AppointmentException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public AppointmentException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
