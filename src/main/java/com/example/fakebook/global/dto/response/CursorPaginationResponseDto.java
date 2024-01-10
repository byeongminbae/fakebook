package com.example.fakebook.global.dto.response;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.interfaces.SortField;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            SortField sortField,
            List<T> data,
            Function<T, R> runnable
    ) {
        boolean hasNext = hasNext(cursorPaginationRequestDto.getLimit(), data);
        StringBuffer nextCursor = new StringBuffer();

        if (hasNext) {
            removeHasNextEntity(data);
            T pageLastData = getPageLastData(data);
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
        if (Objects.nonNull(cursorPaginationRequestDto.getId())) {
            nextCursor.append("id=").append(cursorPaginationRequestDto.getId()).append("&");
        }
        nextCursor.append("limit=").append(cursorPaginationRequestDto.getLimit()).append("&");
        nextCursor.append("sortDirection=").append(cursorPaginationRequestDto.getSortDirection()).append("&");
        nextCursor.append("uniqueIdValue=").append(pageLastData.getId()).append("&");
        nextCursor.append("sortFieldValue=").append(getFieldValue(sortField.getFieldName(), pageLastData));

        List<Field> fields = List.of(cursorPaginationRequestDto.getClass().getDeclaredFields());
        for (Field field : fields) {
            Object fieldValue = getFieldValue(field, cursorPaginationRequestDto);

            if (Objects.nonNull(fieldValue)) {
                nextCursor.append("&").append(field.getName()).append("=").append(fieldValue);
            }
        }
    }

    private static <T> Object getFieldValue(String fieldName, T clazz) {
        try {
            List<Field> fields = new ArrayList<>();
            fields.addAll(List.of(clazz.getClass().getDeclaredFields()));
            fields.addAll(List.of(clazz.getClass().getSuperclass().getDeclaredFields()));
            Field field = fields.stream()
                    .filter((f) -> f.getName().equals(fieldName))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION));
            field.setAccessible(true);
            return field.get(clazz);
        } catch (IllegalAccessException e) {
            throw new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION);
        }
    }

    private static <T> Object getFieldValue(Field field, T clazz) {
        try {
            field.setAccessible(true);
            return field.get(clazz);
        } catch (IllegalAccessException e) {
            throw new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION);
        }
    }

    private static <T extends Base> T getPageLastData(List<T> data) {
        return data.stream()
                .skip(data.size() - 1)
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION));
    }

    private static <T extends Base> void removeHasNextEntity(List<T> data) {
        T lastData = data.stream()
                .skip(data.size() - 1)
                .findFirst()
                .orElseThrow(() -> new BusinessException(CommonException.GLOBAL_UNKNOWN_EXCEPTION));
        data.remove(lastData);
    }
}