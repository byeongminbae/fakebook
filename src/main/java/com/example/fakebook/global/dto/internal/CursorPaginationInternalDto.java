package com.example.fakebook.global.dto.internal;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.interfaces.SortField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
@Builder
public class CursorPaginationInternalDto {
    private Long id;
    private Integer limit;
    private Sort.Direction sortDirection;
    private SortField sortField;

    public static CursorPaginationInternalDto from(
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ){
        return CursorPaginationInternalDto.builder()
                .id(cursorPaginationRequestDto.getId())
                .limit(cursorPaginationRequestDto.getLimit())
                .sortDirection(cursorPaginationRequestDto.getSortDirection())
                .sortField(sortField)
                .build();
    }
}
