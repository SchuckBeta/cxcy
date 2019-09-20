package com.oseasy.dr.modules.dr.excetion;

public class ConnectionException  extends MessageExcetion {
    public  ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
