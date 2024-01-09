package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
import com.example.fakebook.global.repository.BaseCustomRepository;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ChannelCustomRepository extends BaseCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QChannel qChannel = QChannel.channel;

    /**
     * Perform cursor pagination
     * Perform multiple sorting for the same row appearing in the preceding sorting field
     *
     * @param getChannelRequestDto DTO inherits cursor pagination
     * @return Paginated entity list
     */
    public List<Channel> find(GetChannelRequestDto getChannelRequestDto) {
        getChannelRequestDto.validate();

        OrderSpecifier<?> customOrderSpecifier = getOrderSpecifier(
                qChannel,
                getChannelRequestDto.getSortDirection(),
                getChannelRequestDto.getSortField()
        );

        OrderSpecifier<Long> defaultOrderSpecifier = getChannelRequestDto
                .getSortDirection()
                .isAscending() ? qChannel.id.asc() : qChannel.id.desc();

        BooleanExpression condition = Objects.isNull(getChannelRequestDto.getTitle()) ?
                null :
                qChannel.title.contains(getChannelRequestDto.getTitle());

        return jpaQueryFactory.selectFrom(qChannel)
                .where(getQueryCondition(getChannelRequestDto))
                .where(condition)
                .orderBy(customOrderSpecifier, defaultOrderSpecifier)
                .limit(hasNext(getChannelRequestDto.getLimit()))
                .fetch();
    }

    public BooleanExpression getQueryCondition(GetChannelRequestDto getChannelRequestDto) {
        if (!getChannelRequestDto.isCursorExist()) return null;

        Path<?> sortFieldPath = Expressions.path(
                getChannelRequestDto.getSortField().getFieldClass(),
                qChannel,
                getChannelRequestDto.getSortField().getFieldName()
        );

        Expression<?> sortFieldValue = getChannelRequestDto
                .getSortField()
                .convertSortFieldValue(getChannelRequestDto.getSortFieldValue());

        Expression<?> uniqueIdValue = Expressions.constant(getChannelRequestDto.getUniqueIdValue());

        BooleanExpression condition0 = getChannelRequestDto.getSortDirection().isAscending() ?
                Expressions.predicate(Ops.GT, sortFieldPath, sortFieldValue) :
                Expressions.predicate(Ops.LT, sortFieldPath, sortFieldValue);

        BooleanExpression condition1 = Expressions.predicate(Ops.EQ, sortFieldPath, sortFieldValue);

        BooleanExpression condition2 = getChannelRequestDto.getSortDirection().isAscending() ?
                Expressions.predicate(Ops.GT, sortFieldPath, sortFieldValue) :
                Expressions.predicate(Ops.LT, qChannel.id, uniqueIdValue);

        return condition0.or(condition1.and(condition2));
    }
}