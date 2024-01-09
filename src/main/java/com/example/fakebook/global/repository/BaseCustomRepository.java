package com.example.fakebook.global.repository;

import com.example.fakebook.global.entity.QBase;
import com.example.fakebook.global.interfaces.SortField;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static com.querydsl.core.types.dsl.Expressions.path;

public class BaseCustomRepository {

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
