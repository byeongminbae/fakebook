package com.example.boilerplate.global.auth.token.dto.internal;

import com.example.boilerplate.global.enums.TokenType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class TokenPayloadInternalDto {
    private Long userId;
    private TokenType tokenType;
}
