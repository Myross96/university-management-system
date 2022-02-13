package com.foxminded.university.exceptions;

public class GroupDoesNotLearnLectureCourseException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public GroupDoesNotLearnLectureCourseException(String message) {
        super(message);
    }
}
