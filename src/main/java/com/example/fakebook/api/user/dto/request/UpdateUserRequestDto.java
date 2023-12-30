package com.example.fakebook.api.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {
    private String email;
    private String phoneNumber;
}
