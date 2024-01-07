package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
import com.example.fakebook.api.chat.enums.ChannelSortField;
import com.example.fakebook.global.dto.internal.CursorPaginationInternalDto;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ChannelCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QChannel qChannel = QChannel.channel;
    public List<Channel> find(CursorPaginationInternalDto cursorPaginationInternalDto) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(
                cursorPaginationInternalDto.getSortDirection(),
                cursorPaginationInternalDto.getSortField().getName()
        );

        return jpaQueryFactory.selectFrom(qChannel)
                .where(getIdCondition(cursorPaginationInternalDto))
                .limit(cursorPaginationInternalDto.getLimit())
                .orderBy(orderSpecifier)
                .fetch();
    }

    private BooleanExpression getIdCondition(CursorPaginationInternalDto cursorPaginationInternalDto) {
        if (Objects.isNull(cursorPaginationInternalDto.getId())) {
            return null;
        }
        if (cursorPaginationInternalDto.getSortDirection().isAscending()) {
            return qChannel.id.goe(cursorPaginationInternalDto.getId());
        }
        return qChannel.id.loe(cursorPaginationInternalDto.getId());
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort.Direction sortDirection, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, qChannel, fieldName);
        return new OrderSpecifier(sortDirection.isAscending() ? Order.ASC : Order.DESC, fieldPath);
    }
}