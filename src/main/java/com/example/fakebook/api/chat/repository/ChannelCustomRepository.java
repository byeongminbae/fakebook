package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
import com.example.fakebook.api.common.entity.QChannelMember;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ChannelCustomRepository extends BaseCustomRepository {
    private final QChannel qChannel = QChannel.channel;
    private final QChannelMember qChannelMember = QChannelMember.channelMember;
    private final JPAQueryFactory jpaQueryFactory;

    public ChannelCustomRepository(JPAQueryFactory jpaQueryFactory) {
        super(jpaQueryFactory);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression titleFilter(String title) {
        return Objects.isNull(title) ?
                null :
                getFieldStringPath(getEntityPath(Channel.class), "title").contains(title);
    }

    public List<Channel> find(GetChannelRequestDto getChannelRequestDto) {
        BooleanExpression titleFilter = titleFilter(getChannelRequestDto.getTitle());
        switch (getChannelRequestDto.getSortField()) {
            default: {
                JPQLQuery<Channel> cursorPaginationFullQuery = getCursorPaginationFullQuery(
                        Channel.class,
                        getChannelRequestDto,
                        getChannelRequestDto.getSortField()
                );

                return cursorPaginationFullQuery
                        .where(titleFilter)
                        .fetch();
            }
            case MEMBER_COUNT: {
                JPQLQuery<Tuple> jpqlQuery = jpaQueryFactory
                        .select(qChannel, qChannel.id.count())
                        .from(qChannel)
                        .where(titleFilter)
                        .groupBy(qChannel.id)
                        .leftJoin(qChannel.channelMembers, qChannelMember);

                OrderSpecifier<?> orderSpecifier = getOrderSpecifier(
                        qChannelMember.id.count(),
                        getChannelRequestDto.getSortDirection()
                );

                OrderSpecifier<?> idFieldOrderSpecifier = getOrderSpecifier(
                        qChannel.id,
                        getChannelRequestDto.getSortDirection()
                );

                jpqlQuery = jpqlQuery
                        .orderBy(orderSpecifier, idFieldOrderSpecifier)
                        .limit(hasNext(getChannelRequestDto.getLimit()));

                if (getChannelRequestDto.isCursorExists()) {
                    Expression<?> convertedSortFieldValue = getChannelRequestDto.getSortField().convertSortFieldValue(
                            getChannelRequestDto.getSortFieldValue()
                    );
                    Expression<Long> convertedIdValue = Expressions.constant(getChannelRequestDto.getUniqueIdValue());

                    BooleanExpression cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                            qChannelMember.id.count(),
                            convertedSortFieldValue,
                            qChannel.id,
                            convertedIdValue,
                            getChannelRequestDto.getSortDirection()
                    );

                    jpqlQuery = jpqlQuery.having(cursorPaginationQueryCondition);
                }

                return jpqlQuery.fetch()
                        .stream()
                        .map((it) -> it.get(0, Channel.class))
                        .collect(Collectors.toList());
            }
        }
    }
}