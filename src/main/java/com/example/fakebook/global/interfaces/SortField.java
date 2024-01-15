package com.example.fakebook.global.interfaces;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;

/**
 * 정렬 및 필터링에 사용되는 enum 인터페이스
 */
public interface SortField{
    /**
     * @return 기준 엔티티에 선언된 정렬 기준 필드 이름.
     */
    String getEntityFieldName();

    /**
     * @return 응답 DTO 에서 다음 커서 sortFieldValue 로 사용될 필드 이름
     */
    String getDtoFieldName();

    /**
     * @param sortFieldValue CursorPaginationRequestDto 에 선언된 필드
     * @return sortFieldValue 를 정렬 기준에 맞는 자료형으로 파싱
     */
    default Expression<?> convertSortFieldValue(String sortFieldValue) {
        return Expressions.constant(sortFieldValue);
    }
}

