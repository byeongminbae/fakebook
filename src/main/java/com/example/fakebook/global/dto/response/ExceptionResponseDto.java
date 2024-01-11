package com.example.fakebook.global.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponseDto {
    private final String timestamp = LocalDateTime.now().toString();
    private final Boolean isSuccess = false;
    private final Integer statusCode;
    private final String statusMessage;
    private final String exceptionType;
    private final StackTraceElement[] stackTraceElement;
}