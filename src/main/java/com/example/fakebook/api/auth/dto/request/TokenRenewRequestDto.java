package com.example.fakebook.api.auth.dto.request;

import lombok.Getter;

@Getter
public class TokenRenewRequestDto {
    private String refreshToken;
}
