package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public interface ChannelRepository extends BaseRepository<Channel, Long> {

    Channel findByIdAndChannelMembersMemberIdAndDeletedAtIsNull(Long id, Long memberId);
    Channel findByTitle(String title);
    default Channel findByIdAndChannelMembersMemberIdAndDeletedAtIsNullThrowIfNull(Long id, Long memberId) {
        Channel entity = findByIdAndChannelMembersMemberIdAndDeletedAtIsNull(id, memberId);
        if (Objects.isNull(entity))
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return entity;
    }

}
