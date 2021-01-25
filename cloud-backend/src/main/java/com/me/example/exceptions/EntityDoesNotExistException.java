package com.me.example.exceptions;

public final class EntityDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 8561310537534287163L;

    public EntityDoesNotExistException() {
        super();
    }

    public EntityDoesNotExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityDoesNotExistException(final String message) {
        super(message);
    }

    public EntityDoesNotExistException(final Throwable cause) {
        super(cause);
    }

}