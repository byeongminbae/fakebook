package com.example.boilerplate.global.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
public class SuccessVoidResponseDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
}