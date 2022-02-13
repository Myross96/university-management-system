package com.foxminded.university.exceptions;

public class TeacherIsBusyException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public TeacherIsBusyException(String message) {
        super(message);
    }
}
