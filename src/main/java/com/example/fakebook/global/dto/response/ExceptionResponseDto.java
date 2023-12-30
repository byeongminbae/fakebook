package com.example.fakebook.global.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponseDto {
    private final String timestamp = LocalDateTime.now().toString();
    private final String status;
    private final String message;
}