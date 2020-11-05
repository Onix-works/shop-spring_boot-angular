package com.me.example.exceptions;

public final class EntityAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public EntityAlreadyExistException() {
        super();
    }

    public EntityAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistException(final String message) {
        super(message);
    }

    public EntityAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
