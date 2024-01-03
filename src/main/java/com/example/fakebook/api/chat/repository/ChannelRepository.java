package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends BaseRepository<Channel, Long> {
}
