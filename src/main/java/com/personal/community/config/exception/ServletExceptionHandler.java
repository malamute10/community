package com.personal.community.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServletExceptionHandler {

    @ExceptionHandler(CommunityException.class)
    public ResponseEntity<ExceptionResponse> CommunityExceptionHandler(CommunityException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ExceptionResponse.ofCreate(exception));
    }
}
