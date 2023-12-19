package com.example.boilerplate.global.dto.response;

import com.example.boilerplate.global.util.TimeUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponseDto {
    private final LocalDateTime timestamp = TimeUtil.now();
    private final String status;
    private final String message;
}