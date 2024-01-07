package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
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

    public List<Channel> find(GetChannelRequestDto getChannelRequestDto) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(
                getChannelRequestDto.getSortDirection(),
                getChannelRequestDto.getSortField().getName()
        );

        return jpaQueryFactory.selectFrom(qChannel)
                .where(getIdCondition(getChannelRequestDto.getId(), getChannelRequestDto.getSortDirection()))
                .where(getTitleCondition(getChannelRequestDto.getTitle()))
                .limit(getNextEntityWithLimit(getChannelRequestDto))
                .orderBy(orderSpecifier)
                .fetch();
    }

    private BooleanExpression getIdCondition(Long id, Sort.Direction sortDirection) {
        return Objects.isNull(id) ? null : sortDirection.isAscending() ? qChannel.id.goe(id) : qChannel.id.loe(id);
    }

    private BooleanExpression getTitleCondition(String title) {
        return Objects.isNull(title) ? null : qChannel.title.contains(title);
    }

    private static Integer getNextEntityWithLimit(GetChannelRequestDto getChannelRequestDto) {
        return getChannelRequestDto.getLimit() + 1;
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort.Direction sortDirection, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, qChannel, fieldName);
        return new OrderSpecifier(sortDirection.isAscending() ? Order.ASC : Order.DESC, fieldPath);
    }
}