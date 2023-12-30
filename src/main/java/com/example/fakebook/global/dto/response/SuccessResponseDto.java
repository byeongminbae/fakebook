package com.example.fakebook.global.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class SuccessResponseDto<T> {
    private final String timestamp = LocalDateTime.now().toString();
    private final T data;

    public SuccessResponseDto() {
        data = null;
    }
}