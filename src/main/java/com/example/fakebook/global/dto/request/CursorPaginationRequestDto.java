package com.example.fakebook.global.dto.request;

import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;


@Getter
public abstract class CursorPaginationRequestDto {
    @Parameter(description = "ID filter, initial page requested with an empty value")
    private final Long id;

    @Parameter(description = "Maximum number of data entries", example = "5", required = true)
    private final Integer limit;

    @Parameter(description = "Sorting direction", example = "DESC", required = true)
    private final Sort.Direction sortDirection;

    @Parameter(description = "Unique ID used when SortFieldValue is duplicated in DB")
    private final Long uniqueIdValue;

    @Parameter(description = "Values that vary depending on sortField")
    private final String sortFieldValue;

    private boolean validateCursor(){
        return nonNull(uniqueIdValue) && isNull(sortFieldValue) || isNull(uniqueIdValue) && nonNull(sortFieldValue);
    }

    public CursorPaginationRequestDto(Long id, Integer limit, Sort.Direction sortDirection, Long uniqueIdValue, String sortFieldValue) {
        this.id = id;
        this.limit = limit;
        this.sortDirection = sortDirection;
        this.uniqueIdValue = uniqueIdValue;
        this.sortFieldValue = sortFieldValue;

        if(validateCursor())
            throw new BusinessException(CommonException.COMMON_INVALID_INPUT_EXCEPTION);
    }

    public boolean isIdExists() {
        return nonNull(id);
    }
}
