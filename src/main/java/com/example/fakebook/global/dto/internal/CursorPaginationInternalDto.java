package com.example.fakebook.global.dto.internal;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.interfaces.SortField;
import com.example.fakebook.global.util.StringUtil;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimplePath;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import static java.util.Objects.nonNull;

@Builder
public class CursorPaginationInternalDto<T extends Base> {
    private final Class<T> mainEntityClass;
    @Getter
    private final Expression<? extends Comparable<?>> subEntitySortFieldExpression;
    private final SortField sortField;
    private final Long id;
    private final Integer limit;
    @Getter
    private final Sort.Direction sortDirection;
    private final Long uniqueIdValue;
    private final String sortFieldValue;

    public static <T extends Base> CursorPaginationInternalDto<T> from(
            Class<T> mainEntityClass,
            Expression<? extends Comparable<?>> subEntitySortFieldExpression,
            SortField sortField,
            CursorPaginationRequestDto cursorPaginationRequestDto
    ) {
        return CursorPaginationInternalDto.<T>builder()
                .mainEntityClass(mainEntityClass)
                .subEntitySortFieldExpression(subEntitySortFieldExpression)
                .sortField(sortField)
                .limit(cursorPaginationRequestDto.getLimit())
                .sortDirection(cursorPaginationRequestDto.getSortDirection())
                .uniqueIdValue(cursorPaginationRequestDto.getUniqueIdValue())
                .sortFieldValue(cursorPaginationRequestDto.getSortFieldValue())
                .build();
    }

    private EntityPath<T> getEntityPath(Class<T> entityClass) {
        return new PathBuilder<>(entityClass, StringUtil.replaceFirstStringToLowercase(entityClass.getSimpleName()));
    }

    public Expression<?> getConvertedSortFieldValue() {
        return sortField.convertSortFieldValue(sortFieldValue);
    }

    public Expression<Long> getConvertedUniqueIdValue() {
        return Expressions.constant(uniqueIdValue);
    }

    public SimplePath<Long> getMainEntityIdFieldSimplePath() {
        return Expressions.path(Long.class, getMainEntityPath(), "id");
    }

    public OrderSpecifier<? extends Comparable<?>> getSubEntitySortFieldOrderSpecifier() {
        return new OrderSpecifier<>(sortDirection.isAscending() ? Order.ASC : Order.DESC, subEntitySortFieldExpression);
    }

    public OrderSpecifier<Long> getMainEntityIdFieldOrderSpecifier() {
        return new OrderSpecifier<>(sortDirection.isAscending() ? Order.ASC : Order.DESC, getMainEntityIdFieldSimplePath());
    }

    public EntityPath<T> getMainEntityPath() {
        return getEntityPath(mainEntityClass);
    }
    public Integer getLimitWithHasNext() {
        return limit + 1;
    }

    public Expression<Long> getConvertedId() {
        return Expressions.constant(id);
    }

    public boolean isCursorExists() {
        return nonNull(uniqueIdValue);
    }

    public boolean isIdExists() {
        return nonNull(id);
    }
}
