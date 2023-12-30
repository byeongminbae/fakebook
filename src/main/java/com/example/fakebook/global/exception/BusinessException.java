package com.example.fakebook.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String status;
    private final String message;

    public BusinessException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.status = exceptionType.getStatus();
        this.message = exceptionType.getMessage();
    }
}