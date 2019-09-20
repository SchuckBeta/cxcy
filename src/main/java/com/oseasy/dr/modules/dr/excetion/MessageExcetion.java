package com.oseasy.dr.modules.dr.excetion;

public class MessageExcetion extends RuntimeException {

    public MessageExcetion(String message) {
        super(message);
    }

    public MessageExcetion(Throwable e) {
        super(e);
    }

    public MessageExcetion(String message, Throwable cause) {
        super(message, cause);
    }
}
