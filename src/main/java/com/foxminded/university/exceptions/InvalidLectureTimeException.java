package com.foxminded.university.exceptions;

public class InvalidLectureTimeException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public InvalidLectureTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLectureTimeException(String message) {
        super(message);
    }

    public InvalidLectureTimeException(Throwable cause) {
        super(cause);
    }
}
