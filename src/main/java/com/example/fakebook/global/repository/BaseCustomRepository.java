package com.example.fakebook.global.repository;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.interfaces.SortField;
import com.example.fakebook.global.util.StringUtil;
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
     * @return WHERE entity_x.field_ab > value_ab OR (entity_x.field_ab = value_ab AND entity_y.field_c > value_c)
     * WHERE entity_x.field_ab < value_ab OR (entity_x.field_ab = value_ab AND entity_y.field_c < value_c)
     */
    protected <T extends Base> BooleanExpression getCursorPaginationQueryCondition(
            Class<T> mainEntityClass,
            Expression<?> expression,
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ) {
        Expression<?> convertedSortFieldValue = sortField.convertSortFieldValue(
                cursorPaginationRequestDto.getSortFieldValue()
        );

        BooleanExpression booleanExpressionA = compareField(
                expression,
                convertedSortFieldValue,
                cursorPaginationRequestDto.getSortDirection()
        );

        BooleanExpression booleanExpressionB = compareField(expression, convertedSortFieldValue);

        BooleanExpression booleanExpressionC = compareField(
                Expressions.path(Long.class, getEntityPath(mainEntityClass), "id"),
                Expressions.constant(cursorPaginationRequestDto.getUniqueIdValue()),
                cursorPaginationRequestDto.getSortDirection()
        );

        return booleanExpressionA.or(booleanExpressionB.and(booleanExpressionC));
    }


    /**
     * 커서 페이지네이션 기본적인 아이디 필터, 정렬, 리미트가 적용된 쿼리
     * @param mainEntityClass SELECT FROM, ID 정렬 기준 생성에 사용
     * @param subEntitySortExpression 첫번째 정렬 기준 생성에 사용되는 표현. e.g. entity.field, COUNT(entity.field)
     * @param cursorPaginationRequestDto 아이디 필터 조건, 정렬 방향, 리미트 정보가 포함된 DTO
     * @param <T> 모든 엔티티는 Base 를 상속받아야 한다
     */
    protected <T extends Base> JPQLQuery<T> getCursorPaginationBaseQuery(
            Class<T> mainEntityClass,
            Expression<?> subEntitySortExpression,
            CursorPaginationRequestDto cursorPaginationRequestDto
    ) {
        EntityPath<T> mainEntityPath = getEntityPath(mainEntityClass);

        JPQLQuery<T> query = jpaQueryFactory.selectFrom(mainEntityPath);
        SimplePath<Long> idFieldSimplePath = Expressions.path(Long.class, mainEntityPath, "id");

        if (cursorPaginationRequestDto.isIdExist()) {
            Expression<Long> id = Expressions.constant(cursorPaginationRequestDto.getId());
            query = query.where(compareField(idFieldSimplePath, id));
        }

        OrderSpecifier<? extends Comparable> orderSpecifier = new OrderSpecifier(
                cursorPaginationRequestDto.getSortDirection().isAscending() ?
                        Order.ASC :
                        Order.DESC,
                subEntitySortExpression
        );

        OrderSpecifier<Long> idFieldOrderSpecifier = new OrderSpecifier<>(
                cursorPaginationRequestDto.getSortDirection().isAscending() ?
                        Order.ASC :
                        Order.DESC,
                idFieldSimplePath
        );

        return query
                .orderBy(orderSpecifier, idFieldOrderSpecifier)
                .limit(hasNext(cursorPaginationRequestDto.getLimit()));
    }

    /**
     * 다른 엔티티를 참조하지 않는 기본적인 커서 페이지네이션 쿼리 생성
     * @param mainEntityClass 쿼리의 주체가 되는 엔티티
     * @param cursorPaginationRequestDto 추상화된 커서 페이지네이션 요청 DTO
     * @param sortField 정렬 기준
     * @param <T> 모든 엔티티는 Base 를 상속받아야 한다
     */
    protected <T extends Base> JPQLQuery<T> getCursorPaginationCommonQuery(
            Class<T> mainEntityClass,
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ) {
        Expression<?> mainEntityField = Expressions.path(
                Object.class,
                getEntityPath(mainEntityClass),
                sortField.getEntityFieldName()
        );

        JPQLQuery<T> jpqlQuery = getCursorPaginationBaseQuery(
                mainEntityClass,
                mainEntityField,
                cursorPaginationRequestDto
        );

        if (cursorPaginationRequestDto.isCursorExists()) {
            BooleanExpression cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                    mainEntityClass,
                    mainEntityField,
                    cursorPaginationRequestDto,
                    sortField
            );
            jpqlQuery.where(cursorPaginationQueryCondition);
        }

        return jpqlQuery;
    }

    protected static <T extends Base> EntityPath<T> getEntityPath(Class<T> entityClass) {
        return new PathBuilder<>(entityClass, StringUtil.replaceFirstStringToLowercase(entityClass.getSimpleName()));
    }

    protected static Integer hasNext(Integer limit) {
        return limit + 1;
    }
}

