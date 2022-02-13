package com.foxminded.university.exceptions;

public class DataNotUpdatedException extends DaoException {

    private static final long serialVersionUID = 1L;

    public DataNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotUpdatedException(String message) {
        super(message);
    }

    public DataNotUpdatedException(Throwable cause) {
        super(cause);
    }
}
