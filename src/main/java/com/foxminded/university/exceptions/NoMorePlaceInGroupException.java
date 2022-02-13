package com.foxminded.university.exceptions;

public class NoMorePlaceInGroupException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public NoMorePlaceInGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMorePlaceInGroupException(String message) {
        super(message);
    }

    public NoMorePlaceInGroupException(Throwable cause) {
        super(cause);
    }
}
