package com.example.fakebook.global.dto.request;

import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static java.util.Objects.*;


@Getter
@Setter
public abstract class CursorPaginationRequestDto {
    @Parameter(description = "ID filter, initial page requested with an empty value")
    private Long id;
    @Parameter(description = "Maximum number of data entries", example = "5", required = true)
    private Integer limit;
    @Parameter(description = "Sorting direction", example = "DESC", required = true)
    private Sort.Direction sortDirection;

    @Parameter(description = "Unique ID used when SortFieldValue is duplicated in DB")
    private Long uniqueIdValue;
    @Parameter(description = "Values that vary depending on sortField")
    private String sortFieldValue;

    public void validate() {
        if (!((isNull(uniqueIdValue) && isNull(sortFieldValue)) || (nonNull(uniqueIdValue) && nonNull(sortFieldValue))))
            throw new BusinessException(CommonException.GLOBAL_INVALID_INPUT_EXCEPTION);
    }

    public boolean isCursorExist(){
        return nonNull(uniqueIdValue);
    }
}
