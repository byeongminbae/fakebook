package com.example.fakebook.global.dto.response;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


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
            List<T> data,
            Function<T, R> runnable
    ) {
        boolean hasNext = hasNext(cursorPaginationRequestDto.getLimit(), data);
        StringBuffer nextCursor = new StringBuffer();

        if (hasNext) {
            T lastData = popLastEntity(data);
            generateNextCursor(cursorPaginationRequestDto, nextCursor, lastData);
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
            StringBuffer nextCursor,
            T lastData
    ) {
        nextCursor.append("?");
        nextCursor.append("id=").append(lastData.getId()).append("&");
        nextCursor.append("limit=").append(cursorPaginationRequestDto.getLimit()).append("&");
        nextCursor.append("sortDirection=").append(cursorPaginationRequestDto.getSortDirection());

        List<Field> fields = List.of(cursorPaginationRequestDto.getClass().getDeclaredFields());
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = getFieldValue(field, cursorPaginationRequestDto);

            if (!Objects.isNull(fieldValue)) {
                nextCursor.append("&").append(field.getName()).append("=").append(fieldValue);
            }
        }
    }

    private static Object getFieldValue(Field field, CursorPaginationRequestDto cursorPaginationRequestDto) {
        try {
            return field.get(cursorPaginationRequestDto);
        } catch (IllegalAccessException e) {
            throw new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION);
        }
    }

    private static <T extends Base> T popLastEntity(List<T> data) {
        T lastData = data.stream()
                .skip(data.size() - 1)
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION));
        data.remove(lastData);
        return lastData;
    }
}