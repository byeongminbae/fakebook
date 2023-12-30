package com.example.fakebook.api.user.dto.request;

import lombok.Getter;

@Getter
public class CreateUserRequestDto {
    private String signId;
    private String signPassword;
    private String email;
    private String phoneNumber;
}
