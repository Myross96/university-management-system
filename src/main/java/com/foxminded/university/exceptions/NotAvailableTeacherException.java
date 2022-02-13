package com.foxminded.university.exceptions;

public class NotAvailableTeacherException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public NotAvailableTeacherException(String message) {
        super(message);
    }
}
