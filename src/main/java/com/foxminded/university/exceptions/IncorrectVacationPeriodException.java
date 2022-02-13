package com.foxminded.university.exceptions;

public class IncorrectVacationPeriodException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public IncorrectVacationPeriodException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectVacationPeriodException(String message) {
        super(message);
    }

    public IncorrectVacationPeriodException(Throwable cause) {
        super(cause);
    }
}
