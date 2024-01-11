package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.QChannel;
import com.example.fakebook.global.repository.BaseCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ChannelCustomRepository extends BaseCustomRepository {
    private final QChannel qChannel = QChannel.channel;

    public ChannelCustomRepository(JPAQueryFactory jpaQueryFactory) {
        super(jpaQueryFactory);
    }

    public List<Channel> find(GetChannelRequestDto getChannelRequestDto) {
        BooleanExpression titleFilter = Objects.isNull(getChannelRequestDto.getTitle()) ?
                null :
                qChannel.title.contains(getChannelRequestDto.getTitle());

        return getBaseQuery(Channel.class, getChannelRequestDto, getChannelRequestDto.getSortField())
                .where(titleFilter)
                .fetch();
    }
}