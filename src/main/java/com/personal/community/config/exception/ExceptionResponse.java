package com.personal.community.config.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private HttpStatus status;
    private String message;

    public static ExceptionResponse ofCreate(CommunityException ex){
        ExceptionResponse exceptionResponse= new ExceptionResponse();
        exceptionResponse.status = ex.getStatus();
        exceptionResponse.message = ex.getMessage();
        return exceptionResponse;
    }
}
