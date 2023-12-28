package com.example.boilerplate.api.auth.dto.request;

import lombok.Getter;

@Getter
public class TokenSignInRequestDto {
    private String signId;
    private String signPassword;
}
