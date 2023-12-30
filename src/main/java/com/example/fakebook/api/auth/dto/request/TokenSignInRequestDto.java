package com.example.fakebook.api.auth.dto.request;

import lombok.Getter;

@Getter
public class TokenSignInRequestDto {
    private String signId;
    private String signPassword;
}
