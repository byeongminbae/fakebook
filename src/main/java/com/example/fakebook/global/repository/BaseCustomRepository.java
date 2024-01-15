package com.example.fakebook.global.repository;

import com.example.fakebook.global.dto.request.CursorPaginationRequestDto;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.interfaces.SortField;
import com.example.fakebook.global.util.StringUtil;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
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
//        SimplePath<?> entityFieldSimplePath = getEntityFieldSimplePath(getEntitySimplePath(entityClass), fieldName);
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
    protected BooleanExpression getCursorPaginationQueryCondition(
            Expression<?> fieldAB,
            Expression<?> valueAB,
            Expression<?> fieldC,
            Expression<?> valueC,
            Sort.Direction sortDirection
    ) {
//        if (Objects.isNull(fieldAB)
//                || Objects.isNull(valueAB)
//                || Objects.isNull(fieldC)
//                || Objects.isNull(valueC)
//                || Objects.isNull(sortDirection)
//        ) {
//            throw new BusinessException(CommonException.COMMON_INVALID_INPUT_EXCEPTION);
//        }

        BooleanExpression booleanExpressionA = compareField(fieldAB, valueAB, sortDirection);
        BooleanExpression booleanExpressionB = compareField(fieldAB, valueAB);
        BooleanExpression booleanExpressionC = compareField(fieldC, valueC, sortDirection);

        return booleanExpressionA.or(booleanExpressionB.and(booleanExpressionC));
    }

    protected static <T extends Base> EntityPath<T> getEntityPath(Class<T> entityClass) {
        return new PathBuilder<>(entityClass, StringUtil.replaceFirstStringToLowercase(entityClass.getSimpleName()));
    }

    protected static <T extends Base> SimplePath<?> getFieldSimplePath(EntityPath<T> entitySimplePath, String fieldName) {
        return Expressions.path(Object.class, entitySimplePath, fieldName);
    }

    protected static <T extends Base> StringPath getFieldStringPath(EntityPath<T> entitySimplePath, String fieldName) {
        return Expressions.stringPath(entitySimplePath, fieldName);
    }

    protected static Integer hasNext(Integer limit) {
        return limit + 1;
    }

    protected OrderSpecifier<?> getOrderSpecifier(
            SimplePath<?> fieldSimplePath,
            Sort.Direction sortDirection
    ) {
        return new OrderSpecifier(sortDirection.isAscending() ? Order.ASC : Order.DESC, fieldSimplePath);
    }

    protected OrderSpecifier<?> getOrderSpecifier(
            Expression<?> fieldSimplePath,
            Sort.Direction sortDirection
    ) {
        return new OrderSpecifier(sortDirection.isAscending() ? Order.ASC : Order.DESC, fieldSimplePath);
    }

    protected <T extends Base> JPQLQuery<T> getCursorPaginationFullQuery(
            Class<T> entityClass,
            CursorPaginationRequestDto cursorPaginationRequestDto,
            SortField sortField
    ) {
        EntityPath<T> entityPath = getEntityPath(entityClass);
        SimplePath<?> sortFieldSimplePath = getFieldSimplePath(entityPath, sortField.getEntityFieldName());
        SimplePath<?> idFieldSimplePath = getFieldSimplePath(entityPath, "id");
        JPQLQuery<T> jpqlQuery = jpaQueryFactory.selectFrom(entityPath);


        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(
                sortFieldSimplePath,
                cursorPaginationRequestDto.getSortDirection()
        );

        OrderSpecifier<?> idFieldOrderSpecifier = getOrderSpecifier(
                idFieldSimplePath,
                cursorPaginationRequestDto.getSortDirection()
        );

        jpqlQuery
                .orderBy(orderSpecifier, idFieldOrderSpecifier)
                .limit(hasNext(cursorPaginationRequestDto.getLimit()));

        if (cursorPaginationRequestDto.isIdExist()) {
            jpqlQuery.where(compareField(idFieldSimplePath, Expressions.constant(cursorPaginationRequestDto.getId())));
        }

        if (cursorPaginationRequestDto.isCursorExists()) {
            Expression<?> convertedSortFieldValue = sortField.convertSortFieldValue(
                    cursorPaginationRequestDto.getSortFieldValue()
            );
            Expression<Long> convertedIdValue = Expressions.constant(cursorPaginationRequestDto.getUniqueIdValue());

            BooleanExpression cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                    sortFieldSimplePath,
                    convertedSortFieldValue,
                    idFieldSimplePath,
                    convertedIdValue,
                    cursorPaginationRequestDto.getSortDirection()
            );
            jpqlQuery.where(cursorPaginationQueryCondition);
        }

        return jpqlQuery;
    }
}
