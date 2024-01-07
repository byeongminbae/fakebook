package com.example.fakebook.global.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class CursorPaginationResponseDto<T> {
    private final String timestamp = LocalDateTime.now().toString();
    private final Boolean isSuccess = true;
    private final Boolean hasNext;
    private final String nextCursor;
    private final List<T> data;
}