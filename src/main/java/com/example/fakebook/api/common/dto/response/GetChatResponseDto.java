package com.example.fakebook.api.common.dto.response;

import com.example.fakebook.api.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetChatResponseDto {
    private Long id;
    private String content;
    private Long authorId;
    private String authorSignId;
    private String authorEmail;
    private String createdAt;

    public static GetChatResponseDto from(Chat chat){
        return GetChatResponseDto.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .authorId(chat.getAuthor().getId())
                .authorSignId(chat.getAuthor().getSignId())
                .authorEmail(chat.getAuthor().getEmail())
                .createdAt(chat.getCreatedAt().toString())
                .build();
    }
}
