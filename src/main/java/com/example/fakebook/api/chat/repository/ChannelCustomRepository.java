package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
import com.example.fakebook.api.common.entity.QChannelMember;
import com.example.fakebook.global.dto.internal.CursorPaginationInternalDto;
import com.example.fakebook.global.repository.BaseCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    public ChannelCustomRepository(JPAQueryFactory jpaQueryFactory) {
        super(jpaQueryFactory);
    }

    private BooleanExpression titleFilter(String title) {
        return Objects.isNull(title) ? null : qChannel.title.contains(title);
    }

    public List<Channel> find(GetChannelRequestDto getChannelRequestDto) {
        BooleanExpression titleFilter = titleFilter(getChannelRequestDto.getTitle());

        switch (getChannelRequestDto.getSortField()) {
            default: {
                CursorPaginationInternalDto<Channel> cursorPaginationInternalDto = CursorPaginationInternalDto.from(
                        Channel.class,
                        qChannel.id,
                        getChannelRequestDto.getSortField(),
                        getChannelRequestDto
                );

                JPQLQuery<Channel> cursorPaginationFullQuery = getCursorPaginationFullQuery(
                        cursorPaginationInternalDto
                );

                return cursorPaginationFullQuery
                        .where(titleFilter)
                        .fetch();
            }
            case MEMBER_COUNT: {
                CursorPaginationInternalDto<Channel> cursorPaginationInternalDto = CursorPaginationInternalDto.from(
                        Channel.class,
                        qChannelMember.id.count(),
                        getChannelRequestDto.getSortField(),
                        getChannelRequestDto
                );

                JPQLQuery<Channel> baseQuery = getCursorPaginationBaseQuery(cursorPaginationInternalDto);
                JPQLQuery<Tuple> query = baseQuery
                        .select(qChannel, qChannelMember.id.count())
                        .where(titleFilter)
                        .groupBy(qChannel.id)
                        .leftJoin(qChannel.channelMembers, qChannelMember);

                if (cursorPaginationInternalDto.isCursorExists()) {
                    BooleanExpression cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                            cursorPaginationInternalDto
                    );
                    query = query.having(cursorPaginationQueryCondition);
                }

                return query.fetch()
                        .stream()
                        .map((it) -> it.get(0, Channel.class))
                        .collect(Collectors.toList());
            }
        }
    }
}