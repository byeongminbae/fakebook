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
import java.util.List;
import java.util.function.Function;

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
            Function<T, R> runnable
    ) {
        boolean hasNext = hasNext(cursorPaginationRequestDto.getLimit(), data);
        StringBuffer nextCursor = new StringBuffer();

        if (hasNext) {
            removeHasNextEntity(data);
            T pageLastData = getLastData(data);
            generateNextCursor(cursorPaginationRequestDto, sortField, nextCursor, pageLastData);
        }

        List<R> warpedData = data.stream()
                .map(runnable)
                .toList();

        return CursorPaginationResponseDto.<R>builder()
                .hasNext(hasNext)
                .nextCursor(nextCursor.toString())
                .data(warpedData)
                .count(data.size())
                .build();
    }

    private static <T extends Base> boolean hasNext(Integer limit, List<T> data) {
        return limit.compareTo(data.size()) < 0;
    }

    private static <T extends Base> void generateNextCursor(
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField,
            StringBuffer nextCursor,
            T pageLastData
    ) {
        nextCursor.append("?");
        if (nonNull(cursorPaginationRequestDto.getId())) {
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
                .append(pageLastData.getId())
                .append("&")
                .append("sortFieldValue=")
                .append(ReflectionUtil.getFieldValue(pageLastData, sortField.getSortFieldName()));

        for (Field field : cursorPaginationRequestDto.getClass().getDeclaredFields()) {
            Object fieldValue = ReflectionUtil.getFieldValue(cursorPaginationRequestDto, field.getName());

            if (nonNull(fieldValue)) {
                nextCursor.append("&").append(field.getName()).append("=").append(fieldValue);
            }
        }
    }


    private static <T extends Base> T getLastData(List<T> data) {
        return data.stream()
                .skip(data.size() - 1)
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.COMMON_UNKNOWN_EXCEPTION));
    }

    private static <T extends Base> void removeHasNextEntity(List<T> data) {
        T lastData = getLastData(data);
        data.remove(lastData);
    }
}