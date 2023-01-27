package com.personal.community.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없음"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 만료"),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재 합니다."),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 불일치"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한 없음");

    private HttpStatus status;
    private String message;

    ExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
