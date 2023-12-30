package com.example.fakebook.api.auth.dto.request;

import lombok.Getter;

@Getter
public class TokenSignOutRequestDto {
    private String refreshToken;
}
