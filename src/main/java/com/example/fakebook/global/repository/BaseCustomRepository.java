package com.example.fakebook.global.repository;

import com.example.fakebook.global.dto.internal.CursorPaginationInternalDto;
import com.example.fakebook.global.entity.Base;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class BaseCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * @return WHERE entity.field = value
     */
    protected static BooleanExpression compareField(Expression<?> field, Expression<?> value) {
        return Objects.isNull(value) ? null : Expressions.predicate(Ops.EQ, field, value);
    }

    /**
     * @return WHERE entity.field > value
     * WHERE entity.field < value
     */
    protected static BooleanExpression compareField(
            Expression<?> field,
            Expression<?> value,
            Sort.Direction sortDirection
    ) {
        return Objects.isNull(value) ? null :
                sortDirection.isAscending() ?
                        Expressions.predicate(Ops.GT, field, value) :
                        Expressions.predicate(Ops.LT, field, value);
    }

    /**
     * 커서 페이지네이션에 요구되는 조건식 생성
     */
    protected <T extends Base> BooleanExpression getCursorPaginationQueryCondition(
            CursorPaginationInternalDto<T> cursorPaginationInternalDto
    ) {
        Expression<?> sortFieldValue = cursorPaginationInternalDto.getConvertedSortFieldValue();

        BooleanExpression queryConditionA = compareField(
                cursorPaginationInternalDto.getSubEntitySortFieldExpression(),
                sortFieldValue,
                cursorPaginationInternalDto.getSortDirection()
        );

        BooleanExpression queryConditionB = compareField(
                cursorPaginationInternalDto.getSubEntitySortFieldExpression(),
                sortFieldValue
        );

        BooleanExpression queryConditionC = compareField(
                cursorPaginationInternalDto.getMainEntityIdFieldSimplePath(),
                cursorPaginationInternalDto.getConvertedUniqueIdValue(),
                cursorPaginationInternalDto.getSortDirection()
        );

        return queryConditionA.or(queryConditionB.and(queryConditionC));
    }


    /**
     * 커서 페이지네이션 기본적인 아이디 필터, 정렬, 리미트가 적용된 쿼리 생성
     */
    protected <T extends Base>  JPQLQuery<T>  getCursorPaginationBaseQuery(
            CursorPaginationInternalDto<T> cursorPaginationInternalDto
    ) {
        EntityPath<T> mainEntityPath = cursorPaginationInternalDto.getMainEntityPath();
        JPQLQuery<T> query = jpaQueryFactory.selectFrom(mainEntityPath);

        SimplePath<Long> mainEntityIdField = cursorPaginationInternalDto.getMainEntityIdFieldSimplePath();

        if (cursorPaginationInternalDto.isIdExists()) {
            query = query.where(compareField(mainEntityIdField, cursorPaginationInternalDto.getConvertedId()));
        }

        return query
                .orderBy(
                        cursorPaginationInternalDto.getSubEntitySortFieldOrderSpecifier(),
                        cursorPaginationInternalDto.getMainEntityIdFieldOrderSpecifier()
                )
                .limit(cursorPaginationInternalDto.getLimitWithHasNext());
    }

    /**
     * 다른 엔티티를 참조하지 않는 기본적인 커서 페이지네이션 풀 쿼리 생성
     */
    protected <T extends Base> JPQLQuery<T> getCursorPaginationFullQuery(
            CursorPaginationInternalDto<T> cursorPaginationInternalDto
    ) {
        JPQLQuery<T> query = getCursorPaginationBaseQuery(cursorPaginationInternalDto);

        if (cursorPaginationInternalDto.isCursorExists()) {
            BooleanExpression cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                    cursorPaginationInternalDto
            );
            query.where(cursorPaginationQueryCondition);
        }

        return query;
    }
}

