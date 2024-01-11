package com.example.fakebook.global.repository;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.interfaces.SortField;
import com.example.fakebook.global.util.ReflectionUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static com.querydsl.core.types.dsl.Expressions.path;

@RequiredArgsConstructor
public abstract class BaseCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
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

    private String replaceFirstStringToLowercase(String string){
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    private <T extends Base> BooleanBuilder getCursorPaginationQueryCondition(
            Class<T> entity,
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ) {
        BooleanBuilder queryConditions = new BooleanBuilder();

        Path<?> entityPath = Expressions.path(Objects.class, entity.getSimpleName().toLowerCase());
        Path<Long> idPath = Expressions.path(Long.class, entityPath, "id");

        if (Objects.nonNull(cursorPaginationRequestDto.getId()))
            queryConditions.and(filterById(idPath, cursorPaginationRequestDto.getId()));

        if (cursorPaginationRequestDto.isCursorExists()) {
            Path<?> sortFieldPath = Expressions.path(Object.class, entityPath, sortField.getSortFieldName());
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

    private static Integer hasNext(Integer limit) {
        return limit + 1;
    }

    protected <E, P extends Comparable<P>> OrderSpecifier<P> getOrderSpecifier(
            Class<E> entity,
            Sort.Direction sortDirection,
            String sortFieldName
    ) {
        String entityName = replaceFirstStringToLowercase(entity.getSimpleName());
        Class<?> type = ReflectionUtil.getFieldType(entity, sortFieldName);
        Path<?> entityPath = Expressions.path(Objects.class, entityName);
        SimplePath<?> path = path(type, entityPath, sortFieldName);
        return new OrderSpecifier(sortDirection.isAscending() ? Order.ASC : Order.DESC, path);
    }

    protected  <T extends Base> JPQLQuery<T> getBaseQuery(
            Class<T> entity,
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ) {
        BooleanBuilder cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                entity,
                cursorPaginationRequestDto,
                sortField
        );

        OrderSpecifier<?> customOrderSpecifier = getOrderSpecifier(
                sortField.getSortEntityClass(),
                cursorPaginationRequestDto.getSortDirection(),
                sortField.getSortFieldName()
        );

        OrderSpecifier<Long> defaultOrderSpecifier = getOrderSpecifier(
                entity,
                cursorPaginationRequestDto.getSortDirection(),
                "id"
        );

        PathBuilder<T> entityPath = new PathBuilder<>(entity, entity.getSimpleName().toLowerCase());

        return jpaQueryFactory.selectFrom(entityPath)
                .where(cursorPaginationQueryCondition)
                .orderBy(customOrderSpecifier, defaultOrderSpecifier)
                .limit(hasNext(cursorPaginationRequestDto.getLimit()));
    }
}
