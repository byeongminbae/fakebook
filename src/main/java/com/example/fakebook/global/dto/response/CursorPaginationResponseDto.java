package com.example.fakebook.global.dto.response;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.interfaces.SortField;
import com.example.fakebook.global.util.ReflectionUtil;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


@Getter
@Builder
public class CursorPaginationResponseDto<T> {
    private final String timestamp = LocalDateTime.now().toString();
    private final Boolean isSuccess = true;
    private final Boolean hasNext;
    private final String nextCursor;
    private final Integer count;
    private final List<T> data;

    public static <T extends Base, R> CursorPaginationResponseDto<R> from(
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField,
            List<T> data,
            Function<T, R> function
    ) {
        List<R> warpedData = data.stream()
                .map(function)
                .collect(Collectors.toList());

        boolean hasNext = getHasNext(cursorPaginationRequestDto.getLimit(), warpedData);
        String nextCursor = "";
        if (hasNext) {
            removeLastData(warpedData);
            nextCursor = createNextCursor(cursorPaginationRequestDto, sortField, getLastData(warpedData));
        }

        return CursorPaginationResponseDto.<R>builder()
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .count(warpedData.size())
                .data(warpedData)
                .build();
    }

    private static <T> String createNextCursor(
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField,
            T pageLastData
    ) {
        StringBuffer nextCursor = new StringBuffer();

        nextCursor.append("?");
        if (cursorPaginationRequestDto.isIdExist()) {
            nextCursor.append("id=").append(cursorPaginationRequestDto.getId()).append("&");
        }

        nextCursor
                .append("limit=")
                .append(cursorPaginationRequestDto.getLimit())
                .append("&")
                .append("sortDirection=")
                .append(cursorPaginationRequestDto.getSortDirection())
                .append("&")
                .append("uniqueIdValue=")
                .append(ReflectionUtil.getFieldValue(pageLastData, "id"))
                .append("&")
                .append("sortFieldValue=")
                .append(ReflectionUtil.getFieldValue(pageLastData, sortField.getDtoFieldName()));

        for (Field field : cursorPaginationRequestDto.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            Object fieldValue = ReflectionUtil.getFieldValue(cursorPaginationRequestDto, fieldName);
            if (nonNull(fieldValue)) {
                nextCursor.append("&").append(fieldName).append("=").append(fieldValue);
            }
        }

        return nextCursor.toString();
    }

    private static boolean getHasNext(Integer limit, List<?> data) {
        return limit.compareTo(data.size()) < 0;
    }

    private static <T> T getLastData(List<T> data) {
        return data.stream()
                .skip(data.size() - 1)
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.COMMON_UNKNOWN_EXCEPTION));
    }

    private static void removeLastData(List<?> data) {
        data.remove(getLastData(data));
    }
}