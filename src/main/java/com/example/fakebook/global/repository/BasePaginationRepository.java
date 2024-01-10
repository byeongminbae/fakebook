package com.example.fakebook.global.repository;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.interfaces.SortField;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static com.querydsl.core.types.dsl.Expressions.path;

public abstract class BasePaginationRepository {
    /**
     * @return WHERE c0.id = x
     */
    private BooleanExpression filterById(Expression<Long> idPath, Long id) {
        return Objects.isNull(id) ? null : Expressions.predicate(Ops.EQ, idPath, Expressions.constant(id));
    }

    /**
     * @return WHERE c0.id > x || WHERE c0.id < x
     */
    private BooleanExpression filterById(Expression<Long> idPath, Long id, Sort.Direction sortDirection) {
        return sortDirection.isAscending() ?
                Expressions.predicate(Ops.GT, idPath, Expressions.constant(id)) :
                Expressions.predicate(Ops.LT, idPath, Expressions.constant(id));
    }

    /**
     * @return WHERE c0.description = x
     */
    private BooleanExpression filterBySortField(Path<?> sortFieldPath, Expression<?> sortFieldValue) {
        return Expressions.predicate(Ops.EQ, sortFieldPath, sortFieldValue);
    }

    /**
     * @return WHERE c0.description > x || WHERE c0.description < x
     */
    private BooleanExpression filterBySortField(
            Path<?> sortFieldPath,
            Expression<?> sortFieldValue,
            Sort.Direction sortDirection
    ) {
        return sortDirection.isAscending() ?
                Expressions.predicate(Ops.GT, sortFieldPath, sortFieldValue) :
                Expressions.predicate(Ops.LT, sortFieldPath, sortFieldValue);
    }

    protected BooleanBuilder getCursorPaginationQueryCondition(
            String tableName,
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ) {
        BooleanBuilder queryConditions = new BooleanBuilder();

        Path<?> entityPath = Expressions.path(Objects.class, tableName);
        Path<Long> idPath = Expressions.path(Long.class, entityPath, "id");

        if (Objects.nonNull(cursorPaginationRequestDto.getId()))
             queryConditions.and(filterById(idPath, cursorPaginationRequestDto.getId()));

        if (cursorPaginationRequestDto.isCursorExists()) {
            Path<?> sortFieldPath = Expressions.path(sortField.getFieldClass(), entityPath, sortField.getFieldName());
            Expression<?> sortFieldValue = sortField.convertSortFieldValue(cursorPaginationRequestDto.getSortFieldValue());

            BooleanExpression queryCondition0 = filterBySortField(
                    sortFieldPath,
                    sortFieldValue,
                    cursorPaginationRequestDto.getSortDirection()
            );

            BooleanExpression queryCondition1 = filterBySortField(
                    sortFieldPath,
                    sortFieldValue
            );

            BooleanExpression queryCondition2 = filterById(
                    idPath,
                    cursorPaginationRequestDto.getUniqueIdValue(),
                    cursorPaginationRequestDto.getSortDirection()
            );

            queryConditions.and(queryCondition0.or(queryCondition1.and(queryCondition2)));
        }
        return queryConditions;
    }

    protected static Integer hasNext(Integer limit) {
        return limit + 1;
    }

    protected <E, P extends Comparable<P>> OrderSpecifier<P> getOrderSpecifier(
            EntityPathBase<E> qEntity,
            Sort.Direction sortDirection,
            SortField sortField
    ) {
        SimplePath<P> path = path(sortField.getFieldClass(), qEntity, sortField.getFieldName());

        return new OrderSpecifier<>(sortDirection.isAscending() ? Order.ASC : Order.DESC, path);
    }
}
