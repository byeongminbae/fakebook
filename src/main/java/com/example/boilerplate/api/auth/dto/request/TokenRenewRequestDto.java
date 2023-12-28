package com.example.boilerplate.api.auth.dto.request;

import lombok.Getter;

@Getter
public class TokenRenewRequestDto {
    private String refreshToken;
}
