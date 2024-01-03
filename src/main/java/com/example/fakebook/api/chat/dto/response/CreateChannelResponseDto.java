package com.example.fakebook.api.chat.dto.response;

import com.example.fakebook.api.chat.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateChannelResponseDto {
    private Long id;

    public static CreateChannelResponseDto from(Channel channel){
        return CreateChannelResponseDto.builder()
                .id(channel.getId())
                .build();
    }
}
