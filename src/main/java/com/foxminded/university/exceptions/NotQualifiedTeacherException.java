package com.foxminded.university.exceptions;

public class NotQualifiedTeacherException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public NotQualifiedTeacherException(String message) {
        super(message);
    }
}
