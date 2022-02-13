package com.foxminded.university.exceptions;

public class LectureOnHolidayException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public LectureOnHolidayException(String message) {
        super(message);
    }
}
