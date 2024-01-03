package com.example.fakebook.api.chat.dto.request;

import lombok.Getter;

@Getter
public class CreateChannelRequestDto {
    private String title;
    private String description;
}
