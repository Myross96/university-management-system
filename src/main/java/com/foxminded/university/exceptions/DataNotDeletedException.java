package com.foxminded.university.exceptions;

public class DataNotDeletedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataNotDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotDeletedException(String message) {
        super(message);
    }

    public DataNotDeletedException(Throwable cause) {
        super(cause);
    }
}
