package com.foxminded.university.exceptions;

public class LectureOnWeekendException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public LectureOnWeekendException(String message) {
        super(message);
    }
}
