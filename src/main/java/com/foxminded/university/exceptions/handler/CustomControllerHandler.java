package com.foxminded.university.exceptions.handler;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.foxminded.university.exceptions.ServiceException;

@ControllerAdvice
public class CustomControllerHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleAudienceOccupiedException(ServiceException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorMessge(ex), HttpStatus.CONFLICT);
    }

    private Map<String, Object> getErrorMessge(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalTime.now());
        body.put("message", ex.getMessage());
        return body;
    }
}
