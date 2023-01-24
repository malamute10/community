package com.personal.community.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommunityException extends Throwable{

    private HttpStatus status;
    private String message;


    public static CommunityException of(ExceptionEnum ex) {
        CommunityException exception = new CommunityException();
        exception.status = ex.getStatus();
        exception.message = ex.getMessage();
        return exception;
    }

    public static CommunityException of(ExceptionEnum ex, String message) {
        CommunityException exception = new CommunityException();
        exception.status = ex.getStatus();
        exception.message = message;
        return exception;
    }
}
