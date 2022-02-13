package com.foxminded.university.exceptions;

public class NotUniqueEntityException extends ServiceException{

    private static final long serialVersionUID = 1L;

    public NotUniqueEntityException(String message) {
        super(message);
    }

    public NotUniqueEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUniqueEntityException(Throwable cause) {
        super(cause);
    }
}
