package com.foxminded.university.exceptions;

public class DataNotRecordedException extends DaoException {

    private static final long serialVersionUID = 1L;

    public DataNotRecordedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotRecordedException(String message) {
        super(message);
    }

    public DataNotRecordedException(Throwable cause) {
        super(cause);
    }
}
