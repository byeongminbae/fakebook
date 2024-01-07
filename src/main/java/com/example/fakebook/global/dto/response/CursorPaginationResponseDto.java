package com.example.fakebook.global.dto.response;

import com.example.fakebook.global.dto.internal.CursorPaginationInternalDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


@Getter
@Builder
public class CursorPaginationResponseDto<T> {
    private final String timestamp = LocalDateTime.now().toString();
    private final Boolean isSuccess = true;
    private final Boolean hasNext;
    private final String nextCursor;
    private final Integer count;
    private final List<T> data;

    public static <E extends Base, R> CursorPaginationResponseDto<R> from(
            CursorPaginationInternalDto cursorPaginationInternalDto,
            List<E> data,
            Function<E, R> runnable
    ) {
        boolean hasNext = hasNext(cursorPaginationInternalDto, data);

        StringBuffer nextCursor = new StringBuffer();

        if (hasNext) {
            E lastData = getLastData(cursorPaginationInternalDto, data);
            data.remove(lastData);

            nextCursor.append("?");
            nextCursor.append("id=").append(lastData.getId()).append("&");
            nextCursor.append("limit=").append(cursorPaginationInternalDto.getLimit()).append("&");
            nextCursor.append("sortDirection=").append(cursorPaginationInternalDto.getSortDirection()).append("&");
            nextCursor.append("sortField=").append(cursorPaginationInternalDto.getSortField());
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

    private static <E> boolean hasNext(CursorPaginationInternalDto cursorPaginationInternalDto, List<E> data) {
        return cursorPaginationInternalDto.getLimit().compareTo(data.size()) < 0;
    }

    private static <E extends Base> E getLastData(CursorPaginationInternalDto cursorPaginationInternalDto, List<E> data) {
        return data.stream()
                .skip(cursorPaginationInternalDto.getLimit())
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION));
    }
}