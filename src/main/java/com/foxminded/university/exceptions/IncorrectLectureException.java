package com.foxminded.university.exceptions;

public class IncorrectLectureException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public IncorrectLectureException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectLectureException(String message) {
        super(message);
    }

    public IncorrectLectureException(Throwable cause) {
        super(cause);
    }
}
