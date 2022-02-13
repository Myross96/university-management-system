package com.foxminded.university.exceptions;

public class LectureNotReassignException extends ServiceException {

    private static final long serialVersionUID = 1L;
    
    public LectureNotReassignException(String message) {
        super(message);
    }
}
