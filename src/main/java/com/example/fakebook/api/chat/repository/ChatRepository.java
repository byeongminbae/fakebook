package com.example.fakebook.api.chat.repository;

import com.example.fakebook.api.chat.entity.Chat;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends BaseRepository<Chat, Long> {
}
