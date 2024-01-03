package com.example.fakebook.api.common.repository;

import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelMemberRepository extends BaseRepository<ChannelMember, Long> {
}
