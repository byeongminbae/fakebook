package com.example.fakebook.global.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class SuccessVoidResponseDto {
    private final String timestamp = LocalDateTime.now().toString();
}