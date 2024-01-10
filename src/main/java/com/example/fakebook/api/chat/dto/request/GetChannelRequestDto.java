package com.example.fakebook.api.chat.dto.request;

import com.example.fakebook.api.chat.enums.ChannelSortField;
import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ParameterObject
public class GetChannelRequestDto extends CursorPaginationRequestDto {
    @Parameter(description = "Column name for sorting", required = true)
    private ChannelSortField sortField;

    @Parameter(description = "Filter by channel title")
    private String title;

    public GetChannelRequestDto(Long id, Integer limit, Sort.Direction sortDirection, Long uniqueIdValue, String sortFieldValue) {
        super(id, limit, sortDirection, uniqueIdValue, sortFieldValue);
    }
}
