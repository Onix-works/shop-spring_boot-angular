package com.me.example.exceptions;



public final class NullValueException extends RuntimeException {

    private static final long serialVersionUID = 5861310327366717163L;

    public NullValueException() {
        super();
    }

    public NullValueException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NullValueException(final String message) {
        super(message);
    }

    public NullValueException(final Throwable cause) {
        super(cause);
    }

}