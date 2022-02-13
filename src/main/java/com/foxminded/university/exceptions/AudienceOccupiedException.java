package com.foxminded.university.exceptions;

public class AudienceOccupiedException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public AudienceOccupiedException(String message) {
        super(message);
    }
}
