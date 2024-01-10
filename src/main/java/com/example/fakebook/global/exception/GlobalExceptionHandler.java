package com.example.fakebook.global.exception;

import com.example.fakebook.global.dto.response.ExceptionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionResponseDto> businessExceptionHandler(BusinessException businessException) {
        ExceptionResponseDto exceptionResponseDto = ExceptionResponseDto.builder()
                .statusCode(businessException.getExceptionType().getHttpStatus().value())
                .statusMessage(businessException.getExceptionType().getHttpStatus().getReasonPhrase())
                .exceptionType(businessException.getExceptionType().getName())
                .build();

        return ResponseEntity.ok(exceptionResponseDto);
    }
}
