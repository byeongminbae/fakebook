package com.example.fakebook.global.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ParameterObject
public class CursorPaginationRequestDto {
    @Parameter(description = "ID unique value, initial page requested with an empty value")
    private Long id;
    @Parameter(description = "Maximum number of data entries", example = "1")
    private Integer limit;
    @Parameter(description = "Sorting direction", example = "DESC")
    private Sort.Direction sortDirection;
}
