package com.example.boilerplate.global.dto.response;

import com.example.boilerplate.global.util.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class SuccessResponseDto<T> {
    private final LocalDateTime timestamp = TimeUtil.now();
    private final T data;
    public SuccessResponseDto() {
        data = null;
    }
}