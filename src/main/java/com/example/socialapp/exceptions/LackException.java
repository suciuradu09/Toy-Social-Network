package com.example.socialapp.exceptions;

/**
 * Exception for an entity that doesn't exist
 */
public class LackException extends RuntimeException {
    public LackException() {
        super();
    }

    public LackException(String message) {
        super(message);
    }

    public LackException(String message, Throwable cause) {
        super(message, cause);
    }

    public LackException(Throwable cause) {
        super(cause);
    }

    protected LackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
