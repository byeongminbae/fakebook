package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
import com.example.fakebook.global.repository.BasePaginationRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ChannelPaginationRepository extends BasePaginationRepository {
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
        OrderSpecifier<?> customOrderSpecifier = getOrderSpecifier(
                qChannel,
                getChannelRequestDto.getSortDirection(),
                getChannelRequestDto.getSortField()
        );

        OrderSpecifier<Long> defaultOrderSpecifier = getChannelRequestDto
                .getSortDirection()
                .isAscending() ? qChannel.id.asc() : qChannel.id.desc();

        BooleanBuilder cursorPaginationQueryCondition = getCursorPaginationQueryCondition(
                "channel",
                getChannelRequestDto,
                getChannelRequestDto.getSortField()
        );

        BooleanExpression queryCondition1 = Objects.isNull(getChannelRequestDto.getTitle()) ?
                null :
                qChannel.title.contains(getChannelRequestDto.getTitle());

        return jpaQueryFactory.selectFrom(qChannel)
                .where(cursorPaginationQueryCondition)
                .where(queryCondition1)
                .orderBy(customOrderSpecifier, defaultOrderSpecifier)
                .limit(hasNext(getChannelRequestDto.getLimit()))
                .fetch();
    }
}