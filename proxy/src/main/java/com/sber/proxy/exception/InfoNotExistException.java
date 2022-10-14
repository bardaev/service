package com.sber.proxy.exception;

public class InfoNotExistException extends RuntimeException {
    public InfoNotExistException() {
        super();
    }

    public InfoNotExistException(String message) {
        super(message);
    }

    public InfoNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfoNotExistException(Throwable cause) {
        super(cause);
    }

    protected InfoNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
