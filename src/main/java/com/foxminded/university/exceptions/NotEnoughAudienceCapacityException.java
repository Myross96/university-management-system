package com.foxminded.university.exceptions;

public class NotEnoughAudienceCapacityException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public NotEnoughAudienceCapacityException(String message) {
        super(message);
    }
}