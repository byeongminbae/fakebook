package com.example.fakebook.global.interfaces;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;

public interface SortField{
    Class getSortEntityClass();
    String getSortFieldName();
    default Expression<?> convertSortFieldValue(String sortFieldValue) {
        return Expressions.constant(sortFieldValue);
    }
}
