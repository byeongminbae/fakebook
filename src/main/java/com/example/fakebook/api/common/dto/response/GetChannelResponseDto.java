package com.example.fakebook.api.common.dto.response;

import com.example.fakebook.api.chat.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetChannelResponseDto {
    private Long id;
    private String title;
    private String description;
    private Long memberCount;
    private Long chatCount;
    private String creatorSignId;
    private String creatorEmail;
    private String createdAt;

    public static GetChannelResponseDto from(Channel channel){
        return GetChannelResponseDto.builder()
                .id(channel.getId())
                .title(channel.getTitle())
                .description(channel.getDescription())
                .memberCount(channel.getChannelMembers().stream().count())
                .chatCount(channel.getChats().stream().count())
                .creatorSignId(channel.getCreator().getSignId())
                .creatorEmail(channel.getCreator().getEmail())
                .createdAt(channel.getCreatedAt().toString())
                .build();
    }
}
