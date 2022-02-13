package com.foxminded.university.exceptions;

public class TeacherInVacationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TeacherInVacationException(String message) {
        super(message);
    }
}
