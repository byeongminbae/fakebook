package com.example.fakebook.api.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateChannelRequestDto {
    @Schema(example = "channelTitle")
    private String title;
    @Schema(example = "channelDescription")
    private String description;
}
