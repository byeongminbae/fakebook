package com.example.boilerplate.api.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {
    private String email;
    private String phoneNumber;
}
