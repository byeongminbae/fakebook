package com.example.fakebook.global.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class SuccessDataResponseDto<T> {
    private final String timestamp = LocalDateTime.now().toString();
    private final Boolean isSuccess = true;
    private final T data;
}