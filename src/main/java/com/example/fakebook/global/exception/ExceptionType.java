package com.example.fakebook.global.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    HttpStatus getHttpStatus();
    String getName();
}