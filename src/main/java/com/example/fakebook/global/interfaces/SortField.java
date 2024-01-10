package com.example.fakebook.global.interfaces;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface SortField{
    String getFieldName();
    Class getFieldClass();
    Expression<?> convertSortFieldValue(String sortFieldValue);
}
